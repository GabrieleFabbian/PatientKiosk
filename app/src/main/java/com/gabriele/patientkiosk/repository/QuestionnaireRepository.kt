package com.gabriele.patientkiosk.repository

import android.content.Context
import android.util.Log
import com.gabriele.patientkiosk.model.Answer
import com.gabriele.patientkiosk.model.Interpretation
import com.gabriele.patientkiosk.model.Question
import com.gabriele.patientkiosk.model.Questionnaire
import org.json.JSONObject

class QuestionnaireRepository(private val context: Context) {

    fun loadQuestionnaire(fileName: String): Questionnaire? {
        return try {
            val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val obj = JSONObject(json)

            val questionsArray = obj.getJSONArray("questions")
            val questions = mutableListOf<Question>()

            for (i in 0 until questionsArray.length()) {
                val q = questionsArray.getJSONObject(i)
                val answersArray = q.getJSONArray("answers")
                val answers = mutableListOf<Answer>()

                for (j in 0 until answersArray.length()) {
                    val a = answersArray.getJSONObject(j)
                    answers.add(Answer(a.getString("text"), a.getInt("score")))
                }

                questions.add(
                    Question(
                        id = q.getInt("id"),
                        text = q.getString("text"),
                        answers = answers,
                        type = q.optString("type", "")
                    )
                )
            }

            val interpretationsArray = obj.getJSONArray("interpretations")
            val interpretations = mutableListOf<Interpretation>()

            for (i in 0 until interpretationsArray.length()) {
                val interp = interpretationsArray.getJSONObject(i)
                interpretations.add(
                    Interpretation(
                        interp.getInt("min"),
                        interp.getInt("max"),
                        interp.getString("label")
                    )
                )
            }

            Questionnaire(
                id = obj.getString("id"),
                name = obj.getString("name"),
                description = obj.getString("description"),
                category = obj.getString("category"),
                questions = questions,
                maxScore = obj.optInt("maxScore", obj.optInt("maxRawScore", obj.optInt("maxScoreAnxiety", 0) + obj.optInt("maxScoreDepression", 0))),
                interpretations = interpretations
            )
        } catch (e: Exception) {
            Log.e("QuestionnaireRepository", "Errore nel caricamento di $fileName: ${e.message}")
            null
        }
    }

    fun loadAll(): List<Questionnaire> {
        return listOf("dlqi.json", "hads.json", "who5.json")
            .mapNotNull { loadQuestionnaire(it) }
    }
}