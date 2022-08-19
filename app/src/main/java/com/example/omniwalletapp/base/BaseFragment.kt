package com.example.omniwalletapp.base

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.*
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.omniwalletapp.util.Status
import com.example.omniwalletapp.view.OmniLoadingDialog
import dagger.hilt.android.internal.Contexts.getApplication
import org.web3j.protocol.core.methods.response.Transaction
import timber.log.Timber


abstract class BaseFragment<B : ViewBinding, VM : BaseViewModel> : Fragment() {

    var dialog: DialogFragment? = null

    protected abstract val viewModel: VM

    private var _binding: B? = null
    protected val binding get() = _binding!!

    var callBackTransactionConfirmed: (Transaction) -> Unit = {}

    val fManager: FragmentManager by lazy {
        requireActivity().supportFragmentManager
    }

    private val nameFragmentCurrent: String by lazy {
        this.findNavController().currentDestination?.label.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getFragmentBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initControl()
        initUI()
        initEvent()
        initConfig()

        viewModel.transactionLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                when (data.responseType) {
                    Status.ERROR -> {
                        data.error?.message?.let {
                            showToast("Error: $it")
                        }
                    }

                    Status.LOADING -> {
                        showToast("LOADING TRANSACTION...")
                    }

                    Status.SUCCESSFUL -> {
                        data.data?.let {
                            Timber.d("Transaction: $it")
                            showToast("Transaction: $it")
                        }
                    }
                }
            }
        }
    }

    protected abstract fun initControl()

    protected abstract fun initUI()

    protected abstract fun initEvent()

    protected abstract fun initConfig()

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    override fun onDestroyView() {
        dialog?.dismiss()
        dialog = null
        _binding = null
        super.onDestroyView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated: Fragment=>$nameFragmentCurrent")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate: Fragment=>$nameFragmentCurrent")
    }

    override fun onDestroy() {
        Timber.d("onDestroy: Fragment=>$nameFragmentCurrent")
        super.onDestroy()
    }

    fun navigate(navDirection: NavDirections, navOptions: NavOptions? = null) {
        navOptions?.let {
            this.findNavController().navigate(navDirection, it)
        } ?: kotlin.run {
            this.findNavController().navigate(navDirection)
        }
    }

    fun showLoadingDialog() {
        if (dialog == null) {
            dialog = OmniLoadingDialog {
                dialog = null
            }
            dialog!!.show(fManager, "DSBC_LOADING_DIALOG")
        }
    }

    fun hideDialog() {
        if (dialog != null) {
            dialog!!.dismiss()
            dialog = null
        }
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun backToPrevious(desId:Int=-1, inclusive:Boolean=false) {
        if(desId!=-1)
            this.findNavController().popBackStack(desId, inclusive)
        else
            this.findNavController().popBackStack()
    }

    fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Check if no view has focus
        val currentFocusedView = requireActivity().currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView.applicationWindowToken, 0
            )
        }
    }

    fun forceHideKeyboard(activity: Activity) {
        val imm =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val f = activity.currentFocus
        if (null != f && null != f.windowToken && EditText::class.java.isAssignableFrom(f.javaClass)) imm.hideSoftInputFromWindow(
            f.windowToken,
            0
        ) else activity.window
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    fun showKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(
            InputMethodManager.SHOW_IMPLICIT,
            0
        )
    }

    open fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication(requireContext()).getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    open fun copyToClipboard(data: String) {
        val clipboard =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("eth-address", data)
        clipboard.setPrimaryClip(clip)
    }

    open fun showAlertDialog(
        title: String,
        content: String,
        confirmButtonTitle: String,
        cancelButtonTitle: String,
        confirmCallback: () -> Unit,
        cancelCallback: () -> Unit
    ) {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(requireContext())

        val dialog = builder
            .setTitle(title)
            .setMessage(content)
            .setPositiveButton(confirmButtonTitle) { dialog, _ ->
                confirmCallback.invoke()
            }
            .setNegativeButton(cancelButtonTitle) { _, _ ->
                cancelCallback.invoke()
            }
            .show()

/*        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        positiveButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.button_in_alert_dialog_color
            )
        )
        negativeButton.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.button_in_alert_dialog_color
            )
        )*/
    }

}