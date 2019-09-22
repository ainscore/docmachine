package com.andrewinscore.docmachine

data class Section(val pages: List<Page>, val pageNumbering: PageNumbering = PageNumbering.DECIMAL)

enum class PageNumbering {
    DECIMAL,
    ROMAN_LOWER,
    ROMAN_UPPER
}
