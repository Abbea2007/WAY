package com.example.wayapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wayapp.model.ObjetoReportado
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.JvmSuppressWildcards

@Dao
interface ObjetoDao {

    // 1. Insertar un objeto (Si ya existe un objeto con el mismo ID, lo actualiza)
    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarObjeto(objeto: ObjetoReportado): Long

    // 2. Insertar una lista de objetos de golpe (Ideal para guardar lo que descarguemos de Firebase)
    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarObjetos(objetos: List<ObjetoReportado>): List<Long>

    // 3. Obtener todos los objetos
    // Cualquier cambio realizado a la db sera notificado a la pantalla
    @Query("SELECT * FROM objetos_reportados")
    fun obtenerTodosLosObjetos(): Flow<List<ObjetoReportado>>

    // 4. Limpiar la tabla (útil si queremos forzar una sincronización limpia)
    @JvmSuppressWildcards
    @Query("DELETE FROM objetos_reportados")
    suspend fun borrarTodosLosObjetos(): Int

    // Permite encontrar los detalles especificos de cada objeto
    // y mostrarlo en la pantalla de mas detalles
    @Query("SELECT * FROM objetos_reportados WHERE id = :id")
    fun obtenerObjetoPorId(id: String): Flow<ObjetoReportado?>
}