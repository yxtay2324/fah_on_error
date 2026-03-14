package com.github.yxtay2324.fah.listeners

import com.github.yxtay2324.fah.services.MyProjectService
import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project

class TerminalExitListener(project: Project): ExecutionListener {
    private val service = project.service<MyProjectService>()

    override fun processTerminated(
        executorId: String,
        env: ExecutionEnvironment,
        handler: ProcessHandler,
        exitCode: Int
    ) {
        // Exit code 0 is success.
        // We only care about failures (usually 1, 127, etc.)
        thisLogger().warn("exit code: $exitCode")
        if (exitCode != 0) {
            service.playSound("sound/fahhhhh.wav")
        }
    }
}