package com.example.treeapp.domain

import android.util.Log
import android.view.ViewParent
import com.example.treeapp.data.NodeRepository

class AddNodeUseCase(private val repository: NodeRepository) {
    suspend operator fun invoke(name: String, parent: Int?){
        Log.e("usecase add", "try to add")
        repository.addNode(name, parent)
    }
}