package com.example.homework13_1.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Films(
    @ColumnInfo(name = "film_name")
    val filmName: String?,
    @ColumnInfo(name = "film_author")
    val filmAuthor: String?,
    @ColumnInfo(name = "film_year")
    val filmYear: String?,
    @ColumnInfo(name = "film_description")
    val filmDescription: String?,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}