package edu.ucne.registroocupaciones.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registroocupaciones.data.database.OcupacionDb
import edu.ucne.registroocupaciones.data.local.dao.EmpleadoDao
import edu.ucne.registroocupaciones.data.local.dao.OcupacionDao
import edu.ucne.registroocupaciones.data.repository.EmpleadoRepositoryImpl
import edu.ucne.registroocupaciones.data.repository.OcupacionRepositoryImpl
import edu.ucne.registroocupaciones.domain.Empleado.repository.EmpleadoRepository
import edu.ucne.registroocupaciones.domain.Ocupaciones.repository.OcupacionRepository
import edu.ucne.registroocupaciones.data.repository.HoraExtraRepositoryImpl
import edu.ucne.registroocupaciones.data.local.dao.HoraExtraDao
import edu.ucne.registroocupaciones.domain.horaextra.repository.HoraExtraRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOcupacionDb(
        @ApplicationContext appContext: Context
    ): OcupacionDb {
        return Room.databaseBuilder(
            appContext,
            OcupacionDb::class.java,
            "OcupacionDb"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideOcupacionDao(
        ocupacionDb: OcupacionDb
    ): OcupacionDao {
        return ocupacionDb.ocupacionDao()
    }

    @Provides
    @Singleton
    fun provideEmpleadoDao(
        ocupacionDb: OcupacionDb
    ): EmpleadoDao {
        return ocupacionDb.empleadoDao()
    }

    @Provides
    @Singleton
    fun provideOcupacionRepositoryImpl(
        ocupacionDao: OcupacionDao
    ): OcupacionRepositoryImpl {
        return OcupacionRepositoryImpl(ocupacionDao)
    }

    @Provides
    @Singleton
    fun provideOcupacionRepository(
        impl: OcupacionRepositoryImpl
    ): OcupacionRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideEmpleadoRepositoryImpl(
        empleadoDao: EmpleadoDao
    ): EmpleadoRepositoryImpl {
        return EmpleadoRepositoryImpl(empleadoDao)
    }

    @Provides
    @Singleton
    fun provideEmpleadoRepository(
        impl: EmpleadoRepositoryImpl
    ): EmpleadoRepository {
        return impl
    }
    @Provides
    @Singleton
    fun provideHoraExtraDao(ocupacionDb: OcupacionDb): HoraExtraDao {
        return ocupacionDb.horaExtraDao()
    }

    @Provides
    @Singleton
    fun provideHoraExtraRepositoryImpl(horaExtraDao: HoraExtraDao): HoraExtraRepositoryImpl {
        return HoraExtraRepositoryImpl(horaExtraDao)
    }

    @Provides
    @Singleton
    fun provideHoraExtraRepository(impl: HoraExtraRepositoryImpl): HoraExtraRepository {
        return impl
    }
}