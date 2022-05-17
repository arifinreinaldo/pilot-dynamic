package com.proto.dynamiclayout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.proto.dynamiclayout.builder.BlocWrapper
import com.proto.dynamiclayout.builder.TitleBloc
import com.proto.dynamiclayout.database.LocalDatabase
import com.proto.dynamiclayout.database.entity.CartEntity
import com.xwray.groupie.Group
import com.xwray.groupie.Section
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DefaultViewModel @Inject constructor(val local: LocalDatabase) : ViewModel() {
    private val blocData = BlocWrapper(mutableMapOf())
    private val _headerObserver = MutableStateFlow(blocData)
    val headerObserver = _headerObserver.asStateFlow()
    private val _textValue = MutableSharedFlow<Pair<Long, String>>()

    init {
        viewModelScope.launch {
            _textValue.debounce(250).collectLatest {
                Log.d("Typing", "${it.second}")
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            local.cartDao()
                .replace(CartEntity(Random.nextInt(0, 100).toString(), "123", "123", false))
            local.cartDao().getSongs(SimpleSQLiteQuery("Select * from CartEntity")).apply {
                val column = this.columnNames
                val data = mutableListOf<Pair<String, String>>()
                while (this.moveToNext()) {
                    column.forEachIndexed { index, s ->
                        data.add(Pair(s, this.getString(index)))
                    }
                }
                Log.d("TAG", "Hasil: ")
            }
        }
    }

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
                title.notifyChanged()
            }
        }
    }

    fun addSection(section: Section) {
        blocData.addData("1", section)
        viewModelScope.launch {
            _headerObserver.emit(blocData)
        }
    }

    fun inputText(value: String, pos: Long) {
        viewModelScope.launch {
            _textValue.emit(Pair(pos, value))
        }
    }
}