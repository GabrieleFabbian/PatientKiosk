package com.gabriele.patientkiosk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gabriele.patientkiosk.model.Questionnaire

class QuestionnaireAdapter(
    private val questionnaires: List<Questionnaire>,
    private val onItemClick: (Questionnaire) -> Unit
) : RecyclerView.Adapter<QuestionnaireAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvQuestionnaireName)
        val tvDescription: TextView = view.findViewById(R.id.tvQuestionnaireDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_questionnaire, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val questionnaire = questionnaires[position]
        holder.tvName.text = questionnaire.name
        holder.tvDescription.text = questionnaire.description
        holder.itemView.setOnClickListener { onItemClick(questionnaire) }
    }

    override fun getItemCount() = questionnaires.size
}