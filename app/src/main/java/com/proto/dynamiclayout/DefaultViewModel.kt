package com.proto.dynamiclayout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proto.dynamiclayout.builder.BlocWrapper
import com.proto.dynamiclayout.builder.TitleBloc
import com.xwray.groupie.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DefaultViewModel @Inject constructor() : ViewModel() {
    private val blocData = BlocWrapper(mutableMapOf())
    private val _headerObserver = MutableStateFlow(blocData)
    val headerObserver = _headerObserver.asStateFlow()

    fun addData(tag: String, group: Group) {
        blocData.addData(tag, group)
        viewModelScope.launch {
            _headerObserver.emit(blocData)
        }
    }

    fun editData() {
        val index = Random.nextInt(0, blocData.getSize())
        blocData.getData(index.toString())?.let { group ->
            (group as TitleBloc).let { title ->
                title.setData("New Data")
            }
            group.notifyChanged()
        }
    }
}