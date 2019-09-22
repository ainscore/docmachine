package com.andrewinscore.docmachine

import com.andrewinscore.docmachine.component.Text
import org.junit.Test as test

class DocumentTest() {
    @test
    fun test() {
        var paragraph = Text(width=612.0, text="Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
        var page = Page()
        page.addDrawable(paragraph, 0.0, 0.0)
        var section = Section(listOf(page))
        val doc = Document(fonts=emptyMap(), sections=listOf(section))
        doc.writeToFile("test.pdf")
    }
}