package com.andrewinscore.docmachine.component.path

import com.andrewinscore.docmachine.Bounds
import com.andrewinscore.docmachine.Drawable
import com.andrewinscore.docmachine.RenderFunction
import com.andrewinscore.docmachine.RenderedDocument
import com.andrewinscore.docmachine.RenderedDrawable
import com.andrewinscore.docmachine.style.CLEAR
import com.andrewinscore.docmachine.style.CMYKColor


class Path(val commands: List<PathCommand>, val fillColor: CMYKColor, val strokeColor: CMYKColor) :
    Drawable {
    override fun draw(renderedDoc: RenderedDocument): RenderedDrawable {
        val draw: RenderFunction = { stream, loc ->
            if (fillColor != CLEAR) {
                commands.forEach { command -> command.draw(stream, loc) }
                stream.setNonStrokingColor(
                    fillColor.c.toFloat(),
                    fillColor.m.toFloat(),
                    fillColor.y.toFloat(),
                    fillColor.k.toFloat()
                )
                stream.fillEvenOdd()
            }
            if (strokeColor != CLEAR) {
                commands.forEach { command -> command.draw(stream, loc) }
                stream.setStrokingColor(
                    strokeColor.c.toFloat(),
                    strokeColor.m.toFloat(),
                    strokeColor.y.toFloat(),
                    strokeColor.k.toFloat()
                )
                stream.stroke()
            }
        }
        return RenderedDrawable(0.0, 0.0, draw)
    }

    override fun draw(renderedDoc: RenderedDocument, bounds: Bounds): RenderedDrawable {
        return draw(renderedDoc)
    }

}
