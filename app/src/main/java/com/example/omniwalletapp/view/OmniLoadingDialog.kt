package com.example.omniwalletapp.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.example.omniwalletapp.R

class OmniLoadingDialog(var callbackDismiss: (() -> Unit)?) : AppCompatDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_omni_wallet, container, false)
    }

/*    override fun onStart() {
        dialog?.let {
            val params = it.window?.attributes
            // Assign window properties to fill the parent
            params?.width = WindowManager.LayoutParams.MATCH_PARENT
            params?.height = WindowManager.LayoutParams.MATCH_PARENT
            it.window?.attributes = params as WindowManager.LayoutParams
            isCancelable = true
            super.onStart()
        } ?: kotlin.run {
            return
        }
    }*/

    override fun dismiss() {
        super.dismiss()
        callbackDismiss?.invoke()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.themeShowLoadingDialog)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callbackDismiss?.invoke()
        callbackDismiss=null
    }
}