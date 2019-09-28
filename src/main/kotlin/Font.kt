package com.andrewinscore.docmachine

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.font.PDType1Font

import java.io.File
import java.io.InputStream

interface Font {
    fun addFontToDocument(pdDocument: PDDocument): PDFont
}

class TTFFont(val fontStream: InputStream) : Font {

    override fun addFontToDocument(pdDocument: PDDocument): PDFont {
        return PDType0Font.load(pdDocument, fontStream)
    }

    companion object {
        fun fromFile(fontFile: String): TTFFont {
            return TTFFont(File(fontFile).inputStream())
        }
    }
}

class BuiltinFont(val font: PDType1Font) : Font {
    override fun addFontToDocument(pdDocument: PDDocument): PDFont {
        return font
    }
}
