package com.achmea.demo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.achmea.demo.domain.model.Employer

@Entity
data class EmployerEntity(
    @PrimaryKey(autoGenerate = false)
    val employerID: Int?,
    val discountPercentage: Int?,
    val name: String?,
    val place: String?,
    var timestamp: Long? = null
)

fun EmployerEntity.toEmployer(): Employer {
    return Employer(
        discountPercentage = discountPercentage,
        employerID = employerID,
        name = name,
        place = place,
        timestamp = timestamp
    )
}
