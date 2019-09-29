package com.andrewinscore.docmachine.layout

import com.andrewinscore.docmachine.Bounds
import com.andrewinscore.docmachine.RenderFunction
import com.andrewinscore.docmachine.RenderedDocument
import com.andrewinscore.docmachine.RenderedDrawable
import com.andrewinscore.docmachine.Drawable

/**
 * @author ainscore
 */
class VerticalLayout(val drawables: List<Drawable>) : Drawable {

    override fun draw(renderedDoc: RenderedDocument): RenderedDrawable {
        val renderedDrawables = drawables.map { it.draw(renderedDoc) }

        val width = renderedDrawables.map { it.width }.max()

        val height = renderedDrawables.map { it.height }.sum()

        val draw: RenderFunction = { stream, loc ->
            var prevHeight = 0.0
            renderedDrawables.forEach { renderedDrawable ->
                renderedDrawable.draw(stream, loc.add(0.0, -prevHeight))
                prevHeight += renderedDrawable.height
            }
        }

        return RenderedDrawable(width ?: 0.0, height, draw)
    }

    override fun draw(renderedDoc: RenderedDocument, bounds: Bounds): RenderedDrawable {
        return draw(renderedDoc)
    }
}
