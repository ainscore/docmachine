package com.andrewinscore.docmachine.component.path

import com.andrewinscore.docmachine.Bounds
import com.andrewinscore.docmachine.Drawable
import com.andrewinscore.docmachine.RenderFunction
import com.andrewinscore.docmachine.RenderedDocument
import com.andrewinscore.docmachine.RenderedDrawable
import com.andrewinscore.docmachine.style.CLEAR
import com.andrewinscore.docmachine.style.CMYKColor


class Path(
    val commands: List<PathCommand>,
    val fillColor: CMYKColor = CLEAR,
    val lineColor: CMYKColor = CLEAR,
    val lineWidth: Double = 0.0,
    val width: Double = 100.0,
    val height: Double = 100.0
) :
    Drawable {
    override fun draw(renderedDoc: RenderedDocument): RenderedDrawable {
        val draw: RenderFunction = { stream, baseLoc ->
            val loc = baseLoc.add(0.0, -height)
            if (fillColor != CLEAR) {
                stream.setNonStrokingColor(
                    fillColor.c.toFloat(),
                    fillColor.m.toFloat(),
                    fillColor.y.toFloat(),
                    fillColor.k.toFloat()
                )
                commands.forEach { command -> command.draw(stream, loc) }
                stream.fillEvenOdd()
            }
            if (lineColor != CLEAR) {
                stream.setLineWidth(lineWidth.toFloat())
                stream.setStrokingColor(
                    lineColor.c.toFloat(),
                    lineColor.m.toFloat(),
                    lineColor.y.toFloat(),
                    lineColor.k.toFloat()
                )
                commands.forEach { command -> command.draw(stream, loc) }
                stream.stroke()
            }
        }
        return RenderedDrawable(width, height, draw)
    }

    override fun draw(renderedDoc: RenderedDocument, bounds: Bounds): RenderedDrawable {
        return draw(renderedDoc)
    }

    companion object {
        fun newBuilder(): Builder {
            return Builder()
        }
    }

    class Builder {
        private val _commands = mutableListOf<PathCommand>()
        private var _fillColor: CMYKColor = CLEAR
        private var _lineColor: CMYKColor = CLEAR
        private var _lineWidth: Double = 1.0
        private var _width: Double = 100.0
        private var _height: Double = 100.0

        fun moveTo(x: Double, y: Double): Builder {
            _commands.add(MoveCommand(x, y))
            return this
        }

        fun lineTo(x: Double, y: Double): Builder {
            _commands.add(LineToCommand(x, y))
            return this
        }

        fun curveTo(x1: Double, y1: Double, x2: Double, y2: Double, x3: Double, y3: Double): Builder {
            _commands.add(BezierCurveCommand(x1, y1, x2, y2, x3, y3))
            return this
        }

        fun fillColor(color: CMYKColor): Builder {
            _fillColor = color
            return this
        }

        fun lineWidth(width: Double): Builder {
            _lineWidth = width
            return this
        }

        fun lineColor(color: CMYKColor): Builder {
            _lineColor = color
            return this
        }

        fun width(width: Double): Builder {
            _width = width
            return this
        }

        fun height(height: Double): Builder {
            _height = height
            return this
        }

        fun build(): Path {
            return Path(
                commands = _commands,
                fillColor = _fillColor,
                lineColor = _lineColor,
                lineWidth = _lineWidth,
                width = _width,
                height = _height
            )
        }
    }

}
