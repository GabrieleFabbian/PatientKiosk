package com.gabriele.patientkiosk.model

data class Questionnaire(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val questions: List<Question>,
    val maxScore: Int,
    val interpretations: List<Interpretation>
)