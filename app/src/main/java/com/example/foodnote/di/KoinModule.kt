package com.example.foodnote.di

import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.foodnote.data.databaseRoom.DataBase
import com.example.foodnote.data.databaseRoom.dao.DaoDB
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepositoryImpl
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.ViewModelWaterFragmentCompose
import com.example.foodnote.ui.noteBook.viewModel.ViewModelConstructorFragment
import com.example.foodnote.ui.noteBook.viewModel.ViewModelNotesFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
    single(named(DATA_BASE_CALORIES)) {
        get<DataBase>().getDataBaseNotesCalories()
    }
}

val dataStoreModule = module {
    single(named(NAME_DATA_STORE_PREF)) {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { androidContext().preferencesDataStoreFile(NAME_DATA_STORE_PREF_FILE) }
        )
    }

    single<UserPreferencesRepository>(named(NAME_PREF_APP_REPOSITORY)) {
        UserPreferencesRepositoryImpl(get(named(NAME_DATA_STORE_PREF)))
    }
}

val calorieCalculatorScreenModule = module {
    viewModel { ViewModelWaterFragmentCompose() }

    single{
        Room.databaseBuilder(androidContext(), DataBase::class.java, DATA_BASE_NAME).fallbackToDestructiveMigration().build()
    }
}

val noteBookModule = module {
    viewModel {
        ViewModelConstructorFragment()
    }

    viewModel(named(VIEW_MODEL_NOTES)) { (dao: DaoDB) ->
        ViewModelNotesFragment(dao)
    }
}
