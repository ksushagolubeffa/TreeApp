package com.example.treeapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NodeDao {

    @Query("DELETE FROM node WHERE id = :id")
    fun delete(id: Int)

    @Insert
    fun add(node: Node)

    @Query("select * from node where parent = :id")
    fun getChildren(id: Int): List <Node>?

    @Query("SELECT COUNT(*) FROM node")
    fun getAll(): Int
}