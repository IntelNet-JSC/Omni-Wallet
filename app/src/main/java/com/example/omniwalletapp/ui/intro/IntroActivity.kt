package com.example.omniwalletapp.ui.intro

import android.content.Intent
import android.os.Bundle
import com.example.omniwalletapp.base.BaseActivity
import com.example.omniwalletapp.databinding.ActivityIntroBinding
import com.example.omniwalletapp.ui.addWallet.AddWalletActivity
import com.example.omniwalletapp.ui.home.HomeActivity
import com.example.omniwalletapp.util.BalanceUtil
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Single
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthEstimateGas
import org.web3j.protocol.core.methods.response.EthGasPrice
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert
import timber.log.Timber
import java.io.File
import java.math.BigDecimal
import java.math.BigInteger


@AndroidEntryPoint
class IntroActivity : BaseActivity() {

    lateinit var binding: ActivityIntroBinding

    private val keydir: File by lazy {
        File(filesDir, "keystore")
    }

    lateinit var web3j: Web3j

    val toAddress = "0x39fB0Ea8aAdc23683f2d237801e912f55536F5cF"
    val fromAddress = "0x60Aef71878c7f0973DEb83C1b19Ca033DD271483"

    val contractAddress = "0xaE18F6c514A500a30EaFf19F1d1B7B320986eB72"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        web3j = Web3j.build(HttpService("https://data-seed-prebsc-1-s1.binance.org:8545/"))
//        web3j = Web3j.build(HttpService("https://ropsten.infura.io/v3/5c74f1278e2a4c87ab5b46e3aa8cb30b"))

//        getWalletBalance()

/*        getBalanceOf()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    Timber.d("Response: $response")
                    val balance = BalanceUtil.subunitToBase(response)
                    Timber.d("Balance: $balance")
                }, {
                    Timber.d("ERROR: ${it.message}")
                }
            )*/

//        getEstimateGas()
/*            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    Timber.d("Response1: $response")
                    val balance = BalanceUtil.gweiToWei(BigDecimal(response))
                    val ooo = BalanceUtil.weiToEth(balance)
                    Timber.d("Balance WEI: $balance")
                    Timber.d("Balance ETH: $ooo")
                }, {
                    Timber.d("ERROR1: ${it.message}")
                }
            )*/

        binding.btnStart.setOnClickListener {
            if (keydir.exists())
                startActivity(Intent(this, HomeActivity::class.java))
            else
                startActivity(Intent(this, AddWalletActivity::class.java))
            finish()

        }

//        var transactionHash: String? = null
//        val nonce: BigInteger = getNonce(walletAddress)
//        val weiValue: BigDecimal = Convert.toWei(etherAmount, Convert.Unit.ETHER)
//
//        val rawTransaction = RawTransaction.createEtherTransaction(
//            nonce, gasPrice, gasLimit, to_Address, weiValue.toBigIntegerExact()
//        )
//        val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
//
//
//        val hexValue = Numeric.toHexString(signedMessage)
//
//        val ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get()
//
//        transactionHash = ethSendTransaction.transactionHash


    }

    private fun getEstimateGas(){
        val amount: BigInteger = Convert.toWei("0.0005", Convert.Unit.ETHER).toBigInteger()

        val function =
            Function("transfer", listOf(Address(toAddress), Uint256(amount)), emptyList())
        val txData: String = FunctionEncoder.encode(function)

        val transaction = Transaction.createEthCallTransaction(fromAddress, toAddress, txData)

        val ethGasPrice: EthGasPrice = web3j.ethGasPrice().sendAsync().get()
        val ethEstimateGas: EthEstimateGas = web3j.ethEstimateGas(transaction).sendAsync().get()

        Timber.d("amount: $amount")
        Timber.d("ethGasPrice: ${ethGasPrice.gasPrice}")
        Timber.d("ethEstimateGas: ${ethEstimateGas.amountUsed}")

        val xxx = BalanceUtil.weiToEth(ethGasPrice.gasPrice)
        val yyy = xxx.multiply(BigDecimal(ethEstimateGas.amountUsed))

        Timber.d("XXXX: $xxx")

        Timber.d("YYYY: $yyy")


/*        val ethGetTransactionCount = web3j.ethGetTransactionCount(
            fromAddress, DefaultBlockParameterName.LATEST
        ).sendAsync().get()


        val nonce = ethGetTransactionCount.transactionCount*/

/*            val transaction1: Transaction = Transaction.createContractTransaction(
                fromAddress,
                nonce,
                ethGasPrice.gasPrice,
                DefaultGasProvider.GAS_LIMIT,
                BigInteger.valueOf(21000),
                amount,
                txData)*/
//        val xx = web3j.ethEstimateGas(transaction).send().amountUsed

    }

    fun getBalanceOf(): Single<BigInteger> {
        return Single.fromCallable {
            web3j.ethGetBalance(fromAddress, DefaultBlockParameterName.LATEST).send().balance
        }
    }

}