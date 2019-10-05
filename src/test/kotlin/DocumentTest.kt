package com.andrewinscore.docmachine

import com.andrewinscore.docmachine.component.HorizontalAlignment
import com.andrewinscore.docmachine.component.Rectangle
import com.andrewinscore.docmachine.component.Text
import com.andrewinscore.docmachine.component.path.Path
import com.andrewinscore.docmachine.layout.HorizontalLayout
import com.andrewinscore.docmachine.layout.VerticalLayout
import com.andrewinscore.docmachine.style.BLUE
import com.andrewinscore.docmachine.style.RED
import com.andrewinscore.docmachine.style.WHITE
import java.nio.file.Files
import java.nio.file.Paths
import org.junit.Test
import java.nio.file.StandardOpenOption

val DOC_ROOT = Paths.get("src/test/test_docs")

class DocumentTest {
    @Test
    fun testJustifiedText() {
        val paragraph = Text(
            width = 612.0,
            xAlignment = HorizontalAlignment.JUSTIFY,
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        )
        val page = Page(pageDrawables = listOf(PageDrawable(drawable = paragraph)))
        val section = Section(listOf(page))
        val doc = Document(fonts = emptyMap(), sections = listOf(section))
        doc.writeToStream(Files.newOutputStream(DOC_ROOT.resolve("test_justified.pdf")))
    }

    @Test
    fun testVerticalLayout() {
        val redRectangle = Rectangle(
            width = 200.0,
            height = 40.0,
            fillColor = RED
        )
        val whiteRectangle = Rectangle(
            width = 200.0,
            height = 40.0,
            fillColor = WHITE
        )
        val blueRectangle = Rectangle(
            width = 200.0,
            height = 40.0,
            fillColor = BLUE
        )
        val flag = VerticalLayout(listOf(redRectangle, whiteRectangle, blueRectangle))
        val page = Page(pageDrawables = listOf(PageDrawable(drawable = flag)))
        val section = Section(listOf(page))
        val doc = Document(fonts = emptyMap(), sections = listOf(section))
        doc.writeToStream(Files.newOutputStream(DOC_ROOT.resolve("test_vertical_layout.pdf")))
    }

    @Test
    fun testHorizontalLayout() {
        val redRectangle = Rectangle(
            width = 66.0,
            height = 120.0,
            fillColor = RED
        )
        val whiteRectangle = Rectangle(
            width = 66.0,
            height = 120.0,
            fillColor = WHITE
        )
        val blueRectangle = Rectangle(
            width = 66.0,
            height = 120.0,
            fillColor = BLUE
        )
        val flag = HorizontalLayout(listOf(redRectangle, whiteRectangle, blueRectangle))
        val page = Page(pageDrawables = listOf(PageDrawable(drawable = flag)))
        val section = Section(listOf(page))
        val doc = Document(fonts = emptyMap(), sections = listOf(section))
        doc.writeToStream(Files.newOutputStream(DOC_ROOT.resolve("test_horizontal_layout.pdf")))
    }

    @Test
    fun testTriangle() {
        val path = Path.newBuilder()
            .moveTo(20.0, 20.0)
            .lineTo(40.0, 0.0)
            .lineTo(0.0, 0.0)
            .lineTo(20.0, 20.0)
            .width(40.0)
            .height(20.0)
            .fillColor(RED)
            .build()

        val page = Page(pageDrawables = listOf(PageDrawable(drawable = path)))
        val section = Section(listOf(page))
        val doc = Document(fonts = emptyMap(), sections = listOf(section))
        doc.writeToStream(Files.newOutputStream(DOC_ROOT.resolve("test_path.pdf")))
    }
}