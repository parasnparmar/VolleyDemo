package com.example.volleydemo

import com.google.gson.annotations.SerializedName

data class UserResponse(
    var page: Int,
    @SerializedName("per_page")
    var perPage: Int,
    var total: Int,
    @SerializedName("total_pages")
    var totalPages: Int,
    @SerializedName("data")
    var user : ArrayList<User>
){
    override fun toString(): String {
        return "page = $page   perPage = $perPage  total = $total"
    }
}
