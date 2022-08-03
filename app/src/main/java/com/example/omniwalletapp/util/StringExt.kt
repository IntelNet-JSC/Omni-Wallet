package com.example.omniwalletapp.util

fun String.isValidEmail() =
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.formatAddressWallet(start:Int=6) = if(isNotEmpty()) this.replace(this.substring(start, 38), "...") else ""

fun String.isValidateAddress() = this.contains("0x")

fun String.getStringAddressFromScan():String{
    if(!this.isValidateAddress())
        return ""
    val startPos = this.indexOf("0x")
    val endPos = this.indexOf("@")
    return if(endPos!=-1){
        this.substring(startPos, endPos)
    }else
        this.substring(startPos)
}