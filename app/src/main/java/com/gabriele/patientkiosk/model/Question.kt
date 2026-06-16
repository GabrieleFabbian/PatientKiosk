package com.gabriele.patientkiosk.model

data class Question(
    val id: Int,
    val text: String,
    val answers: List<Answer>
)