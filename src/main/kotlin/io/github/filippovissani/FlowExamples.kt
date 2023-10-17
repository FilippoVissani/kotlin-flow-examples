package io.github.filippovissani

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object FlowExamples {
    // This code outputs 1, 2, 3, but it waits 1000ms before printing each one.
    fun simple1(): Sequence<Int> = sequence { // sequence builder
        for (i in 1..3) {
            Thread.sleep(1000) // pretend we are computing it
            yield(i) // yield next value
        }
    }

    suspend fun simple2(): List<Int> {
        delay(1000) // pretend we are doing something asynchronous here
        return listOf(1, 2, 3)
    }

    fun simple3(): Flow<Int> = flow { // flow builder
        for (i in 1..3) {
            delay(1000) // pretend we are doing something useful here
            emit(i) // emit next value
        }
    }

    fun simple4(): Flow<Int> = flow {
        println("Flow started")
        for (i in 1..3) {
            delay(1000)
            emit(i)
        }
    }
}
