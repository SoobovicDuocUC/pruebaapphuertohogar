package com.example.projectohuertoapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.projectohuertoapp.data.local.entity.Usuario

@Database(entities = [Usuario::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
}
