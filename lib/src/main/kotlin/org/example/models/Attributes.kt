package org.example.models

data class Attributes(
    val foregroundColor: Color = Color.DEFAULT,
    val backgroundColor: Color = Color.DEFAULT,
    val style: CharStyle = CharStyle.DEFAULT,
)