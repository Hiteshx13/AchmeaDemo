package com.achmea.demo.presentation

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.achmea.demo.R
import com.achmea.demo.databinding.DialogFilterBinding


fun showFilterDialog(
    context: Context,
    isCancelable: Boolean,
    listener: DialogOnClick
) {

    val inflater = LayoutInflater.from(context)
    val binding: DialogFilterBinding =
        DataBindingUtil.inflate(inflater, R.layout.dialog_filter, null, false)

    val mDialog: Dialog = Dialog(context).apply {
        setContentView(binding.root)
        setCanceledOnTouchOutside(isCancelable)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    binding.btnApply.setOnClickListener {
        val filter = binding.etFilter.text.toString()
        val maxRow = binding.etMaxRow.text.toString().toIntOrNull()
        listener.onApply(filter, maxRow)
        mDialog.dismiss()
    }
    mDialog.show()
}

interface DialogOnClick {
    fun onApply(filter: String, maxRow: Int?)
}
