package com.demo.app.repository

import android.content.Context
import com.demo.app.model.DataModel
import com.demo.app.utils.JsonFileReader
import javax.inject.Inject

class SpecificationRepository @Inject constructor() {
    suspend fun getDataModel(context: Context, fileName: String): DataModel? {
        return JsonFileReader.readJsonFile(context, fileName)
    }

}