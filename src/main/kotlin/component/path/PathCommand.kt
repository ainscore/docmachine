package com.andrewinscore.docmachine.component.path

import com.andrewinscore.docmachine.PdfLoc
import org.apache.pdfbox.pdmodel.PDPageContentStream

/**
 * @author ainscore
 */

interface PathCommand {
    fun draw(stream: PDPageContentStream, base: PdfLoc)
}
