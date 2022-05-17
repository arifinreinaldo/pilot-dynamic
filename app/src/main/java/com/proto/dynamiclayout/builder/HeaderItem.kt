package com.proto.dynamiclayout.builder

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import com.proto.dynamiclayout.R
import com.proto.dynamiclayout.databinding.HeaderItemBinding
import com.xwray.groupie.viewbinding.BindableItem

class HeaderItem(
    val headerData: String,
    private val idx: Long,
    val span: Int,
    val action: () -> Unit
) :
    BindableItem<HeaderItemBinding>(idx) {
    var value = false

    companion object {
        const val FAVORITE = "FAVORITE"
    }

//    override fun bind(binding: HeaderItemBinding, position: Int, payloads: List<Any>) {
//        if (payloads.contains(FAVORITE)) {
//            bindHeart(binding)
//        } else {
//            bind(binding, position)
//        }
//    }
//
//    private fun bindHeart(binding: HeaderItemBinding) {
//        binding.check.isChecked = value
//    }

    override fun getLayout(): Int = R.layout.header_item
    override fun initializeViewBinding(view: View) = HeaderItemBinding.bind(view)
    override fun getSpanSize(spanCount: Int, position: Int) = span
    override fun bind(viewBinding: HeaderItemBinding, position: Int) {
        viewBinding.tvTitle.text = if (value) "checked" else headerData
        viewBinding.check.isChecked = value
        viewBinding.check.setOnClickListener {
            viewBinding.tvTitle.text = "Checked"
            value = viewBinding.check.isChecked
        }
        viewBinding.tvTitle.setOnClickListener {
            action()
        }
    }

    override fun getSwipeDirs(): Int = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
}