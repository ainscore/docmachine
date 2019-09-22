package com.andrewinscore.docmachine

import com.andrewinscore.docmachine.component.HorzAlignment
import com.andrewinscore.docmachine.component.Text
import org.junit.Test as test

class DocumentTest() {
    @test
    fun test() {
        val paragraph = Text(
            width = 612.0,
            xAlignment = HorzAlignment.JUSTIFY,
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        )
        val page = Page(pageDrawables = listOf(PageDrawable(drawable = paragraph)))
        val section = Section(listOf(page))
        val doc = Document(fonts = emptyMap(), sections = listOf(section))
        doc.writeToFile("test.pdf")
    }
}