package com.example.amprime.firebaseauth.helper

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Window
import com.example.amprime.firebaseauth.R

class CustomDialog {

    companion object {
        private var dialog: Dialog? = null
        fun showDialog(activity: Activity) {
            dialog = Dialog(activity)
            dialog?.apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setCancelable(false)
                setContentView(R.layout.layout_custom_dialog)
                show()
            }

        }

        fun hideDialog() {
            dialog?.dismiss()
        }
    }
}


