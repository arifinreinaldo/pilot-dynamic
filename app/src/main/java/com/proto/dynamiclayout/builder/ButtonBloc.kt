package com.proto.dynamiclayout.builder

import android.view.View
import com.proto.dynamiclayout.R
import com.proto.dynamiclayout.databinding.ButtonBlocBinding
import com.xwray.groupie.viewbinding.BindableItem

class ButtonBloc(val headerData: String, val width: Int, val action: () -> Unit) :
    BindableItem<ButtonBlocBinding>() {
    override fun bind(viewBinding: ButtonBlocBinding, position: Int) {
        viewBinding.button.text = headerData
        viewBinding.button.setOnClickListener {
            action()
        }
    }

    override fun getLayout() = R.layout.button_bloc

    override fun initializeViewBinding(view: View) = ButtonBlocBinding.bind(view)
    override fun getSpanSize(spanCount: Int, position: Int) = width
}