package com.andrewinscore.docmachine

import com.andrewinscore.docmachine.style.FontStyle
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont

data class RenderedDocument (val fonts: Map<FontStyle, PDFont>, val document: PDDocument)
