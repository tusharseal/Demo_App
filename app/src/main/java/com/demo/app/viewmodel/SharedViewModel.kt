package com.demo.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.app.model.SelectionObject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() :
    ViewModel() {

    val selectionObjectList: MutableLiveData<MutableList<SelectionObject>> = MutableLiveData()

    init {
        selectionObjectList.value = mutableListOf() // or any initial data
    }

    fun addToSelectionObjectList(item: SelectionObject) {
        val currentList = selectionObjectList.value?.toMutableList() ?: mutableListOf()
        currentList.add(item)
        selectionObjectList.value = currentList
    }

    fun removeFromSelectionObjectList(item: SelectionObject) {
        val currentList = selectionObjectList.value?.toMutableList() ?: mutableListOf()
        currentList.remove(item)
        selectionObjectList.value = currentList
    }


}