package com.andrewinscore.docmachine.component

import com.andrewinscore.docmachine.Bounds
import com.andrewinscore.docmachine.Drawable
import com.andrewinscore.docmachine.RenderFunction
import com.andrewinscore.docmachine.RenderedDocument
import com.andrewinscore.docmachine.RenderedDrawable
import com.andrewinscore.docmachine.style.CLEAR
import com.andrewinscore.docmachine.style.CMYKColor

/**
 * @author ainscore
 */
class Rectangle(val width: Double, val height: Double, val fillColor: CMYKColor, val strokeColor: CMYKColor = CLEAR, val strokeWidth: Double = 0.0) : Drawable {

    override fun draw(renderedDoc: RenderedDocument): RenderedDrawable {
        val (c, m, y, k) = fillColor
        val (strokeC, strokeM, strokeY, strokeK) = strokeColor

        val draw: RenderFunction = { stream, loc ->
            stream.setNonStrokingColor(c.toFloat(), m.toFloat(), y.toFloat(), k.toFloat())
            stream.addRect(loc.x.toFloat(), (loc.y - height).toFloat(), width.toFloat(), height.toFloat())
            if (strokeColor == CLEAR) {
                stream.fill()
            } else {
                stream.setStrokingColor(strokeC.toFloat(), strokeM.toFloat(), strokeY.toFloat(), strokeK.toFloat())
                stream.setLineWidth(strokeWidth.toFloat())
                stream.fillAndStroke()
            }
        }

        return RenderedDrawable(width, height, draw)
    }

    override fun draw(renderedDoc: RenderedDocument, bounds: Bounds): RenderedDrawable {
        return draw(renderedDoc)
    }
}
