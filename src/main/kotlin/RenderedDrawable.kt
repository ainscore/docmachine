package com.andrewinscore.docmachine

import org.apache.pdfbox.pdmodel.PDPageContentStream

typealias RenderFunction = (PDPageContentStream, PdfLoc) -> Unit

class RenderedDrawable(val width: Double, val height: Double, val draw: RenderFunction)
