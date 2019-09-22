package com.andrewinscore.docmachine.component

import com.andrewinscore.docmachine.Bounds
import com.andrewinscore.docmachine.Drawable
import com.andrewinscore.docmachine.RenderFunction
import com.andrewinscore.docmachine.RenderedDocument
import com.andrewinscore.docmachine.RenderedDrawable
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject

/**
 * @author ainscore
 */
class Image(val imageBytes: ByteArray, val width: Double, val height: Double) : Drawable {

    override fun draw(renderedDoc: RenderedDocument): RenderedDrawable {
        val draw: RenderFunction = { stream, loc ->
            stream.drawImage(
                PDImageXObject.createFromByteArray(renderedDoc.document, imageBytes, null),
                loc.x.toFloat(),
                (loc.y - height).toFloat(),
                width.toFloat(),
                height.toFloat()
            )
        }

        return RenderedDrawable(width, height, draw)
    }

    override fun draw(renderedDoc: RenderedDocument, bounds: Bounds): RenderedDrawable {
        return draw(renderedDoc)
    }
}
