package com.pdmcourse2026.basictemplate.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pdmcourse2026.basictemplate.data.database.dao.OptionDao
import com.pdmcourse2026.basictemplate.data.database.dao.QuestionDao
import com.pdmcourse2026.basictemplate.data.database.entities.OptionEntity
import com.pdmcourse2026.basictemplate.data.database.entities.QuestionEntity

@Database(
  entities = [QuestionEntity::class, OptionEntity::class],
  version = 2,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun questionDao(): QuestionDao
  abstract fun optionDao(): OptionDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        Room.databaseBuilder(
          context = context.applicationContext,
          klass = AppDatabase::class.java,
          name = "rankeuca_database"
        )
        .fallbackToDestructiveMigration(true)
        .build()
        .also { INSTANCE = it }
      }
    }
  }
}
