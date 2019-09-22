package com.andrewinscore.docmachine.component.path

import com.andrewinscore.docmachine.PdfLoc
import org.apache.pdfbox.pdmodel.PDPageContentStream

import java.io.IOException

class MoveCommand(val x: Double, val y: Double) : PathCommand {
    override fun draw(stream: PDPageContentStream, base: PdfLoc) {
        val abs = base.add(x, y)
        try {
            stream.moveTo(abs.x.toFloat(), abs.y.toFloat())
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }
}
