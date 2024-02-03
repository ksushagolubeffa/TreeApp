package com.example.treeapp.domain

import com.example.treeapp.data.NodeRepository

class DeleteNodeUseCase(private val repository: NodeRepository) {

    suspend operator fun invoke(id: Int){
        repository.deleteNode(id)
    }
}