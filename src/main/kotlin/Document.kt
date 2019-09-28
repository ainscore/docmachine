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

class Document(
    val fonts: Map<FontStyle, Font>,
    val sections: List<Section>,
    val title: String = "",
    val author: String = ""
) {
    fun writeToFile(fileName: String) {

        val pdDocument = PDDocument()
        pdDocument.documentId = 12345 // Keep doc content stable across runs

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

        val renderedDoc = RenderedDocument(
            fontsWithDefaults.mapValues { font -> font.value.addFontToDocument(pdDocument) },
            pdDocument
        )

        val currentPageNumber = 0
        sections.forEach { section ->
            val sectionPages = PDPageLabelRange()
            sectionPages.style = PDPageLabelRange.STYLE_ROMAN_LOWER
            pageLabels.setLabelItem(currentPageNumber, sectionPages)

            section.pages.forEachIndexed { i, page ->
                val outlineItems = mutableListOf<PDOutlineItem>()
                page.outlineItems.forEachIndexed { j, outlineItem ->
                    val pdOutlineItem = PDOutlineItem()
                    pdOutlineItem.title = outlineItem
                    val dest = PDPageFitWidthDestination()
                    dest.pageNumber = i
                    pdOutlineItem.destination = dest
                    outlineItems += pdOutlineItem
                }
                outlineItems.forEach(outline::addLast)

                page.draw(renderedDoc)
            }
        }

        outline.openNode()

        pdDocument.documentCatalog.pageLabels = pageLabels
        pdDocument.documentCatalog.pageMode = PageMode.USE_OUTLINES

        pdDocument.documentInformation.author = author
        pdDocument.documentInformation.title = title

        pdDocument.save(fileName)
        pdDocument.close()
    }

}