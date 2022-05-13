package com.proto.dynamiclayout.builder

import android.view.View
import com.proto.dynamiclayout.R
import com.proto.dynamiclayout.databinding.HeaderItemBinding
import com.xwray.groupie.viewbinding.BindableItem

class DetailItem(val headerData: String) : BindableItem<HeaderItemBinding>() {
    override fun bind(viewBinding: HeaderItemBinding, position: Int) {
        viewBinding.tvTitle.text = headerData
    }

    override fun getLayout(): Int = R.layout.header_item
    override fun initializeViewBinding(view: View) = HeaderItemBinding.bind(view)
    override fun getSpanSize(spanCount: Int, position: Int) = 2
}