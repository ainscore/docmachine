package com.andrewinscore.docmachine.component.path

import com.andrewinscore.docmachine.PdfLoc
import org.apache.pdfbox.pdmodel.PDPageContentStream

class MoveCommand(val x: Double, val y: Double) : PathCommand {
    override fun draw(stream: PDPageContentStream, base: PdfLoc) {
        val abs = base.add(x, y)
        stream.moveTo(abs.x.toFloat(), abs.y.toFloat())
    }
}
