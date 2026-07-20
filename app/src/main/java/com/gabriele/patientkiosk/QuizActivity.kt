package com.gabriele.patientkiosk

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gabriele.patientkiosk.model.Questionnaire
import com.gabriele.patientkiosk.repository.QuestionnaireRepository

class QuizActivity : AppCompatActivity() {

    private lateinit var tvQuestionnaireName: TextView
    private lateinit var tvProgress: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvQuestion: TextView
    private lateinit var llAnswers: LinearLayout
    private lateinit var btnBack: Button
    private lateinit var btnNext: Button

    private lateinit var questionnaire: Questionnaire
    private var currentQuestionIndex = 0
    private val selectedAnswers = mutableMapOf<Int, Int>()
    private lateinit var patientCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        patientCode = intent.getStringExtra("patient_code") ?: ""
        val questionnaireId = intent.getStringExtra("questionnaire_id") ?: ""

        tvQuestionnaireName = findViewById(R.id.tvQuestionnaireName)
        tvProgress = findViewById(R.id.tvProgress)
        progressBar = findViewById(R.id.progressBar)
        tvQuestion = findViewById(R.id.tvQuestion)
        llAnswers = findViewById(R.id.llAnswers)
        btnBack = findViewById(R.id.btnBack)
        btnNext = findViewById(R.id.btnNext)

        val repository = QuestionnaireRepository(this)
        questionnaire = repository.loadAll().first { it.id == questionnaireId }

        showQuestion()

        btnNext.setOnClickListener {
            val currentQuestion = questionnaire.questions[currentQuestionIndex]
            if (!selectedAnswers.containsKey(currentQuestion.id)) return@setOnClickListener

            if (currentQuestionIndex < questionnaire.questions.size - 1) {
                currentQuestionIndex++
                showQuestion()
            } else {
                goToResult()
            }
        }

        btnBack.setOnClickListener {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                showQuestion()
            }
        }
    }

    private fun showQuestion() {
        val question = questionnaire.questions[currentQuestionIndex]
        val total = questionnaire.questions.size
        val current = currentQuestionIndex + 1

        tvQuestionnaireName.text = questionnaire.name
        tvProgress.text = "$current / $total"
        progressBar.progress = (current * 100) / total
        tvQuestion.text = question.text

        llAnswers.removeAllViews()

        question.answers.forEach { answer ->
            val btn = Button(this)
            btn.text = answer.text
            btn.textSize = 15f
            btn.setPadding(32, 24, 32, 24)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 16)
            btn.layoutParams = params

            val isSelected = selectedAnswers[question.id] == answer.score

            if (isSelected) {
                btn.setBackgroundColor(0xFF1D9E75.toInt())
                btn.setTextColor(0xFFFFFFFF.toInt())
                btn.setTypeface(null, Typeface.BOLD)
            } else {
                btn.setBackgroundColor(0xFFFFFFFF.toInt())
                btn.setTextColor(0xFF1A1A2E.toInt())
            }

            btn.setOnClickListener {
                selectedAnswers[question.id] = answer.score
                showQuestion()
            }

            llAnswers.addView(btn)
        }

        btnBack.visibility = if (currentQuestionIndex == 0) View.INVISIBLE else View.VISIBLE

        val isLast = currentQuestionIndex == questionnaire.questions.size - 1
        btnNext.text = if (isLast) "Concludi" else "Prossima domanda"
    }

    private fun goToResult() {
        val totalScore = selectedAnswers.values.sum()
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("patient_code", patientCode)
        intent.putExtra("questionnaire_id", questionnaire.id)
        intent.putExtra("total_score", totalScore)
        startActivity(intent)
    }
}