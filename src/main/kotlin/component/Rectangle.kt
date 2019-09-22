package com.andrewinscore.docmachine.component

import com.andrewinscore.docmachine.Bounds
import com.andrewinscore.docmachine.Drawable
import com.andrewinscore.docmachine.RenderFunction
import com.andrewinscore.docmachine.RenderedDocument
import com.andrewinscore.docmachine.RenderedDrawable
import com.andrewinscore.docmachine.style.CMYKColor

/**
 * @author ainscore
 */
class Rectangle(val width: Double, val height: Double, val fillColor: CMYKColor) : Drawable {

    override fun draw(renderedDoc: RenderedDocument): RenderedDrawable {
        val (c, m, y, k) = fillColor

        val draw: RenderFunction = { stream, loc ->
            stream.setNonStrokingColor(c.toFloat(), m.toFloat(), y.toFloat(), k.toFloat())
            stream.addRect(loc.x.toFloat(), loc.y.toFloat(), width.toFloat(), height.toFloat())
            stream.fill()
        }

        return RenderedDrawable(width, height, draw)
    }

    override fun draw(renderedDoc: RenderedDocument, bounds: Bounds): RenderedDrawable {
        return draw(renderedDoc)
    }
}
