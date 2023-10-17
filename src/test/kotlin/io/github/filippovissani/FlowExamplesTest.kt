package io.github.filippovissani

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
}