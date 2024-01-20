package com.achmea.demo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EmployerEntity(
    @PrimaryKey(autoGenerate = false)
    val employerID: Int?,
    val discountPercentage: Int?,
    val name: String?,
    val place: String?,
    var timestamp: Long? = null
)
