package com.gabriele.patientkiosk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var etPatientCode: EditText
    private lateinit var btnContinue: Button
    private lateinit var tvError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        etPatientCode = findViewById(R.id.etPatientCode)
        btnContinue = findViewById(R.id.btnContinue)
        tvError = findViewById(R.id.tvError)

        btnContinue.setOnClickListener {
            val code = etPatientCode.text.toString().trim()

            if (code.isEmpty()) {
                tvError.visibility = View.VISIBLE
            } else {
                tvError.visibility = View.GONE
                val intent = Intent(this, QuestionnaireListActivity::class.java)
                intent.putExtra("patient_code", code)
                startActivity(intent)
            }
        }
    }
}