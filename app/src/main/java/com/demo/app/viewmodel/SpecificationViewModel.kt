package com.demo.app.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.app.model.DataModel
import com.demo.app.repository.SpecificationRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpecificationViewModel @Inject constructor(private val repository: SpecificationRepository) :
    ViewModel() {

    private val _dataModel = MutableLiveData<DataModel?>()
    val dataModel: LiveData<DataModel?> get() = _dataModel

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private var _selectedSpecification =
        MutableLiveData<MutableList<DataModel.Specification.DataList?>>()
    val selectedSpecification: LiveData<MutableList<DataModel.Specification.DataList?>>
        get() = _selectedSpecification

    private var _sumAndSpecification =
        MutableLiveData<Pair<Int, MutableList<DataModel.Specification.DataList?>>>()
    val sumAndSpecification: LiveData<Pair<Int, MutableList<DataModel.Specification.DataList?>>>
        get() = _sumAndSpecification

    fun loadDataFromJson(context: Context, fileName: String) {
        _loading.value = true

        viewModelScope.launch {
            try {
                val result = repository.getDataModel(context, fileName)
                _dataModel.postValue(result)
            } catch (e: Exception) {
                // Handle exception
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun addOrReplaceSpecification(items: MutableList<DataModel.Specification.DataList?>) {

        if (!items.isNullOrEmpty()) {
            val currentList = _selectedSpecification.value ?: mutableListOf()

            items.forEach { newSpec ->
                val existingIndex = currentList.indexOfFirst { it?.uniqueId == newSpec?.uniqueId }

                if (existingIndex != -1) {
                    // Replace the existing object with the new one
                    currentList[existingIndex] = newSpec
                } else {
                    // Add the new object to the list
                    currentList.add(newSpec)
                }
            }

            // Update the value of _selectedSpecification using postValue
            _selectedSpecification.postValue(currentList)
        }
    }


    fun pushEmptySpecificationList(items: MutableList<DataModel.Specification.DataList?>) {
        _selectedSpecification.value = items
    }

    fun calculateCartValue(selectedSpecsList: MutableList<DataModel.Specification.DataList?>) {
        Log.d("xoxo-->", "calculateCartValue: " + Gson().toJson(selectedSpecsList))
        var sum = 0
        selectedSpecsList.map {
            sum += (it?.price!! * it.quantitySelected)
        }

        _sumAndSpecification.postValue(Pair(sum, selectedSpecsList))

    }

}