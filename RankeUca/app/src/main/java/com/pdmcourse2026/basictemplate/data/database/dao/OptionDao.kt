package com.pdmcourse2026.basictemplate.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pdmcourse2026.basictemplate.data.database.entities.OptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OptionDao {

  @Query("SELECT * FROM options WHERE questionId = :questionId")
  fun getOptionsForQuestion(questionId: Int): Flow<List<OptionEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOption(option: OptionEntity)

  @Delete
  suspend fun deleteOption(option: OptionEntity)
}
