package com.example.omniwalletapp.ui.home.receive

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.omniwalletapp.R
import com.example.omniwalletapp.base.BaseBottomSheetFragment
import com.example.omniwalletapp.databinding.FragmentReceiveTokenBinding
import com.example.omniwalletapp.util.dpToPx
import com.example.omniwalletapp.util.formatAddressWallet
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReceiveTokenDialogFragment : BaseBottomSheetFragment<FragmentReceiveTokenBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentReceiveTokenBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val strAddressWallet = getString(R.string.address_demo)
        binding.txtAddress.text = strAddressWallet.formatAddressWallet()

        generateQRCode(strAddressWallet)?.run {
            binding.imgQR.setImageBitmap(this)
        }

        binding.imgShareAddress.setOnClickListener {

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, strAddressWallet)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.txtCopy.setOnClickListener {
            copyToClipboard(strAddressWallet)
            showToast(getString(R.string.toast_address_copied))
        }
    }

    private fun generateQRCode(text: String): Bitmap? {
        val writer = MultiFormatWriter()
        return try {
            val matrix = writer.encode(text, BarcodeFormat.QR_CODE, 350, 350)
            val encoder = BarcodeEncoder()
            val bitmap = encoder.createBitmap(matrix)
            bitmap
        } catch (e: WriterException) {
            null
        }
    }
}