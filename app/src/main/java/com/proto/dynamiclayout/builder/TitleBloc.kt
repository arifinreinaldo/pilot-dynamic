package com.proto.dynamiclayout.builder

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import com.proto.dynamiclayout.R
import com.proto.dynamiclayout.databinding.HeaderItemBinding
import com.proto.dynamiclayout.databinding.TitleBlocBinding
import com.xwray.groupie.viewbinding.BindableItem

class TitleBloc(val headerData: String, id: Long, val action: () -> Unit) :
    BindableItem<TitleBlocBinding>(id) {
    private var value = headerData

    companion object {
        const val TEXT = "text"
    }

    override fun bind(viewBinding: TitleBlocBinding, position: Int) {
        updateBinding(viewBinding)
    }

    private fun updateBinding(viewBinding: TitleBlocBinding) {
        viewBinding.title.title = value
    }

    override fun bind(viewBinding: TitleBlocBinding, position: Int, payload: List<Any>) {
        if (payload.contains(TEXT)) {
            updateBinding(viewBinding)
        } else {
            bind(viewBinding, position)
        }
    }


    fun setData(data: String) {
        value = data
    }


    override fun getLayout() = R.layout.title_bloc

    override fun initializeViewBinding(view: View) = TitleBlocBinding.bind(view)
    override fun getSpanSize(spanCount: Int, position: Int) = spanCount
}