package com.pdmcourse2026.basictemplate.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pdmcourse2026.basictemplate.domain.model.Question

@Entity(tableName = "questions")
data class QuestionEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val title: String,
)

fun QuestionEntity.toModel(): Question {
  return Question(
    id = id,
    title = title,
    optionCount = 0
  )
}
