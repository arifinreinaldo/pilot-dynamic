package com.proto.dynamiclayout.builder

import android.view.View
import com.proto.dynamiclayout.R
import com.proto.dynamiclayout.databinding.SpaceBlocBinding
import com.xwray.groupie.viewbinding.BindableItem

class SpaceBloc(private val width: Int) :
    BindableItem<SpaceBlocBinding>() {
    override fun bind(viewBinding: SpaceBlocBinding, position: Int) {

    }

    override fun getLayout() = R.layout.space_bloc

    override fun initializeViewBinding(view: View) = SpaceBlocBinding.bind(view)
    override fun getSpanSize(spanCount: Int, position: Int) = width
}