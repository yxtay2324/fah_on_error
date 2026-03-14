package com.github.yxtay2324.fah.subscribers

import com.github.yxtay2324.fah.services.MyProjectService
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project

class DaemonSubscriber(private val project: Project, disposable: Disposable) {
    private val service = project.service<MyProjectService>()

    init {
        println("subscribing to daemon")
        project.messageBus.connect(disposable).subscribe(
            DaemonCodeAnalyzer.DAEMON_EVENT_TOPIC,
            object : DaemonCodeAnalyzer.DaemonListener {
                override fun daemonFinished(editors: Collection<FileEditor>) {
                    for (editor in editors) {
                        if (editor is TextEditor) {
                            thisLogger().warn("editor changed: $editor")
                            service.checkForNewErrors(project, editor)
                        }
                    }
                }
            }
        )
    }
}
