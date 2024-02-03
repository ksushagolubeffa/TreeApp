package com.example.treeapp.domain

import com.example.treeapp.data.NodeRepository

class GetAllUseCase(private val repository: NodeRepository) {
    suspend operator fun invoke(): Int{
        return repository.getAll()
    }
}