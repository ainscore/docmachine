package com.andrewinscore.docmachine

import com.andrewinscore.docmachine.style.FontStyle
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PageMode
import org.apache.pdfbox.pdmodel.common.PDPageLabelRange
import org.apache.pdfbox.pdmodel.common.PDPageLabels
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitWidthDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem

class Document(val fonts: Map<FontStyle, Font>, val sections: List<Section>) {
    private var _author: String? = null
    private var _title: String? = null

    fun setAuthor(author: String) {
        this._author = author
    }

    fun setTitle(title: String) {
        this._title = title
    }

    fun writeToFile(fileName: String) {

        val pdDocument = PDDocument()

        val pageLabels = PDPageLabels(pdDocument)

        val outline = PDDocumentOutline()
        pdDocument.documentCatalog.documentOutline = outline

        val defaults: Map<FontStyle, Font> = mapOf(
                FontStyle.NORMAL to BuiltinFont(PDType1Font.HELVETICA),
                FontStyle.BOLD to BuiltinFont(PDType1Font.HELVETICA_BOLD),
                FontStyle.ITALIC to BuiltinFont(PDType1Font.HELVETICA_OBLIQUE),
                FontStyle.BOLD_ITALIC to BuiltinFont(PDType1Font.HELVETICA_BOLD_OBLIQUE)
        )

        val fontsWithDefaults = defaults + fonts

        val renderedDoc = RenderedDocument(fontsWithDefaults.mapValues { font -> font.value.addFontToDocument(pdDocument) }, pdDocument)

        val currentPageNumber = 0
        for (section in sections) {
            val sectionPages = PDPageLabelRange()
            sectionPages.style = PDPageLabelRange.STYLE_ROMAN_LOWER
            pageLabels.setLabelItem(currentPageNumber, sectionPages)

            for (i in section.pages.indices) {
                val page = section.pages[i]
                val outlineItems = mutableListOf<PDOutlineItem>()
                for (j in page._outlineItems.indices) {
                    val outlineItem = PDOutlineItem()
                    outlineItem.title = page._outlineItems[j]
                    val dest = PDPageFitWidthDestination()
                    dest.pageNumber = i
                    outlineItem.destination = dest
                    outlineItems += outlineItem
                }
                outlineItems.forEach(outline::addLast)

                page.draw(renderedDoc)
            }
        }

        outline.openNode()

        pdDocument.documentCatalog.pageLabels = pageLabels
        pdDocument.documentCatalog.pageMode = PageMode.USE_OUTLINES

        pdDocument.documentInformation.author = _author
        pdDocument.documentInformation.title = _title

        try {
            pdDocument.save(fileName)
            pdDocument.close()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}