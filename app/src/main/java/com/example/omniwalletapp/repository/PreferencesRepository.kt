package com.example.omniwalletapp.repository

import android.content.Context
import com.example.omniwalletapp.util.Constants
import com.example.omniwalletapp.util.fromJson
import com.google.gson.Gson
import javax.inject.Inject

class PreferencesRepository @Inject constructor(context: Context, val gson: Gson) {

    private var prefs =
        context.getSharedPreferences(Constants.LOCAL_SHARED_PREF, Context.MODE_PRIVATE)

/*    fun isRememberLogin(): Boolean {
        return prefs.getBoolean("is_remember_login", false)
    }

    fun setRememberLogin(remember: Boolean) {
        prefs.edit().putBoolean("is_remember_login", remember).apply()
    }*/

    fun getAddress(): String {
        return prefs.getString("address_wallet", "") ?: ""
    }

    fun setAddress(address: String) {
        prefs.edit().putString("address_wallet", address).apply()
    }

    fun getDefaultNetwork(): String? {
        return prefs.getString("default_network_name", null)
    }

    fun setDefaultNetwork(netName: String) {
        prefs.edit().putString("default_network_name", netName).apply()
    }

    fun getListTokenAddress(type: String) : List<String>?{
        val json = prefs.getString(
            if (type == Constants.BSC_SYMBOL) "list_token_address_bnb" else "list_token_address_eth", null
        ) ?: return null
//        val type1 = object : TypeToken<List<String?>?>() {}.type
        return fromJson(gson, json)
    }

    fun setListTokenAddress(lstAddress: List<String>, type: String) {
        if (type == Constants.BSC_SYMBOL)
            prefs.edit().putString("list_token_address_bnb", gson.toJson(lstAddress)).apply()
        else
            prefs.edit().putString("list_token_address_eth", gson.toJson(lstAddress)).apply()
    }

    fun hideTokenAddress(index:Int, type:String){
        val lstToken = getListTokenAddress(type)?.toMutableList()?: return
        lstToken.removeAt(index)
        setListTokenAddress(lstToken, type)
    }

    fun checkExistTokenAddress(address: String, type: String):Boolean{
        val lstTokenAddress = getListTokenAddress(type)?.toMutableList()?: mutableListOf()
        val index = lstTokenAddress.indexOfFirst { it.equals(address, true) }
        return index!=-1
    }

    fun addTokenAddress(address: String, type: String){
        val lstTokenAddress = getListTokenAddress(type)?.toMutableList()?: mutableListOf()
        lstTokenAddress.add(address)
        setListTokenAddress(lstTokenAddress, type)
    }

    fun clearData() {
        prefs.edit().clear().apply()
    }


    fun clearDataAddressWallet() {
        prefs.edit().remove("address_wallet").apply()
    }

}