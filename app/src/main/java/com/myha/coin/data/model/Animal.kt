package com.myha.coin.data.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "animals")
data class Animal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") @field:SerializedName("id")
    var id: Long,
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
//    @SerializedName("photos")
//    val photos: List<Photo>? = null,
    @Embedded @field:SerializedName("contact")
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
)
@Entity(tableName = "contacts")
data class Contact(
    @ColumnInfo(name = "email") @field:SerializedName("email")
    val email: String,
    @ColumnInfo(name = "phone") @field:SerializedName("phone")
    val phone: String,
    @Embedded @field:SerializedName("address")
    val address: Address? = null
)
@Entity(tableName = "address")
data class Address(
    @ColumnInfo(name = "address1") @field:SerializedName("address1")
    val address1: String,
    @ColumnInfo(name = "address2") @field:SerializedName("address2")
    val address2: String,
    @ColumnInfo(name = "city") @field:SerializedName("city")
    val city: String,
    @ColumnInfo(name = "state") @field:SerializedName("state")
    val state: String,
    @ColumnInfo(name = "country") @field:SerializedName("country")
    val country: String
)
data class NewAnimal(
    var type: String? = "",
    var age: String? = "",
    var gender: String? = "",
    var size: String? = "",
    var coat: String? = "",
    var name: String? = "",
    var description: String? = "",
    //val photos: List<Photo>? = null,
    //var contact: Contact? = null
)