package com.demo.app.model


import com.google.gson.annotations.SerializedName

data class SelectionObject(
    @SerializedName("child")
    var child: MutableList<DataModel.Specification.DataList?>,
    @SerializedName("parent")
    var parent: DataModel.Specification.DataList?
)