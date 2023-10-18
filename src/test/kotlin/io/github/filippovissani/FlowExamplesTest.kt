package io.github.filippovissani

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class FlowExamplesTest {

    @Test
    fun simple1() {
        FlowExamples.simple1().forEach { value -> println(value) }
    }

    @Test
    fun simple2() {
        runBlocking<Unit> {
            FlowExamples.simple2().forEach { value -> println(value) }
        }
    }

    @Test
    fun simple3() {
        runBlocking<Unit> {
            // Launch a concurrent coroutine to check if the main thread is blocked
            launch {
                for (k in 1..3) {
                    println("I'm not blocked $k")
                    delay(100)
                }
            }
            // Collect the flow
            FlowExamples.simple3().collect { value -> println(value) }
        }
    }

    @Test
    fun simple4() {
        runBlocking<Unit> {
            println("Calling simple function...")
            val flow = FlowExamples.simple4()
            println("Calling collect...")
            flow.collect { value -> println(value) }
            println("Calling collect again...")
            flow.collect { value -> println(value) }
        }
    }

    @Test
    fun simple5() {
        runBlocking<Unit> {
            withTimeoutOrNull(250) { // Timeout after 250ms
                FlowExamples.simple5().collect { value -> println(value) }
            }
            println("Done")
        }
    }

    @Test
    fun performRequest() {
        runBlocking<Unit> {
            (1..3).asFlow() // a flow of requests
                .map { request -> FlowExamples.performRequest(request) }
                .collect { response -> println(response) }
        }
    }

    @Test
    fun performRequest2() {
        runBlocking<Unit> {
            (1..3).asFlow() // a flow of requests
                .transform { request ->
                    emit("Making request $request")
                    emit(FlowExamples.performRequest(request))
                }
                .collect { response -> println(response) }
        }
    }

    @Test
    fun numbers() =
        runBlocking<Unit> {
            FlowExamples.numbers()
                .take(2) // take only the first two
                .collect { value -> println(value) }
        }
}
