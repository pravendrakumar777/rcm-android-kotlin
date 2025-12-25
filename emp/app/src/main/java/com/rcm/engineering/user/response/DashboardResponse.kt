package com.rcm.engineering.user.response

data class DashboardResponse(
    val totalEmployees: Int,
    val present: Int,
    val absent: Int,
    val checkInData: Map<String, Float>,
    val checkOutData: Map<String, Float>,
    val statusDistribution: Map<String, Int>
)
