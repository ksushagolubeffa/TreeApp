package com.example.treeapp.di

import com.example.treeapp.data.NodeDao
import com.example.treeapp.data.NodeRepository
import com.example.treeapp.data.NodeRepositoryImpl
import com.example.treeapp.domain.AddNodeUseCase
import com.example.treeapp.domain.DeleteNodeUseCase
import com.example.treeapp.domain.GetAllUseCase
import com.example.treeapp.domain.GetChildrenUseCase
import com.example.treeapp.presentation.viewmodel.NodeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val nodesModule = module {

    viewModel{ NodeViewModel(get(), get(), get(), get()) }

    single { provideNodeRepository(get()) }
    factory { provideAddNodeUseCase(get()) }
    factory { provideDeleteNodeUseCase(get()) }
    factory { provideGetChildrenUseCase(get()) }
    factory { provideGetAllUseCase(get()) }

}

private fun provideDeleteNodeUseCase(
    repository: NodeRepository,
): DeleteNodeUseCase =
    DeleteNodeUseCase(repository)

private fun provideGetChildrenUseCase(
    repository: NodeRepository,
): GetChildrenUseCase =
    GetChildrenUseCase(repository)

private fun provideGetAllUseCase(
    repository: NodeRepository,
): GetAllUseCase =
    GetAllUseCase(repository)

private fun provideAddNodeUseCase(
    repository: NodeRepository,
): AddNodeUseCase =
    AddNodeUseCase(repository)

private fun provideNodeRepository(
    nodeDao: NodeDao
): NodeRepository =
    NodeRepositoryImpl(nodeDao)