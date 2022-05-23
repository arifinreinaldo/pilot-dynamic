package com.proto.dynamiclayout

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DynamicViewModel : ViewModel() {
    val action = mutableStateOf(Navigation.INIT)
    private val add = Component("Add", "Add", "BUTTON")
    private val remove = Component("Remove", "Remove", "BUTTON")
    private val reset = Component("Next Fragment", "Next Fragment", "BUTTON")
    private val back = Component("Back", "Back", "BUTTON")

    val formData = mutableListOf<List<Component>>().toMutableStateList()
    val listData = mutableListOf<Component>().toMutableStateList()
    val listDataNew = mutableListOf<List<Bloc>>().toMutableStateList()

    private val textChangeValue = MutableSharedFlow<ACTION.TEXTCHANGE>()

    init {
        viewModelScope.launch {
            textChangeValue.debounce(200).collectLatest {
                Log.d("Last ", it.valueData)
            }
        }
    }

    val row = mutableListOf<List<Component>>()
    fun getLayoutConfig(screenName: String) {
        listData.clear()
        when (screenName) {
            "Main" -> {
                listData.add(add)
                listData.add(remove)
                listData.add(reset)
            }
            else -> {
                listData.add(reset)
                listData.add(add)
                listData.add(remove)
                listData.add(back)
            }
        }
        formData.clear()
        formData.add(listOf(Component("Hehe", "HOHO", "LABEL"), Component("Hehe", "HOHO", "LABEL")))
        listDataNew.clear()
        val dataNew = mutableListOf<Bloc>()
        dataNew.add(Bloc("Itemno", 30.toFloat(), 1, 1, "LABEL", "STRING"))
        dataNew.add(Bloc("ItemName", 70.toFloat(), 2, 1, "LABEL", "STRING"))
        dataNew.add(Bloc("ItemImg", 30.toFloat(), 0, 2, "IMAGEWITHPREVIEW", "STRING"))
        dataNew.add(Bloc("StockAvailable", 8.toFloat(), 1, 2, "OPTION", "STRING"))
        dataNew.add(Bloc("StockAvailableLbl", 30.toFloat(), 2, 2, "LABEL", "STRING"))
        dataNew.add(Bloc("SACS", 8.toFloat(), 3, 2, "TEXTBOX", "NUMBER", "UNIQUE1"))
        dataNew.add(Bloc("SAPC", 8.toFloat(), 4, 2, "TEXTBOX", "NUMBER", "UNIQUE2"))
        dataNew.add(Bloc("WHCS", 8.toFloat(), 5, 2, "TEXTBOX", "NUMBER", "UNIQUE3"))
        dataNew.add(Bloc("WHPC", 8.toFloat(), 6, 2, "TEXTBOX", "NUMBER", "UNIQUE4"))
        dataNew.add(Bloc("NearOutOfStock", 10.toFloat(), 1, 3, "OPTION", "STRING"))
        dataNew.add(Bloc("NearOutOfStockLbl", 30.toFloat(), 2, 3, "LABEL", "STRING"))
        dataNew.add(Bloc("OutOfStock", 10.toFloat(), 1, 4, "OPTION", "STRING"))
        dataNew.add(Bloc("OutOfStockLbl", 20.toFloat(), 2, 4, "LABEL", "STRING"))
        dataNew.add(Bloc("Reason", 30.toFloat(), 3, 4, "COMBOBOX", "STRING", "123"))
        dataNew.add(Bloc("TakePhoto", 16.toFloat(), 4, 4, "TAKEPHOTOWITHPREVIEW", "STRING"))
        dataNew.add(Bloc("LeadBasePack", 0.toFloat(), 11, 5, "LABEL", "STRING"))
        dataNew.add(Bloc("StockDate", 0.toFloat(), 12, 5, "LABEL", "STRING"))
//        for (i in 1..10) {
        listDataNew.add(dataNew)
//        }
    }

    var count: Int = 0
    fun doSomething(value: ACTION) {
        when (value) {
            is ACTION.EXECUTE -> {

            }
            is ACTION.TEXTCHANGE -> {
                listDataNew.flatMap { it }.find { it.id == value.id }?.apply {
                    this.value.value = value.valueData
                    viewModelScope.launch {
                        textChangeValue.emit(value)
                    }
                }
            }
        }
    }
}

data class Component(val FieldName: String, var Default: String, val FieldControl: String)

data class Bloc(
    val FieldName: String,
    val ColumnWidth: Float,
    var DisplayNo: Int,
    var LineIndex: Int,
    val FieldControl: String,
    val DataMemberType: String,
    val id: String = "",
    var value: MutableState<String> = mutableStateOf(""),
)


sealed class ACTION {
    data class EXECUTE(val actionString: String) : ACTION()
    data class TEXTCHANGE(val valueData: String, val actionString: String, val id: String) :
        ACTION()

}