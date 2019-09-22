package com.andrewinscore.docmachine

/**
 * @author ainscore
 */
interface Drawable {

    fun draw(renderedDoc: RenderedDocument): RenderedDrawable

    fun draw(renderedDoc: RenderedDocument, bounds: Bounds): RenderedDrawable

}
