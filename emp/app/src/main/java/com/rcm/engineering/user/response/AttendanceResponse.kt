package com.rcm.engineering.user.response

data class AttendanceResponse(
    val empCode: String,
    val date: String,
    val status: String,
    val checkIn: String?,
    val checkOut: String?
)
