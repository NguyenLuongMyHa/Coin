package com.myha.coin.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "animals")
data class Animal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") @field:SerializedName("id")
    val id: Long?,
    @ColumnInfo(name = "type") @field:SerializedName("type")
    var type: String? = "",
    @ColumnInfo(name = "age") @field:SerializedName("age")
    var age: String? = "",
    @ColumnInfo(name = "gender") @field:SerializedName("gender")
    var gender: String? = "",
    @ColumnInfo(name = "size") @field:SerializedName("size")
    var size: String? = "",
    @ColumnInfo(name = "coat") @field:SerializedName("coat")
    var coat: String? = "",
    @ColumnInfo(name = "name") @field:SerializedName("name")
    var name: String? = "",
    @ColumnInfo(name = "description") @field:SerializedName("description")
    var description: String? = "",
    @Ignore @ColumnInfo(name = "photos") @field:SerializedName("photos")
    val photos: List<Photo>? = null,
    @Ignore @ColumnInfo(name = "contact") @field:SerializedName("contact")
    var contact: Contact? = null
) : Serializable

data class Photo(
    @SerializedName("small")
    val smallsize: String,
    @SerializedName("medium")
    val mediumsize: String,
    @SerializedName("large")
    val largesize: String,
    @SerializedName("full")
    val fullsize: String
) : Serializable

data class Contact(
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("address")
    val address: Address
) : Serializable

data class Address(
    @SerializedName("address1")
    val address1: String,
    @SerializedName("address2")
    val address2: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("country")
    val country: String
) : Serializable
