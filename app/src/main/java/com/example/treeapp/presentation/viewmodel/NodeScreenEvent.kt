package com.example.treeapp.presentation.viewmodel

interface NodeScreenEvent {

    data class OnDeleteChild(val id: Int): NodeScreenEvent

    data class OnAddChild(val name: String, val parent: Int?): NodeScreenEvent

    data class OnLoadChildrenList(val id: Int): NodeScreenEvent

    data class OnLoadAllNodes(val id: Int): NodeScreenEvent

}