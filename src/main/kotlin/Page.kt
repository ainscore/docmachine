package com.andrewinscore.docmachine

import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation

/**
 * @author ainscore
 */
class Page(
    val height: Double = 792.0,
    val width: Double = 612.0,
    val pageDrawables: List<PageDrawable>,
    val annotations: List<PDAnnotation> = emptyList(),
    val outlineItems: List<String> = emptyList()
) {

    fun draw(renderedDoc: RenderedDocument) {
        val page = PDPage()

        renderedDoc.document.addPage(page)
        val stream = PDPageContentStream(renderedDoc.document, page)
        pageDrawables.forEach { pageDrawable ->
            val renderedDrawable = pageDrawable.drawable.draw(
                renderedDoc
            )

            renderedDrawable.draw(stream, PdfLoc(pageDrawable.x, height - pageDrawable.y))
        }
        page.annotations = annotations
        stream.close()

    }
}

data class PageDrawable(val drawable: Drawable, val x: Double = 0.0, val y: Double = 0.0)

