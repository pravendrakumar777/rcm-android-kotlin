package com.rcm.engineering.user.models

data class Challan(
    val id: Long,
    val challanNo: String,
    val refChNo: String?,
    val date: String,
    val customerName: String,
    val items: List<ChallanItem>,
    val modifiedDate: String
)
