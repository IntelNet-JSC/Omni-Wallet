package com.example.omniwalletapp.base

import androidx.lifecycle.*
import com.example.omniwalletapp.util.Data
import com.example.omniwalletapp.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.web3j.protocol.core.methods.response.Transaction
import javax.inject.Inject

typealias EventTransaction = Event<Data<Transaction>>

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    // loading liveData
    protected val _fetchLiveData = MutableLiveData<Data<Void>>()
    val fetchLiveData: LiveData<Data<Void>> = _fetchLiveData

    // loading liveData
    protected val _listenLiveData = MutableLiveData<EventTransaction>()
    val listenLiveData: LiveData<EventTransaction> = _listenLiveData

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private fun clearDisposables() {
        compositeDisposable.clear()
    }

    override fun onCleared() {
        clearDisposables()
    }

    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

}