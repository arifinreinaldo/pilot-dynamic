package com.proto.dynamiclayout.builder

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.proto.dynamiclayout.R
import com.proto.dynamiclayout.databinding.InputBlocBinding
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder


class InputBloc(
    val headerData: String,
    id: Long,
    val span: Int,
    val action: (String, Long) -> Unit
) :
    BindableItem<InputBlocBinding>(id) {
    private var value = ""

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            value = p0.toString()
            action(value, id)
        }

    }

    companion object {
        const val TEXT = "text"
    }

    override fun createViewHolder(itemView: View): GroupieViewHolder<InputBlocBinding> {
        super.createViewHolder(itemView).apply {
//            binding.label.addTextChangedListener {
//                value = it.toString()
//                Log.d("TAG", "createViewHolder: $value")
//            }
            return this
        }
    }

    override fun onViewAttachedToWindow(viewHolder: GroupieViewHolder<InputBlocBinding>) {
        super.onViewAttachedToWindow(viewHolder)
        viewHolder.binding.label.addTextChangedListener(textWatcher)
    }

    override fun onViewDetachedFromWindow(viewHolder: GroupieViewHolder<InputBlocBinding>) {
        super.onViewDetachedFromWindow(viewHolder)
        viewHolder.binding.label.removeTextChangedListener(textWatcher)
    }

    override fun bind(viewBinding: InputBlocBinding, position: Int) {
        updateBinding(viewBinding)
    }

    private fun updateBinding(viewBinding: InputBlocBinding) {
        viewBinding.label.setText(value.ifEmpty { headerData })
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
    override fun getSpanSize(spanCount: Int, position: Int) = span
}