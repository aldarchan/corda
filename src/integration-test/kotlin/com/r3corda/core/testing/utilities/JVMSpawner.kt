package com.r3corda.core.testing.utilities

import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

fun spawn(className: String, args: List<String>): Process {
    val separator = System.getProperty("file.separator")
    val classpath = System.getProperty("java.class.path")
    val path = System.getProperty("java.home") + separator + "bin" + separator + "java"
    val javaArgs = listOf(path, "-javaagent:lib/quasar.jar",  "-cp", classpath, className)
    val builder = ProcessBuilder(javaArgs + args)
    builder.redirectError(Paths.get("error.$className.log").toFile())
    val process = builder.start();
    return process
}

fun assertExitOrKill(proc: Process) {
    try {
        assertEquals(proc.waitFor(2, TimeUnit.MINUTES), true)
    } catch (e: Throwable) {
        proc.destroy()
        throw e
    }
}

fun assertAliveAndKill(proc: Process) {
    try {
        assertEquals(proc.isAlive, true)
    } finally {
        proc.destroy()
    }
}