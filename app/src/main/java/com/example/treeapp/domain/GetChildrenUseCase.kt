package com.example.treeapp.domain

import com.example.treeapp.data.NodeEntity
import com.example.treeapp.data.NodeRepository

class GetChildrenUseCase(private val repository: NodeRepository) {
    suspend operator fun invoke(id: Int): List<NodeEntity>?{
        return repository.getChildren(id)
    }
}