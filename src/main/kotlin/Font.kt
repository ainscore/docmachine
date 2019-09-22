package com.andrewinscore.docmachine

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.font.PDType1Font

import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

interface Font {
    fun addFontToDocument(pdDocument: PDDocument): PDFont
}

class TTFFont private constructor(val fontPath: String) : Font {

    override fun addFontToDocument(pdDocument: PDDocument): PDFont {
        return PDType0Font.load(pdDocument, ByteArrayInputStream(loadFontFile(fontPath)))
    }

    companion object {

        fun fromFile(filePath: String): Font {
            return TTFFont(filePath)
        }

        private fun loadFontFile(fontFile: String): ByteArray {
            try {
                return Files.readAllBytes(Paths.get(fontFile))
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

        }
    }
}

class BuiltinFont(val font: PDType1Font) : Font {
    override fun addFontToDocument(pdDocument: PDDocument): PDFont {
        return font
    }
}
