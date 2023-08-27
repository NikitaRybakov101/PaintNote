package com.example.foodnote.di

import androidx.room.Room
import com.example.foodnote.data.databaseRoom.DataBase
import com.example.foodnote.data.databaseRoom.dao.DaoDB
import com.example.foodnote.ui.noteBook.canvas.viewModel.CanvasPaintFragmentViewModel
import com.example.foodnote.ui.noteBook.viewModel.ViewModelNotesFragment
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val applicationModule = module {
    single{
        Room.databaseBuilder(androidContext(), DataBase::class.java, DATA_BASE_NAME).fallbackToDestructiveMigration().build()
    }
    single(named(DATA_BASE)) {
        get<DataBase>().dataBase()
    }
}

val noteBookModule = module {
    viewModel(named(VIEW_MODEL_NOTES)) { (dao: DaoDB) ->
        ViewModelNotesFragment(dao)
    }

    viewModel(named(VIEW_MODEL_CANVAS)) {
        CanvasPaintFragmentViewModel()
    }
}
