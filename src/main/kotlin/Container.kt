package com.andrewinscore.docmachine

import com.andrewinscore.docmachine.style.CLEAR
import com.andrewinscore.docmachine.style.CMYKColor
import org.apache.pdfbox.pdmodel.PDPageContentStream

class Container(
    val content: Drawable,
    val fillColor: CMYKColor = CLEAR,
    val strokeColor: CMYKColor = CLEAR,
    val paddingTop: Double = 0.0,
    val paddingBottom: Double = 0.0,
    val paddingLeft: Double = 0.0,
    val paddingRight: Double = 0.0

) : Drawable {

    override fun draw(renderedDoc: RenderedDocument): RenderedDrawable {
        val contentRenderedDrawable = content.draw(renderedDoc)

        val width = paddingLeft + paddingRight + contentRenderedDrawable.width
        val height = paddingTop + paddingBottom + contentRenderedDrawable.height

        val draw = { stream: PDPageContentStream, loc: PdfLoc ->
            stream.setNonStrokingColor(
                fillColor.c.toFloat(),
                fillColor.m.toFloat(),
                fillColor.y.toFloat(),
                fillColor.k.toFloat()
            )
            stream.addRect(loc.x.toFloat(), (loc.y - height).toFloat(), width.toFloat(), height.toFloat())
            stream.fill()

            val contentLocation = loc.add(paddingLeft, -paddingTop)
            contentRenderedDrawable.draw(stream, contentLocation)
        }

        return RenderedDrawable(width, height, draw)
    }

    override fun draw(renderedDoc: RenderedDocument, bounds: Bounds): RenderedDrawable {
        val contentRenderedDrawable = content.draw(renderedDoc)

        val width = bounds.width
        val height = bounds.height

        val draw = { stream: PDPageContentStream, loc: PdfLoc ->
            stream.setNonStrokingColor(
                fillColor.c.toFloat(),
                fillColor.m.toFloat(),
                fillColor.y.toFloat(),
                fillColor.k.toFloat()
            )
            stream.addRect(loc.x.toFloat(), (loc.y - height).toFloat(), width.toFloat(), height.toFloat())
            stream.fill()

            val contentLocation = loc.add(paddingLeft, -paddingTop)
            contentRenderedDrawable.draw(stream, contentLocation)
        }

        return RenderedDrawable(width, height, draw)
    }
}
