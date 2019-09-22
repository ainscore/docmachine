package com.andrewinscore.docmachine

import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation

import java.io.IOException

/**
 * @author ainscore
 */
class Page(val height: Double = 792.0, val width: Double = 612.0) {

    private var _drawables: MutableList<PageDrawable> = mutableListOf()
    public var _outlineItems: MutableList<String> = mutableListOf()
    private var _annotations: MutableList<PDAnnotation> = mutableListOf()

    private inner class PageDrawable {
        var x: Double = 0.toDouble()
        var y: Double = 0.toDouble()
        var drawable: Drawable? = null
    }

    fun draw(renderedDoc: RenderedDocument) {
        val page = PDPage()
        try {
            renderedDoc.document!!.addPage(page)
            val stream = PDPageContentStream(renderedDoc.document, page)
            for (pageDrawable in _drawables) {
                val renderedDrawable = pageDrawable.drawable!!.draw(
                        renderedDoc
                )

                renderedDrawable.draw(stream, PdfLoc(pageDrawable.x, height - pageDrawable.y))
            }
            page.annotations = _annotations
            stream.close()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

    fun addDrawable(drawable: Drawable, x: Double, y: Double) {
        val pageDrawable = PageDrawable()
        pageDrawable.drawable = drawable
        pageDrawable.x = x
        pageDrawable.y = y
        _drawables.add(pageDrawable)
    }

    fun addAnnotation(annotation: PDAnnotation) {
        _annotations.add(annotation)
    }

    fun addOutlineItem(itemTitle: String) {
        _outlineItems.add(itemTitle)
    }

}
