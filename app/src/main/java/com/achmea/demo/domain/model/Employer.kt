package com.achmea.demo.domain.model

data class Employer(
    val discountPercentage: Int?,
    val employerID: Int?,
    val name: String?,
    val place: String?,
    var timestamp: Long? = null
)