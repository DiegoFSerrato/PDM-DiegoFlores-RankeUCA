package com.pdmcourse2026.basictemplate.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pdmcourse2026.basictemplate.domain.model.Option

@Entity(
  tableName = "options",
  foreignKeys = [
    ForeignKey(
      entity = QuestionEntity::class,
      parentColumns = ["id"],
      childColumns = ["questionId"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [Index("questionId")]
)
data class OptionEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val name: String,
  val imageUrl: String,
  val questionId: Int,
)

fun OptionEntity.toModel(): Option {
  return Option(
    id = id,
    name = name,
    imageUrl = imageUrl,
    questionId = questionId,
    votes = 0
  )
}

fun Option.toEntity(): OptionEntity {
  return OptionEntity(
    id = id,
    name = name,
    imageUrl = imageUrl,
    questionId = questionId
  )
}
