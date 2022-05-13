package com.proto.dynamiclayout.builder

import android.view.View
import androidx.core.widget.addTextChangedListener
import com.proto.dynamiclayout.R
import com.proto.dynamiclayout.databinding.InputBlocBinding
import com.xwray.groupie.viewbinding.BindableItem


class InputBloc(headerData: String, id: Long, val action: () -> Unit) :
    BindableItem<InputBlocBinding>(id) {
    private var value = headerData

    companion object {
        const val TEXT = "text"
    }

    override fun bind(viewBinding: InputBlocBinding, position: Int) {
        updateBinding(viewBinding)
        viewBinding.label.addTextChangedListener {
            value = it.toString()
        }
    }

    fun updateBinding(viewBinding: InputBlocBinding) {
        viewBinding.label.setText(value)
    }

    override fun bind(viewBinding: InputBlocBinding, position: Int, payload: List<Any>) {
        if (payload.contains(TEXT)) {
            updateBinding(viewBinding)
        } else {
            bind(viewBinding, position)
        }
    }

    fun setData(data: String) {
        value = data
    }

    override fun getLayout() = R.layout.input_bloc

    override fun initializeViewBinding(view: View) = InputBlocBinding.bind(view)
    override fun getSpanSize(spanCount: Int, position: Int) = spanCount
}