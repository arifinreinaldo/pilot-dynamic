package com.proto.dynamiclayout

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.proto.dynamiclayout.builder.ButtonBloc
import com.proto.dynamiclayout.builder.HeaderItem
import com.proto.dynamiclayout.builder.InputBloc
import com.proto.dynamiclayout.builder.TitleBloc
import com.proto.dynamiclayout.databinding.FragmentDefaultBinding
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DefaultFragment : Fragment(R.layout.fragment_default) {
    private val binding by viewBinding(FragmentDefaultBinding::bind)
    private val headerSection = Section()
    private val formSection = Section()
    private val tableSection = Section()
    private val vm: DefaultViewModel by viewModels()
    private val adapter = GroupieAdapter().apply {
        spanCount = 10
    }
    var index = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDisplay()
        setupObserver()
        setupListener()
//        val title = TitleBloc("Help", index.toLong()) {}
//        headerSection.add(title)
//        val input = InputBloc("hehe", index.toLong()) {}
//        headerSection.add(input)
//        formSection.add(ButtonBloc("Update", 10) {
//            index++
//            title.setData("Heheh $index")
//            title.notifyChanged()
////            input.setData("Hihi $index")
////            input.notifyChanged()
//            val input = InputBloc("hehe", index.toLong()) {}
//            headerSection.add(input)
//        })


//        val section = Section()
//        section.setHeader(HeaderItem("Header") {
//            (requireActivity() as MainActivity).popBack()
//        })
//        section.add(
//            HeaderItem(
//                "Kerjain"
//            ) { Toast.makeText(requireContext(), "hoho", Toast.LENGTH_SHORT).show() }
//        )
//        for (i in 1..count) {
//            section.add(HeaderItem("Lagi", {}))
//        }
//        section.setFooter(HeaderItem("Footer") {
//            (requireActivity() as MainActivity).nextScreen(Random.nextInt(100, 200))
//        })
//        adapter.add(section)
//        adapter.add(Section().apply {
//            val updatingGroup = Section()
//
//        })
    }

    private fun setupListener() {

    }

    private fun setupDisplay() {
        val layoutManager = SpannedGridLayoutManager(
            orientation = SpannedGridLayoutManager.Orientation.VERTICAL,
            spans = 10
        )
//        val layoutManager = StaggeredGridLayoutManager(10, StaggeredGridLayoutManager.VERTICAL)
//        val layoutManager = GridLayoutManager(requireContext(), adapter.spanCount).apply {
//            spanSizeLookup = adapter.spanSizeLookup
//        }
        layoutManager.itemOrderIsStable = true

        layoutManager.spanSizeLookup = SpannedGridLayoutManager.SpanSizeLookup { position ->
            when (position % 3) {
                0 -> {
                    SpanSize(2, 2)
                }
                1 -> {
                    SpanSize(8, 1)
                }
                2 -> {
                    SpanSize(8, 1)
                }
                else -> {
                    SpanSize(0, 0)
                }
            }
        }
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = adapter
        adapter.add(headerSection)
        adapter.add(formSection)
        adapter.add(tableSection)
        formSection.add(ButtonBloc("Add ListView", 10) {
            addListView()
        })
        formSection.add(ButtonBloc("Top", 10) {
            binding.recycler.smoothScrollToPosition(13)
        })
        formSection.add(ButtonBloc("Next", 10) {
            (requireActivity() as MainActivity).nextScreen(10)
        })
//        formSection.add(ButtonBloc("Add Block", 10) {
//            addButton()
//        })
//        formSection.add(ButtonBloc("Edit Block", 10) {
//            vm.editData()
//        })
//        formSection.add(ButtonBloc("Add Text", 10) {
//            addText()
//        })
    }

    private fun addListView() {
        for (i in 1..50) {
//            val section = Section()
//            section.add(HeaderItem("Huc", index.toLong(), 4) {})
            vm.addData(index.toString(), HeaderItem("Huc", index.toLong(), 3) {})
            index++
//            section.add(InputBloc("Default Text", index.toLong(), 3) {})
            vm.addData(
                index.toString(),
                InputBloc("Default Text", index.toLong(), 3) { value, pos ->
                    vm.inputText(value, pos)
                }
            )
            index++
//            section.add(InputBloc("Default Text 2", index.toLong(), 3) {})
            vm.addData(
                index.toString(),
                InputBloc("Default Text 2", index.toLong(), 3) { value, pos ->
                    Toast.makeText(requireContext(), value, Toast.LENGTH_SHORT).show()
                })
            index++
        }
    }

    private fun addText() {
        for (i in 1..20) {
            vm.addData(
                index.toString(),
                InputBloc("Default Text", index.toLong(), 10) { value, pos -> })
            index++
        }

    }

    private fun addButton() {
        vm.addData(index.toString(), TitleBloc("hehe", index.toLong()) {})
        index++
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.headerObserver.collectLatest {
                        headerSection.update(it.getAll())
                    }
                }
            }
        }
    }

}