package io.github.filippovissani

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// https://kotlinlang.org/docs/flow.html#flow-builders

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

    fun simple5(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100)
            println("Emitting $i")
            emit(i)
        }
    }

    suspend fun performRequest(request: Int): String {
        delay(1000) // imitate long-running asynchronous work
        return "response $request"
    }

    fun numbers(): Flow<Int> = flow {
        try {
            emit(1)
            emit(2)
            println("This line will not execute")
            emit(3)
        } finally {
            println("Finally in numbers")
        }
    }
}
