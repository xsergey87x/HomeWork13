
package com.example.homework13_1.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Films::class], version = 1)
abstract class AppBase : RoomDatabase() {
    abstract fun filmDataO(): FilmDataO
}
