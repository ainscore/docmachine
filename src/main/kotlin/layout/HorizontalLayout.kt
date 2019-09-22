package com.andrewinscore.docmachine.layout

import com.andrewinscore.docmachine.Bounds
import com.andrewinscore.docmachine.Drawable
import com.andrewinscore.docmachine.RenderFunction
import com.andrewinscore.docmachine.RenderedDocument
import com.andrewinscore.docmachine.RenderedDrawable

/**
 * @author ainscore
 */
class HorizontalLayout(val drawables: Sequence<Drawable>) : Drawable {
    override fun draw(renderedDoc: RenderedDocument): RenderedDrawable {

        val renderedDrawables = drawables.map { it.draw(renderedDoc) }

        val width = renderedDrawables.map { it.width }.sum()

        val height = renderedDrawables.map { it.height }.max()

        val draw: RenderFunction = { stream, loc ->
            var prevWidth = 0.0
            renderedDrawables.forEach { renderedDrawable ->
                renderedDrawable.draw(stream, loc.add(prevWidth, 0.0))
                prevWidth += renderedDrawable.width
            }
        }

        return RenderedDrawable(width, height ?: 0.0, draw)
    }

    override fun draw(renderedDoc: RenderedDocument, bounds: Bounds): RenderedDrawable {
        return draw(renderedDoc)
    }
}
