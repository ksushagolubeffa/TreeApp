package com.example.treeapp.presentation.viewmodel

import androidx.compose.runtime.Immutable
import com.example.treeapp.data.NodeEntity

@Immutable
data class NodeScreenState(
    val error: Throwable? = null,
    val children: List<NodeEntity>? = null,
    val count: Int = 0
)