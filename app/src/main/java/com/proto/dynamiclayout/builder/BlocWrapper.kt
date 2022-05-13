package com.proto.dynamiclayout.builder

import com.xwray.groupie.Group

data class BlocWrapper(val headerData: MutableMap<String, Group>) {
    fun addData(tag: String, group: Group) {
        headerData[tag] = group
    }

    fun getData(tag: String) = headerData[tag]
    fun getAll() = headerData.values
    fun getSize() = headerData.size

    override fun hashCode(): Int {
        return headerData.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return false
    }
}