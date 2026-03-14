package com.github.yxtay2324.fah.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.github.yxtay2324.fah.MyBundle
import java.io.BufferedInputStream
import javax.sound.sampled.AudioSystem
import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerEx
import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.TextEditor

@Service(Service.Level.PROJECT)
class MyProjectService(project: Project) {

    private val processedErrorOffsets = mutableSetOf<Int>()

    init {
        thisLogger().info(MyBundle.message("projectService", project.name))
    }

    fun checkForNewErrors(project: Project, textEditor: TextEditor) {
        val document: Document = textEditor.editor.document

        var foundNewError = false

        // Process only high-severity ERROR highlights (redlines)
        DaemonCodeAnalyzerEx.processHighlights(
            document, project, HighlightSeverity.ERROR,
            0, document.textLength
        ) { info: HighlightInfo ->
            val offset = info.startOffset

            // Only trigger if we haven't played a sound for this specific error location yet
            if (!processedErrorOffsets.contains(offset)) {
                processedErrorOffsets.add(offset)
                foundNewError = true
            }
            true // Continue processing
        }

        if (foundNewError) {
            thisLogger().warn("Error found, playing sound")
            this.playSound("sounds/fahhhhh.wav")
        }
    }

    fun playSound(soundFileName: String?) {
        try {
            val inputStream = javaClass.getResourceAsStream("/" + soundFileName)
            val audioStream = AudioSystem.getAudioInputStream(BufferedInputStream(inputStream))
            val clip = AudioSystem.getClip()
            clip.open(audioStream)
            clip.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
