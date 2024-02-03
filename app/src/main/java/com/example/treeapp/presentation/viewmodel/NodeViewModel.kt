package com.example.treeapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.treeapp.domain.AddNodeUseCase
import com.example.treeapp.domain.DeleteNodeUseCase
import com.example.treeapp.domain.GetAllUseCase
import com.example.treeapp.domain.GetChildrenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NodeViewModel(
    private val addNodeUseCase: AddNodeUseCase,
    private val deleteNodeUseCase: DeleteNodeUseCase,
    private val getChildrenUseCase: GetChildrenUseCase,
    private val getAllUseCase: GetAllUseCase,

    ) : ViewModel() {

    private val _state = MutableStateFlow(NodeScreenState())
    val state: StateFlow<NodeScreenState> = _state.asStateFlow()

    private fun addNode(name: String, parent: Int?) {
        viewModelScope.launch {
            try {
                Log.e("viewmodel add", "try to add")
                addNodeUseCase(name, parent)
                Log.e("viewmodel add", "added")
            } catch (error: Throwable) {
                _state.emit(
                    _state.value.copy(
                        error = error
                    )
                )
                Log.e("viewmodel add", error.message.toString())
            }
        }
    }

    private fun getChildren(id: Int) {
        viewModelScope.launch {
            try {
                getChildrenUseCase(id).also { children ->
                    _state.emit(
                        _state.value.copy(
                            children = children
                        )
                    )
                }
            } catch (error: Throwable) {
                _state.emit(
                    _state.value.copy(
                        error = error
                    )
                )
            }
        }
    }

    private fun getAll() {
        viewModelScope.launch {
            try {
                getAllUseCase().also { count ->
                    _state.emit(
                        _state.value.copy(
                            count = count
                        )
                    )
                }
            } catch (error: Throwable) {
                _state.emit(
                    _state.value.copy(
                        error = error
                    )
                )
            }
        }
    }

    private fun deleteNode(id: Int) {
        viewModelScope.launch {
            try {
                deleteNodeUseCase(id)
            } catch (error: Throwable) {
                _state.emit(
                    _state.value.copy(
                        error = error
                    )
                )
            }
        }
    }

    fun reducer(event: NodeScreenEvent) {
        when (event) {
            is NodeScreenEvent.OnDeleteChild -> deleteNode(event.id)
            is NodeScreenEvent.OnLoadChildrenList -> getChildren(event.id)
            is NodeScreenEvent.OnAddChild -> addNode(event.name, event.parent)
            is NodeScreenEvent.OnLoadAllNodes -> getAll()
        }
    }
}