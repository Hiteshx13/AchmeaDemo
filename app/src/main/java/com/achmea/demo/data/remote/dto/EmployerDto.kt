package com.achmea.demo.data.remote.dto

import androidx.room.PrimaryKey
import com.achmea.demo.data.local.EmployerEntity
import com.achmea.demo.domain.model.Employer
import com.google.gson.annotations.SerializedName

data class EmployerDto(
    @SerializedName("DiscountPercentage")
    val discountPercentage: Int?,

    @PrimaryKey(autoGenerate = false)
    @SerializedName("EmployerID")
    val employerID: Int?,

    @SerializedName("Name")
    val name: String?,

    @SerializedName("Place")
    val place: String?,
    var timestamp: Long? = null
)

fun EmployerDto.toEmployerEntity(): EmployerEntity {
    return EmployerEntity(
        discountPercentage = discountPercentage,
        employerID = employerID,
        name = name,
        place = place,
        timestamp = timestamp
    )
}

fun EmployerDto.toEmployer(): Employer {
    return Employer(
        discountPercentage = discountPercentage,
        employerID = employerID,
        name = name,
        place = place,
        timestamp = timestamp
    )
}