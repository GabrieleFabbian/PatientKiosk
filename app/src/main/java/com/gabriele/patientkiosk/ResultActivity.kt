package com.gabriele.patientkiosk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gabriele.patientkiosk.repository.QuestionnaireRepository

class ResultActivity : AppCompatActivity() {

    private lateinit var tvQuestionnaireName: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvMaxScore: TextView
    private lateinit var tvInterpretation: TextView
    private lateinit var btnFinish: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val patientCode = intent.getStringExtra("patient_code") ?: ""
        val questionnaireId = intent.getStringExtra("questionnaire_id") ?: ""
        val totalScore = intent.getIntExtra("total_score", 0)

        tvQuestionnaireName = findViewById(R.id.tvQuestionnaireName)
        tvScore = findViewById(R.id.tvScore)
        tvMaxScore = findViewById(R.id.tvMaxScore)
        tvInterpretation = findViewById(R.id.tvInterpretation)
        btnFinish = findViewById(R.id.btnFinish)

        val repository = QuestionnaireRepository(this)
        val questionnaire = repository.loadAll().first { it.id == questionnaireId }

        tvQuestionnaireName.text = "${questionnaire.name} — ${questionnaire.description}"
        tvScore.text = "$totalScore"
        tvMaxScore.text = "su ${questionnaire.maxScore}"

        val interpretation = questionnaire.interpretations
            .firstOrNull { totalScore >= it.min && totalScore <= it.max }
        tvInterpretation.text = interpretation?.label ?: "—"

        btnFinish.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}