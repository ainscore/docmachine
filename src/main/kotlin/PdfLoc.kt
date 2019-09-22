package com.andrewinscore.docmachine

data class PdfLoc(val x: Double, val y: Double) {
    fun add(xDiff: Double, yDiff: Double): PdfLoc {
        return PdfLoc(x + xDiff, y + yDiff)
    }
}
