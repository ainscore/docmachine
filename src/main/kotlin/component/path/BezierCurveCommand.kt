package com.andrewinscore.docmachine.component.path

import com.andrewinscore.docmachine.PdfLoc
import org.apache.pdfbox.pdmodel.PDPageContentStream

/**
 * @author ainscore
 */
class BezierCurveCommand(
    val x1: Double,
    val y1: Double,
    val x2: Double,
    val y2: Double,
    val x3: Double,
    val y3: Double
) : PathCommand {

    override fun draw(stream: PDPageContentStream, base: PdfLoc) {
        val (x, y) = base.add(x1, y1)
        val (x1, y1) = base.add(x2, y2)
        val (x2, y2) = base.add(x3, y3)
        stream.curveTo(x.toFloat(), y.toFloat(), x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat())
    }
}
