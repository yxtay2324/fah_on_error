package com.github.yxtay2324.fah.startup

import com.github.yxtay2324.fah.listeners.TerminalExitListener
import com.github.yxtay2324.fah.subscribers.DaemonSubscriber
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class MyProjectActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        thisLogger().warn("Starting MyProjectActivity")

        DaemonSubscriber(project, project)
        TerminalExitListener(project)
    }
}