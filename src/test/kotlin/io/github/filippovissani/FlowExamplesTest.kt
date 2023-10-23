package io.github.filippovissani

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.system.measureTimeMillis

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

    @Test
    fun sum() =
        runBlocking<Unit> {
            val sum = (1..5).asFlow()
                .map { it * it } // squares of numbers from 1 to 5
                .reduce { a, b -> a + b } // sum them (terminal operator)
            println(sum)
        }

    @Test
    fun sequentialFlow() =
        runBlocking<Unit> {
            (1..5).asFlow()
                .filter {
                    println("Filter $it")
                    it % 2 == 0
                }
                .map {
                    println("Map $it")
                    "string $it"
                }.collect {
                    println("Collect $it")
                }
        }

    @Test
    fun context() =
        runBlocking<Unit> {
            FlowExamples.simple6().collect { value -> FlowExamples.log("Collected $value") }
        }

    @Test
    fun simple7() =
        runBlocking<Unit> {
            assertThrows<IllegalStateException> {
                FlowExamples.simple7().collect { value -> println(value) }
            }
        }

    @Test
    fun simple8() =
        runBlocking<Unit> {
            FlowExamples.simple8().collect { value ->
                FlowExamples.log("Collected $value")
            }
        }

    @Test
    fun simple9() =
        runBlocking<Unit> {
            val time = measureTimeMillis {
                FlowExamples.simple9().collect { value ->
                    delay(300) // pretend we are processing it for 300 ms
                    println(value)
                }
            }
            println("Collected in $time ms")
        }

    @Test
    fun simple9Buffer() =
        runBlocking<Unit> {
            val time = measureTimeMillis {
                FlowExamples.simple9()
                    .buffer() // buffer emissions, don't wait
                    .collect { value ->
                        delay(300) // pretend we are processing it for 300 ms
                        println(value)
                    }
            }
            println("Collected in $time ms")
        }

    @Test
    fun simple9Conflation() =
        runBlocking<Unit> {
            val time = measureTimeMillis {
                FlowExamples.simple9()
                    .conflate() // conflate emissions, don't process each one
                    .collect { value ->
                        delay(300) // pretend we are processing it for 300 ms
                        println(value)
                    }
            }
            println("Collected in $time ms")
        }

    @Test
    fun simple9CollectLatest() =
        runBlocking<Unit> {
            val time = measureTimeMillis {
                FlowExamples.simple9()
                    .collectLatest { value -> // cancel & restart on the latest value
                        println("Collecting $value")
                        delay(300) // pretend we are processing it for 300 ms
                        println("Done $value")
                    }
            }
            println("Collected in $time ms")
        }
}
