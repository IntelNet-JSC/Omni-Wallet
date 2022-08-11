package com.example.omniwalletapp.util

data class Data<RequestData>(var responseType: Status, var data: RequestData? = null, var error: Error? = null)

enum class Status { SUCCESSFUL, ERROR, LOADING }