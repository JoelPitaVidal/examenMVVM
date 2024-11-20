package com.example.examenmvvm

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyViewModel(): ViewModel() {

    // etiqueta para logcat
    private val TAG_LOG = "miDebug"

    // estados del juego
    // usamos LiveData para que la IU se actualice
    // patron de diseño observer
    val estadoLiveData: MutableLiveData<Estados?> = MutableLiveData(Estados.INICIO)

    // este va a ser nuestra lista para la secuencia random
    // usamos mutable, ya que la queremos modificar
    var _numbers = mutableStateOf(0)

    // inicializamos variables cuando instanciamos
    init {
    // estado inicial
        Log.d(TAG_LOG, "Inicializamos ViewModel - Estado: ${estadoLiveData.value}")
    }
    // Nuevo LiveData para la cuenta atrás:
    val cuentaAtrasLiveData: MutableLiveData<Int> = MutableLiveData(5)
    /**
     * crear entero random
     */
    fun crearRandom() {
        // cambiamos estado, por lo tanto la IU se actualiza
        estadoLiveData.value = Estados.GENERANDO
        _numbers.value = (0..3).random()
        Log.d(TAG_LOG, "creamos random ${_numbers.value} - Estado: ${estadoLiveData.value}")
        actualizarNumero(_numbers.value)
        iniciarCuentaAtras()
    }
    /**
     * actualizar numero en datos
     * @param numero: Int numero random
     */
    fun actualizarNumero(numero: Int) {
        Log.d(TAG_LOG, "actualizamos numero en Datos - Estado: ${estadoLiveData.value}")
        Datos.numero = numero
        // cambiamos estado, por lo tanto la IU se actualiza
        estadoLiveData.value = Estados.ADIVINANDO
    }
    /**
     * creamos cuenta atras
     */
    fun iniciarCuentaAtras() {
        estadoLiveData.value == Estados.CONTANDO
        // iniciamos cuenta atrás en 5 segundos
        viewModelScope.launch {
            //inicia la cuenta atás regresiva desde 5
            for (i in 5 downTo 1) {
                cuentaAtrasLiveData.value = i
                Log.d(TAG_LOG, "Cuenta atrás: $i")
                //Hacemos que la corrutina espere 1 segundo antes de continuar
                delay(1000)
            }
            // Si la cuenta atrás llega a 1 y el juego aún está en estado ADIVINANDO, reiniciar a INICIO
            if (estadoLiveData.value == Estados.ADIVINANDO) {
                estadoLiveData.value = Estados.INICIO
            }
        }
    }
    /**
     * comprobar si el boton pulsado es el correcto
     * @param ordinal: Int numero de boton pulsado
     * @return Boolean si coincide TRUE, si no FALSE
     */
    fun comprobar(ordinal: Int): Boolean {

        // mientras comprobamos, lanzamos estados auxiliares en paralelo
        estadosAuxiliares()

        Log.d(TAG_LOG, "comprobamos - Estado: ${estadoLiveData.value}")
        return if (ordinal == Datos.numero) {
            Log.d(TAG_LOG, "es correcto")
            estadoLiveData.value = Estados.INICIO
            Log.d(TAG_LOG, "GANAMOS - Estado: ${estadoLiveData.value}")
            true
        } else {
            Log.d(TAG_LOG, "no es correcto")
            estadoLiveData.value = Estados.ADIVINANDO
            Log.d(TAG_LOG, "otro intento - Estado: ${estadoLiveData.value}")
            false
        }
    }

    /**
     * Corutina que lanza estados auxiliares
     */
    fun estadosAuxiliares() {
        viewModelScope.launch {
            // guardamos el estado auxiliar
            var estadoAux = EstadosAuxiliares.AUX1

            // hacemos un cambio a tres estados auxiliares
            Log.d(TAG_LOG, "estado (corutina): ${estadoAux}")
            delay(1500)
            estadoAux = EstadosAuxiliares.AUX2
            Log.d(TAG_LOG, "estado (corutina): ${estadoAux}")
            delay(1500)
            estadoAux = EstadosAuxiliares.AUX3
            Log.d(TAG_LOG, "estado (corutina): ${estadoAux}")
            delay(1500)
        }
    }
}