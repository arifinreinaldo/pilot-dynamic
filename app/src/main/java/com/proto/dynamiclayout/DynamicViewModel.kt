package com.proto.dynamiclayout

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DynamicViewModel @Inject constructor(val connect: ConnectivityManager) :
    ViewModel() {
    val action = mutableStateOf(Navigation.INIT)
    val isInternetOn = mutableStateOf(false)
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

    fun registerNetwork() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connect.registerNetworkCallback(networkRequest,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    connect.getNetworkCapabilities(network)?.apply {
                        hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    }
                    setInternet(true)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    setInternet(false)
                }
            })
    }

    fun checkInternet() {
        viewModelScope.launch(Dispatchers.IO) {
            var rst = false
            connect.getNetworkCapabilities(connect.activeNetwork)?.apply {
                rst = hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

            }
            setInternet(rst)
        }
    }

    private fun setInternet(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("TAG", "setInternet: $value")
            isInternetOn.value = value
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
        dataNew.add(Bloc("Itemno", 30.toFloat(), 1, 1, "LABEL", "STRING", 50F, "Label1"))
        dataNew.add(Bloc("ItemName", 70.toFloat(), 2, 1, "LABEL", "STRING", 50F, "Label2"))
        dataNew.add(Bloc("ItemImg", 30.toFloat(), 1, 2, "IMAGEWITHPREVIEW", "STRING", 128F))
        dataNew.add(Bloc("StockAvailable", 8.toFloat(), 2, 2, "OPTION", "STRING", 50F))
        dataNew.add(Bloc("StockAvailableLbl", 30.toFloat(), 3, 2, "LABEL", "STRING", 50F))
        dataNew.add(Bloc("SACS", 8.toFloat(), 4, 2, "TEXTBOX", "NUMBER", 50F, "UNIQUE1"))
        dataNew.add(Bloc("SAPC", 8.toFloat(), 5, 2, "TEXTBOX", "NUMBER", 50F, "UNIQUE2"))
        dataNew.add(Bloc("WHCS", 8.toFloat(), 6, 2, "TEXTBOX", "NUMBER", 50F, "UNIQUE3"))
        dataNew.add(Bloc("WHPC", 8.toFloat(), 7, 2, "TEXTBOX", "NUMBER", 50F, "UNIQUE4"))
        dataNew.add(Bloc("NearOutOfStock", 10.toFloat(), 2, 3, "OPTION", "STRING", 58F))
        dataNew.add(Bloc("NearOutOfStockLbl", 30.toFloat(), 3, 3, "LABEL", "STRING", 58F))
        dataNew.add(Bloc("OutOfStock", 10.toFloat(), 1, 4, "OPTION", "STRING", 40F))
        dataNew.add(Bloc("OutOfStockLbl", 20.toFloat(), 2, 4, "LABEL", "STRING", 40F))
        dataNew.add(Bloc("Reason", 30.toFloat(), 3, 4, "COMBOBOX", "STRING", 40F, "123"))
        dataNew.add(Bloc("TakePhoto", 16.toFloat(), 4, 4, "TAKEPHOTOWITHPREVIEW", "STRING", 40F))
        dataNew.add(Bloc("LeadBasePack", 0.toFloat(), 11, 5, "LABEL", "STRING", 0F))
        dataNew.add(Bloc("StockDate", 0.toFloat(), 12, 5, "LABEL", "STRING", 0F))
        for (i in 1..149) {
            listDataNew.add(dataNew)
        }
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
    val RowHeight: Float,
    val id: String = "",
    var value: MutableState<String> = mutableStateOf(""),
) {
    fun uniqueID() = "$DisplayNo,$LineIndex"
}


sealed class ACTION {
    data class EXECUTE(val actionString: String) : ACTION()
    data class TEXTCHANGE(val valueData: String, val actionString: String, val id: String) :
        ACTION()

}