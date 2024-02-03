package com.example.treeapp.di

import android.content.Context
import androidx.room.Room
import com.example.treeapp.data.NodeDao
import com.example.treeapp.data.NodeDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { provideAppDatabase(get()) }
    factory { provideNodeDao(get()) }
}

fun provideAppDatabase(context: Context): NodeDatabase {
    return Room.databaseBuilder(context, NodeDatabase::class.java, "node.db")
        .allowMainThreadQueries()
        .build()
}

fun provideNodeDao(nodeDatabase: NodeDatabase): NodeDao {
    return nodeDatabase.getNodeDao()
}
