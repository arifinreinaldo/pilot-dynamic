package com.proto.dynamiclayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.proto.dynamiclayout.theme.JetpackComposeTheme
import kotlin.random.Random

class ComposeFragment(private val count: Int) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                JetpackComposeTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        Column {
                            Button(onClick = { next() }) {
                                Text("Heheh")
                            }
                            for (i in 1..count) {
                                Text("Hello Compose!")
                            }

                            val itemsIndexedList = (0..count).toList()
                            LazyColumn {
                                itemsIndexed(itemsIndexedList) { index, item ->
                                    Text("Item at index $index is $item")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun next() {
        (requireActivity() as MainActivity).nextScreen(Random.nextInt(1, 10))
    }
}
