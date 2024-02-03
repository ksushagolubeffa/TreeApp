package com.example.treeapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Node::class], version = 1, exportSchema = true)
abstract class NodeDatabase: RoomDatabase() {
    abstract fun getNodeDao(): NodeDao
}