package com.gabriele.patientkiosk

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gabriele.patientkiosk.repository.QuestionnaireRepository

class QuestionnaireListActivity : AppCompatActivity() {

    private lateinit var rvQuestionnaires: RecyclerView
    private lateinit var tvPatientCode: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire_list)

        val patientCode = intent.getStringExtra("patient_code") ?: ""

        tvPatientCode = findViewById(R.id.tvPatientCode)
        rvQuestionnaires = findViewById(R.id.rvQuestionnaires)

        tvPatientCode.text = "Paziente: $patientCode"

        val repository = QuestionnaireRepository(this)
        val questionnaires = repository.loadAll()

        val adapter = QuestionnaireAdapter(questionnaires) { questionnaire ->
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("patient_code", patientCode)
            intent.putExtra("questionnaire_id", questionnaire.id)
            startActivity(intent)
        }

        rvQuestionnaires.layoutManager = LinearLayoutManager(this)
        rvQuestionnaires.adapter = adapter
    }
}