package com.example.omniwalletapp.repository

import android.content.Context
import com.example.omniwalletapp.entity.Wallet
import io.reactivex.Observable
import org.bouncycastle.util.encoders.Hex
import org.web3j.crypto.Bip44WalletUtils
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.WalletUtils
import timber.log.Timber
import java.io.File
import javax.inject.Inject


class AddWalletRepository @Inject constructor(
    context: Context,
    private val keyStoreFile: File
) {

    private val passwordRepository = PasswordRepository(context)

    fun createWallet(pass: String): Observable<Pair<Wallet, String>> {
        return Observable.fromCallable {
            val bip39Wallet = Bip44WalletUtils.generateBip44Wallet(pass, keyStoreFile)
            Timber.d("bip39Wallet: $bip39Wallet")
            val credentials = Bip44WalletUtils.loadBip44Credentials("", bip39Wallet.mnemonic)
            Timber.d("address: ${credentials.address}")
            Timber.d("privateKey: ${credentials.ecKeyPair.privateKey.toString(16)}")

            Pair(
                Wallet(address = credentials.address),
                bip39Wallet.mnemonic
            )
        }
    }

    fun importPrivateKey(prvKey: String, pass: String): Observable<Wallet> {
        return Observable.fromCallable {
            val keys = ECKeyPair.create(Hex.decode(prvKey))
            val nameFile = WalletUtils.generateWalletFile(pass, keys, keyStoreFile, false)
            Timber.d("nameFile: $nameFile")
            val credentials = WalletUtils.loadCredentials(pass, File(keyStoreFile, nameFile))
            Timber.d("address: ${credentials.address}")

            Wallet(address = credentials.address)
        }
    }

    fun importMnemonic(mnemonic: String, pass: String): Observable<Wallet> {
        return Observable.fromCallable {
            val credentials = Bip44WalletUtils.loadBip44Credentials("", mnemonic)
            val nameFile =
                WalletUtils.generateWalletFile(pass, credentials.ecKeyPair, keyStoreFile, false)
            Timber.d("address: ${credentials.address}")
            Timber.d("nameFile: $nameFile")

            Wallet(address = credentials.address)
        }
    }

}