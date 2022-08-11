package com.example.omniwalletapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.omniwalletapp.base.BaseViewModel
import com.example.omniwalletapp.repository.WalletRepository
import com.example.omniwalletapp.util.Data
import com.example.omniwalletapp.util.Event
import com.example.omniwalletapp.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.web3j.crypto.Credentials
import timber.log.Timber
import javax.inject.Inject

typealias EventAddress = Event<Data<String>>

@HiltViewModel
class HomeViewModel @Inject constructor(private val walletRepository: WalletRepository) :
    BaseViewModel() {

    var credentials: Credentials? = null

    private val _addressLiveData: MutableLiveData<EventAddress> = MutableLiveData()
    val addressLiveData: LiveData<EventAddress> = _addressLiveData


    fun loadCredentials2(address: String) {
        val disposable = walletRepository.loadCredentials2(address)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _addressLiveData.value = Event(Data(responseType = Status.LOADING))
            }
            .doOnComplete {
                Timber.d("Close waitting dialog")
            }
            .subscribe(
                { response ->
                    Timber.d("On Next Called: $response")
                    credentials = response
                    _addressLiveData.value =
                        Event(
                            Data(
                                responseType = Status.SUCCESSFUL,
                                data = response.address
                            )
                        )
                }, { error ->
                    Timber.e("On Error Called, ${error.message}")
                    _addressLiveData.value =
                        Event(Data(Status.ERROR, null, error = Error(error.message)))
                }, {
                    Timber.d("On Complete Called")
                }
            )
        addDisposable(disposable)
    }

}