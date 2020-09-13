package com.myha.coin.data.model

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type


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
    @TypeConverters(PhotoConverter::class) @ColumnInfo(name = "photos") @field:SerializedName("photos")
    var photos: List<Photo>? = null,
    @Embedded @field:SerializedName("contact")
    var contact: Contact? = null
) : Serializable {
}

class PhotoConverter : Serializable {
    @TypeConverter
    fun fromPhotoValuesList(photos: List<Photo?>?): String? {
        if (photos == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Photo?>?>() {}.type
        return gson.toJson(photos, type)
    }

    @TypeConverter
    fun toPhotoValuesList(photos: String?): List<Photo>? {
        if (photos == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Photo?>?>() {}.type
        return gson.fromJson<List<Photo>>(photos, type)
    }
}


@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true)
    var photoId: Long,
    var animalId: Long,
    @ColumnInfo(name = "small") @field:SerializedName("small")
    var smallsize: String? = "",
    @ColumnInfo(name = "medium") @field:SerializedName("medium")
    var mediumsize: String? = "",
    @ColumnInfo(name = "large") @field:SerializedName("large")
    var largesize: String? = "",
    @ColumnInfo(name = "full") @field:SerializedName("full")
    var fullsize: String? = ""
)

data class AnimalPhotos(
    @Embedded val animal: Animal,
    @Relation(
        parentColumn = "id",
        entityColumn = "animalId"
    )
    val photos: List<Photo>
)

@Entity(tableName = "contacts")
data class Contact(
    @ColumnInfo(name = "email") @field:SerializedName("email")
    var email: String? = "",
    @ColumnInfo(name = "phone") @field:SerializedName("phone")
    var phone: String? = "",
    @Embedded @field:SerializedName("address")
    var address: Address? = null
)
@Entity(tableName = "address")
data class Address(
    @ColumnInfo(name = "address1") @field:SerializedName("address1")
    var address1: String? = "",
    @ColumnInfo(name = "address2") @field:SerializedName("address2")
    var address2: String? = "",
    @ColumnInfo(name = "city") @field:SerializedName("city")
    var city: String? = "",
    @ColumnInfo(name = "state") @field:SerializedName("state")
    var state: String? = "",
    @ColumnInfo(name = "country") @field:SerializedName("country")
    var country: String? = ""
) {
    fun getAddress(): String = "${getString(address1)}${getString(address2)}${getString(city)}${getString(
        state
    )}${getString(country)}"
    private fun getString(str: String?): String {
        return if(str.isNullOrEmpty())
            ""
        else
            str.plus(", ")
    }
}
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