package com.intelnet.mylibrary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.intelnet.mylibrary.ui.intro.IntroActivity

open class OmniWallet {

    companion object {

        internal const val EXTRA_DISABLE_OPEN_NETWORK = "extra.disable_open_network"
        internal const val EXTRA_DISABLE_ADD_TOKEN = "extra.disable_add_token"
        internal const val EXTRA_BSC_TESTNET_IS_DEFAULT = "extra.bsc_testnet_is_default"

        /**
         * Use this to use ImagePicker in Activity Class
         *
         * @param activity Activity Instance
         */
        @JvmStatic
        fun with(activity: Activity): Builder {
            return Builder(activity)
        }

        /**
         * Use this to use ImagePicker in Fragment Class
         *
         * @param fragment Fragment Instance
         */
        @JvmStatic
        fun with(fragment: Fragment): Builder {
            return Builder(fragment)
        }


        class Builder(private val activity: Activity) {

            private var fragment: Fragment? = null

            private var disableOpenNetwork: Boolean = false
            private var disableAddToken: Boolean = false
            private var bscTestnetIsDefault: Boolean = true

            constructor(fragment: Fragment) : this(fragment.requireActivity()) {
                this.fragment = fragment
            }

            /**
             * Disable open network.
             */
            fun disableOpenNetwork(): Builder {
                this.disableOpenNetwork = true
                return this
            }


            /**
             * Disable add token.
             */
            fun disableAddToken(): Builder {
                this.disableAddToken = true
                return this
            }


            /**
             * set bsc testnet is default.
             */
            fun bscTestnetIsDefault(): Builder {
                this.bscTestnetIsDefault = true
                return this
            }

            /**
             * set eth testnet is default.
             */
            fun ethTestnetIsDefault(): Builder {
                this.bscTestnetIsDefault = false
                return this
            }

            /**
             * Start Image Picker Activity
             */
            fun start() {
                startActivity()
            }


            /**
             * Get Bundle for IntroActivity
             */
            private fun getBundle(): Bundle {
                return Bundle().apply {
                    putBoolean(EXTRA_DISABLE_OPEN_NETWORK, disableOpenNetwork)
                    putBoolean(EXTRA_DISABLE_ADD_TOKEN, disableAddToken)
                    putBoolean(EXTRA_BSC_TESTNET_IS_DEFAULT, bscTestnetIsDefault)
                }
            }

            /**
             * Start ImagePickerActivity with given Argument
             */
            private fun startActivity() {
                val intent = Intent(activity, IntroActivity::class.java)
                intent.putExtras(getBundle())
                if (fragment != null) {
                    fragment?.startActivity(intent)
                } else {
                    activity.startActivity(intent)
                }
            }
        }
    }
}