package com.example.omniwalletapp.repository

import android.content.Context
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

    fun createWallet(pass: String): Observable<Pair<String, String>> {
        return Observable.fromCallable {
            val bip39Wallet = Bip44WalletUtils.generateBip44Wallet(pass, keyStoreFile)
            Timber.d("bip39Wallet: $bip39Wallet")
            val credentials = Bip44WalletUtils.loadBip44Credentials("", bip39Wallet.mnemonic)
            Timber.d("address: ${credentials.address}")
            Timber.d("privateKey: ${credentials.ecKeyPair.privateKey.toString(16)}")

            passwordRepository.setPassword2(credentials.address, pass)

            Pair(
                credentials.address,
                bip39Wallet.mnemonic
            )
        }
    }

    fun importPrivateKey(prvKey: String, pass: String): Observable<String> {
        return Observable.fromCallable {
            val keys = ECKeyPair.create(Hex.decode(prvKey))
            val nameFile = WalletUtils.generateWalletFile(pass, keys, keyStoreFile, false)
            Timber.d("nameFile: $nameFile")
            val credentials = WalletUtils.loadCredentials(pass, File(keyStoreFile, nameFile))
            Timber.d("address: ${credentials.address}")

            credentials.address
        }.flatMap {
            passwordRepository.setPassword(it, pass)
        }
    }

    fun importMnemonic(mnemonic: String, pass: String): Observable<String> {
        return Observable.fromCallable {
            val credentials = Bip44WalletUtils.loadBip44Credentials("", mnemonic)
            val nameFile =
                WalletUtils.generateWalletFile(pass, credentials.ecKeyPair, keyStoreFile, false)
            Timber.d("address: ${credentials.address}")
            Timber.d("nameFile: $nameFile")

            credentials.address
        }.flatMap {
            passwordRepository.setPassword(it, pass)
        }
    }

}