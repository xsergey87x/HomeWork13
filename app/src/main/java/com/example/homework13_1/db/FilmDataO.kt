package com.example.homework13_1.db

import androidx.room.*


@Dao
interface FilmDataO {

    @Transaction
    suspend fun transactionSample(films: Films) {
        insertAll(films)
        delete(films)
    }

    @Query("SELECT * FROM films")
    suspend fun getAll(): List<Films>

    @Query("SELECT * FROM films WHERE id IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<Films>

    @Query("SELECT * FROM films WHERE film_name LIKE :first AND " +
            "film_name LIKE :last LIMIT 1")
    suspend fun findByName(first: String, last: String): Films

    @Insert
    suspend fun insertAll(vararg films: Films)

    @Delete
    suspend fun delete(user: Films)

    @Query("DELETE FROM films")
    suspend fun removeAll()

}