package com.example.omniwalletapp.repository

import android.text.TextUtils
import com.example.omniwalletapp.BuildConfig
import com.example.omniwalletapp.base.Erc20TokenWrapper
import com.example.omniwalletapp.entity.NetworkInfo
import com.example.omniwalletapp.util.BalanceUtil
import com.example.omniwalletapp.util.BalanceUtil.convertTogEstimateGasEth
import com.example.omniwalletapp.util.Constants
import io.reactivex.Observable
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthEstimateGas
import org.web3j.protocol.core.methods.response.EthGasPrice
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.TransactionManager
import org.web3j.tx.response.NoOpProcessor
import org.web3j.tx.response.TransactionReceiptProcessor
import org.web3j.utils.Convert
import timber.log.Timber
import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject

class NetworkRepository @Inject constructor(val preferencesRepository: PreferencesRepository) {

//    private val preferencesRepository = PreferencesRepository(context)


    private val NETWORKS = arrayOf(
        NetworkInfo(
            name = Constants.BSC_TESTNET,
            rpcServerUrl = "https://data-seed-prebsc-1-s1.binance.org:8545/",
            chainId = 97,
            symbol = Constants.BSC_SYMBOL,
            scanUrl = "https://testnet.bscscan.com",
            backendUrl = "https://api-testnet.bscscan.com",
            apiKey = BuildConfig.BSC_API
        ),
        NetworkInfo(
            name = Constants.ROPSTEN_NETWORK_NAME,
            rpcServerUrl = "https://ropsten.infura.io/v3/5c74f1278e2a4c87ab5b46e3aa8cb30b",
            chainId = 3,
            symbol = Constants.ETH_SYMBOL,
            scanUrl = "https://ropsten.etherscan.io",
            backendUrl = "https://api-ropsten.etherscan.io",
            apiKey = BuildConfig.Etherscan_API
        )
    )

    private var defaultNetwork = getByName(preferencesRepository.getDefaultNetwork()) ?: NETWORKS[0]

//    private var web3j = Web3j.build(HttpService(getDefaultNetwork().rpcServerUrl))

    fun getNetworkList() = NETWORKS

    fun setDefaultNetworkInfo(pos: Int) {
        defaultNetwork = NETWORKS[pos]
        preferencesRepository.setDefaultNetwork(defaultNetwork.name)
    }

    fun getDefaultNetwork(): NetworkInfo {
        return defaultNetwork
    }

    private fun getByName(name: String?): NetworkInfo? {
        if (!TextUtils.isEmpty(name))
            for (NETWORK in NETWORKS)
                if (name == NETWORK.name)
                    return NETWORK
        return null
    }

    fun getWalletBalance(address: String): Observable<BigInteger> {
        return Observable.fromCallable {
            val web3j = Web3j.build(HttpService(getDefaultNetwork().rpcServerUrl))
            web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().balance
        }
    }


    fun getListToken(credentials: Credentials): Observable<List<Map<String, String>>> {
        val lstAddress = preferencesRepository.getListTokenAddress(defaultNetwork.symbol)
        val web3j = Web3j.build(HttpService(getDefaultNetwork().rpcServerUrl))

        val transactionReceiptProcessor: TransactionReceiptProcessor = NoOpProcessor(web3j)

        val transactionManager: TransactionManager = RawTransactionManager(
            web3j,
            credentials,
            defaultNetwork.chainId.toLong(),
            transactionReceiptProcessor
        )

        if (lstAddress == null)
            Observable.just(mutableListOf<Map<String, String>>())
        val lstObservable = mutableListOf<Observable<Map<String, String>>>()
        lstAddress?.forEach {
            val contract = Erc20TokenWrapper.load(
                it, web3j,
                transactionManager, BigInteger.ZERO, BigInteger.ZERO
            )
            lstObservable.add(generateContractObservable(contract, credentials.address, it))
        }

        return Observable.zip(lstObservable) {
            it.toMutableList() as MutableList<Map<String, String>>
        }
    }


    fun getInforToken(
        credentials: Credentials,
        contractAddress: String
    ): Observable<Map<String, String>> {
        val web3j = Web3j.build(HttpService(getDefaultNetwork().rpcServerUrl))

        val transactionReceiptProcessor: TransactionReceiptProcessor = NoOpProcessor(web3j)

        val transactionManager: TransactionManager = RawTransactionManager(
            web3j,
            credentials,
            defaultNetwork.chainId.toLong(),
            transactionReceiptProcessor
        )

        return Observable.fromCallable {
            val contract = Erc20TokenWrapper.load(
                contractAddress, web3j,
                transactionManager, BigInteger.ZERO, BigInteger.ZERO
            )
            mapOf(
                "symbol" to contract.symbol().value,
                "decimals" to contract.decimals().value.toString(),
                "contractAddress" to contract.contractAddress
            )
        }
    }


    private fun generateContractObservable(
        contract: Erc20TokenWrapper,
        address: String,
        contractAddress: String
    ): Observable<Map<String, String>> {
        return Observable.fromCallable {
            mapOf(
                "name" to contract.name().value,
                "symbol" to contract.symbol().value,
                "address" to contractAddress,
                "balance" to BalanceUtil.subunitToBase(contract.balanceOf(Address(address)).value)
                    .toString()
            )
        }
    }

    fun getEstimateGas(
        fromAddress: String,
        toAddress: String, // or contractAddress
        amount: String
    ): Observable<BigDecimal> {
        val web3j = Web3j.build(HttpService(getDefaultNetwork().rpcServerUrl))

        return Observable.fromCallable {
            val amount2: BigInteger = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger()
            val function =
                Function(
                    "transfer",
                    listOf(Address(toAddress), Uint256(amount2)),
                    emptyList()
                )
            val txData: String = FunctionEncoder.encode(function)

            val transaction = Transaction.createEthCallTransaction(fromAddress, toAddress, txData)

            val ethGasPrice: EthGasPrice = web3j.ethGasPrice().sendAsync().get()
            val ethEstimateGas: EthEstimateGas = web3j.ethEstimateGas(transaction).sendAsync().get()

            Timber.d("amount: $amount2")
            Timber.d("ethGasPrice: ${ethGasPrice.gasPrice}")
            Timber.d("ethEstimateGas: ${ethEstimateGas.amountUsed}")

            val amountEstimateEth =
                convertTogEstimateGasEth(ethGasPrice.gasPrice, ethEstimateGas.amountUsed)

            Timber.d("amountEstimateEth: $amountEstimateEth")

            amountEstimateEth
        }
    }

/*    fun createTransaction(
        from: Wallet, toAddress: String, subUnitAmount: BigInteger,
        gasPrice: BigInteger, gasLimit: Long, password: String
    ): Single<String> {

        return Single.fromCallable<Long> {
            web3j.ethGetTransactionCount(from.address, DefaultBlockParameterName.LATEST)
                .send().transactionCount.toLong()
        }.flatMap<ByteArray> {
            accountManager.signTransaction(
                from, password, toAddress, subUnitAmount, gasPrice, gasLimit,
                it, getDefaultNetwork().chainId.toLong()
            )
        }.flatMap {
            Single.fromCallable {
                val raw = web3j.ethSendRawTransaction(Numeric.toHexString(it)).send()

                if (raw.hasError())
                    throw Exception(raw.error.message)

                raw.transactionHash
            }
        }.subscribeOn(Schedulers.io())
    }*/

}