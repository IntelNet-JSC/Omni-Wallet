package com.intelnet.mylibrary.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.intelnet.mylibrary.base.BaseViewModel
import com.intelnet.mylibrary.repository.WalletRepository
import com.intelnet.mylibrary.util.Data
import com.intelnet.mylibrary.util.Event
import com.intelnet.mylibrary.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

typealias EventLogin = Event<Data<String>>

@HiltViewModel
class LoginLaterViewModel @Inject constructor(
    private val walletRepository: WalletRepository
) : BaseViewModel() {

    private val _loginLiveData: MutableLiveData<EventLogin> =
        MutableLiveData()
    val loginLiveData: LiveData<EventLogin> = _loginLiveData


    fun loadCredentials(pass: String) {
        val disposable = walletRepository.loadCredentials(pass)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _loginLiveData.value = Event(Data(responseType = Status.LOADING))
            }
            .doOnComplete {
                Timber.d("Close waitting dialog")
            }
            .subscribe(
                { response ->
                    Timber.d("On Next Called: $response")
                    _loginLiveData.value =
                        Event(
                            Data(
                                responseType = Status.SUCCESSFUL,
                                data = response.address
                            )
                        )
                }, { error ->
                    Timber.e("On Error Called, ${error.message}")
                    _loginLiveData.value =
                        Event(Data(Status.ERROR, null, error = Error(error.message)))
                }, {
                    Timber.d("On Complete Called")
                }
            )
        addDisposable(disposable)
    }
}