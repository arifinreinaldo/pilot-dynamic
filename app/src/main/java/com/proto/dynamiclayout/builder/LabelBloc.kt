package com.proto.dynamiclayout.builder

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import com.proto.dynamiclayout.R
import com.proto.dynamiclayout.databinding.HeaderItemBinding
import com.proto.dynamiclayout.databinding.LabelBlocBinding
import com.proto.dynamiclayout.databinding.TitleBlocBinding
import com.xwray.groupie.viewbinding.BindableItem

class LabelBloc(val headerData: String, val width: Int, val action: () -> Unit) :
    BindableItem<LabelBlocBinding>() {
    override fun bind(viewBinding: LabelBlocBinding, position: Int) {
        viewBinding.label.text = headerData
    }

    override fun getLayout() = R.layout.label_bloc

    override fun initializeViewBinding(view: View) = LabelBlocBinding.bind(view)
    override fun getSpanSize(spanCount: Int, position: Int) = width
}