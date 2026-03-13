package org.example.models

data class Position(
    val row: Int,
    val column: Int,
) {
    internal fun coerceIn() {}
}