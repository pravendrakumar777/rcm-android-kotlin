package com.rcm.engineering.user.models

data class ChallanItem(
    val id: Long,
    val description: String,
    val weight: String,
    val quantity: Int,
    val piecesPerKg: Double,
    val ratePerPiece: Double,
    val totalPieces: Int,
    val totalAmount: Double,
    val process: String?,
    val hsnCode: String?,
    val unit: String?
)
