package com.andrewinscore.docmachine

import com.andrewinscore.docmachine.style.CLEAR
import com.andrewinscore.docmachine.style.CMYKColor
import org.apache.pdfbox.pdmodel.PDPageContentStream

class Container : Drawable {
    private var fillColor: CMYKColor = CLEAR
    private var strokeColor: CMYKColor = CLEAR
    private var _content: Drawable? = null

    private var paddingTop = 0.0
    private var paddingBottom = 0.0
    private var paddingLeft = 0.0
    private var paddingRight = 0.0

    fun setFillColor(fillColor: CMYKColor) {
        this.fillColor = fillColor
    }

    fun setStrokeColor(strokeColor: CMYKColor) {
        this.strokeColor = strokeColor
    }

    fun setPadding(padding: Float) {
        setPadding(padding, padding)
    }

    fun setPadding(verticalPadding: Float, horizontalPadding: Float) {
        paddingBottom = verticalPadding.toDouble()
        paddingTop = paddingBottom
        paddingRight = horizontalPadding.toDouble()
        paddingLeft = paddingRight
    }

    fun setPaddingTop(topPadding: Float) {
        this.paddingTop = topPadding.toDouble()
    }

    fun setPaddingBottom(bottomPadding: Float) {
        this.paddingBottom = bottomPadding.toDouble()
    }

    fun setPaddingLeft(leftPadding: Float) {
        this.paddingLeft = leftPadding.toDouble()
    }

    fun setPaddingRight(rightPadding: Float) {
        this.paddingRight = rightPadding.toDouble()
    }

    fun setContent(content: Drawable) {
        _content = content
    }

    override fun draw(renderedDoc: RenderedDocument): RenderedDrawable {
        val contentRenderedDrawable = _content!!.draw(renderedDoc)

        val width = paddingLeft + paddingRight + contentRenderedDrawable.width
        val height = paddingTop + paddingBottom + contentRenderedDrawable.height

        val draw = { stream: PDPageContentStream, loc:PdfLoc ->
            stream.setNonStrokingColor(fillColor.c.toFloat(), fillColor.m.toFloat(), fillColor.y.toFloat(), fillColor.k.toFloat())
            stream.addRect(loc.x.toFloat(), (loc.y - height).toFloat(), width.toFloat(), height.toFloat())
            stream.fill()

            val contentLocation = loc.add(paddingLeft, -paddingTop)
            contentRenderedDrawable.draw(stream, contentLocation)
        }

        return RenderedDrawable(width, height, draw)
    }

    override fun draw(renderedDoc: RenderedDocument, bounds: Bounds): RenderedDrawable {
        val contentRenderedDrawable = _content!!.draw(renderedDoc)

        val width = bounds.width
        val height = bounds.height

        val draw = { stream: PDPageContentStream, loc:PdfLoc ->
            stream.setNonStrokingColor(fillColor.c.toFloat(), fillColor.m.toFloat(), fillColor.y.toFloat(), fillColor.k.toFloat())
            stream.addRect(loc.x.toFloat(), (loc.y - height).toFloat(), width.toFloat(), height.toFloat())
            stream.fill()

            val contentLocation = loc.add(paddingLeft, -paddingTop)
            contentRenderedDrawable.draw(stream, contentLocation)
        }

        return RenderedDrawable(width, height, draw)
    }
}
