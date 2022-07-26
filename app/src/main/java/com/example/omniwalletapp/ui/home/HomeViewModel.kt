package com.example.omniwalletapp.ui.home

import com.example.omniwalletapp.base.BaseViewModel

//typealias EventEthAccounts = Event<Data<EthAccounts>>
//typealias EventEthBlockNumber = Event<Data<EthBlockNumber>>

class HomeViewModel: BaseViewModel(){

/*    private val _accountsLiveData: MutableLiveData<EventEthAccounts> = MutableLiveData()
    val accountsLiveData: LiveData<EventEthAccounts> = _accountsLiveData

    private val _blockNumberLiveData: MutableLiveData<EventEthBlockNumber> = MutableLiveData()
    val blockNumberLiveData: LiveData<EventEthBlockNumber> = _blockNumberLiveData

    var web3: Web3j = Web3j.build(HttpService(Constant.ETHEREUM_NOTE_URL))*/

/*    fun getAccounts(){
        val disposable = web3.ethAccounts().flowable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _accountsLiveData.value = EventEthAccounts(Data(Status.LOADING, null))
            }
            .doOnComplete {
                Timber.d("Close waitting dialog")
            }
            .subscribe({ response ->
                Timber.d("Accounts size: ${response.accounts.size}")
                val data = Data(responseType = Status.SUCCESSFUL, data = response)
                _accountsLiveData.value = EventEthAccounts(data)

            }, { error ->
                Timber.e("On Error Called")
                val data = Data<EthAccounts>(
                    responseType = Status.ERROR,
                    error = Error(error.message),
                    data = null
                )

                _accountsLiveData.value = EventEthAccounts(data)
//                handleNetworkError(error)
            }, {
                Timber.d("On Complete Called")
            })
        addDisposable(disposable)
    }*/

}