package com.andrewinscore.docmachine.component.path

import com.andrewinscore.docmachine.Bounds
import com.andrewinscore.docmachine.Drawable
import com.andrewinscore.docmachine.RenderFunction
import com.andrewinscore.docmachine.RenderedDocument
import com.andrewinscore.docmachine.RenderedDrawable
import com.andrewinscore.docmachine.style.CLEAR
import com.andrewinscore.docmachine.style.CMYKColor

import java.io.IOException


class Path(val commands: List<PathCommand>, private val _fillColor: CMYKColor, private val _strokeColor: CMYKColor) :
    Drawable {
    override fun draw(renderedDoc: RenderedDocument): RenderedDrawable {
        val draw: RenderFunction = { stream, loc ->
            if (_fillColor != CLEAR) {
                commands.forEach { command -> command.draw(stream, loc) }
                val fillColor = _fillColor
                stream.setNonStrokingColor(
                    fillColor.c.toFloat(),
                    fillColor.m.toFloat(),
                    fillColor.y.toFloat(),
                    fillColor.k.toFloat()
                )
                stream.fillEvenOdd()
            }
            if (_strokeColor != CLEAR) {
                commands.forEach { command -> command.draw(stream, loc) }
                val strokeColor = _strokeColor
                try {
                    stream.setStrokingColor(
                        strokeColor.c.toFloat(),
                        strokeColor.m.toFloat(),
                        strokeColor.y.toFloat(),
                        strokeColor.k.toFloat()
                    )
                    stream.stroke()
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
        }
        return RenderedDrawable(0.0, 0.0, draw)
    }

    override fun draw(renderedDoc: RenderedDocument, bounds: Bounds): RenderedDrawable {
        return draw(renderedDoc)
    }

}
