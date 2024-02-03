package com.example.treeapp.data

interface NodeRepository {
    suspend fun getChildren(id: Int): List<NodeEntity>?

    suspend fun addNode(name: String, parent: Int?)

    suspend fun deleteNode(id: Int)

    suspend fun getAll(): Int
}