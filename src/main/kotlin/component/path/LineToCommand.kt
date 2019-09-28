package com.andrewinscore.docmachine.component.path

import com.andrewinscore.docmachine.PdfLoc
import org.apache.pdfbox.pdmodel.PDPageContentStream

/**
 * @author ainscore
 */
class LineToCommand(val x: Double, val y: Double) : PathCommand {

    override fun draw(stream: PDPageContentStream, base: PdfLoc) {
        val abs = base.add(x, y)
        stream.lineTo(abs.x.toFloat(), abs.y.toFloat())
    }
}
