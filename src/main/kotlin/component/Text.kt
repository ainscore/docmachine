package com.andrewinscore.docmachine.component

import com.andrewinscore.docmachine.Bounds
import com.andrewinscore.docmachine.Drawable
import com.andrewinscore.docmachine.RenderFunction
import com.andrewinscore.docmachine.RenderedDocument
import com.andrewinscore.docmachine.RenderedDrawable
import com.andrewinscore.docmachine.style.CMYKColor
import com.andrewinscore.docmachine.style.FontStyle
import org.apache.pdfbox.pdmodel.font.PDFont

import java.io.IOException

import com.andrewinscore.docmachine.style.BLACK

enum class HorzAlignment {
    LEFT,
    CENTER,
    RIGHT,
    JUSTIFY
}

enum class VertAlignment {
    TOP,
    CENTER,
    BOTTOM
}

class Text(
        val text:String,
        var width:Double=Double.POSITIVE_INFINITY,
        val height:Double=Double.POSITIVE_INFINITY,
        val fontSize:Double=10.0,
        val lineHeight:Double=10.0,
        val fontColor:CMYKColor=BLACK,
        val fontStyle:FontStyle=FontStyle.NORMAL,
        val xAlignment:HorzAlignment=HorzAlignment.LEFT,
        val yAlignment:VertAlignment=VertAlignment.BOTTOM,
        val characterSpacing:Double=0.0
): Drawable {

    private fun getAscent(pdFont: PDFont): Double {
        return pdFont.fontDescriptor.ascent.toDouble() / 1000.0 * fontSize
    }

    override fun draw(renderedDoc: RenderedDocument): RenderedDrawable {
        val pdFont = renderedDoc.fonts.getOrElse(fontStyle) { throw Exception("No font for style: %s".format(fontStyle))}
        val getStringWidth:(String) -> Double = { string ->
            pdFont.getStringWidth(string) / 1000.0 * fontSize + characterSpacing / 1000 * fontSize * (string.length - 1)
        }

        val positionedLines: List<List<WordPosition>>

        var height = 0.0

        if (java.lang.Double.isInfinite(width)) {
            width = text.split("\n".toRegex()).map { string -> getStringWidth(string) }.max() ?: 0.0
        }

        val yOffset: Double

        when (yAlignment) {
            VertAlignment.CENTER -> yOffset = getAscent(pdFont) + lineHeight / 2 - getAscent(pdFont) / 2
            VertAlignment.TOP -> yOffset = (getAscent(pdFont) + getAscent(pdFont)).toDouble()
            else -> yOffset = getAscent(pdFont).toDouble()
        }

        if (xAlignment == HorzAlignment.JUSTIFY) {
            val justifiedText = JustifiedText(getStringWidth, text, width)
            positionedLines = justifiedText.getText()
        } else {
            positionedLines = text.split("\n".toRegex()).flatMap {line ->
                var newLines = emptyList<String>()
                var currentLine = emptyList<String>()
                line.split(" ".toRegex()).forEach {word ->
                    val testLine = currentLine + word
                    if (getStringWidth(testLine.joinToString(" ")) <= width) {
                        currentLine = testLine
                    } else {
                        newLines = newLines + currentLine.joinToString(" ")
                        currentLine = listOf(word)
                    }
                }
                newLines = newLines + currentLine.joinToString(" ")
                newLines
            }.map { line ->
                var xPos = 0.0
                when (xAlignment) {
                    HorzAlignment.CENTER -> xPos += (width / 2.0 - getStringWidth(line) / 2.0)
                    HorzAlignment.RIGHT -> xPos += (width - getStringWidth(line))
                    else -> xPos = xPos
                }

                listOf(WordPosition(line, xPos))
            }
            height = (positionedLines.size * lineHeight.toFloat()).toDouble()
        }

        val finalPositionedLines = positionedLines.toMutableList()

        val draw:RenderFunction = { stream, loc ->
            val xPos = loc.x
            val yPos = loc.y - yOffset
            stream.beginText()
            stream.setStrokingColor(fontColor.c.toFloat(), fontColor.m.toFloat(), fontColor.y.toFloat(), fontColor.k.toFloat())
            stream.setNonStrokingColor(fontColor.c.toFloat(), fontColor.m.toFloat(), fontColor.y.toFloat(), fontColor.k.toFloat())
            stream.setCharacterSpacing((characterSpacing / 1000 * fontSize).toFloat())
            stream.setFont(pdFont, fontSize.toFloat())
            stream.setLeading(lineHeight.toFloat())
            stream.newLineAtOffset(xPos.toFloat(), yPos.toFloat())
            for (line in finalPositionedLines) {
                val commands = line.flatMap { wordPosition -> listOf((-(wordPosition.offset / fontSize * 1000)).toFloat(), wordPosition.word) }.toTypedArray()
                try {
                    stream.showTextWithPositioning(commands)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }

                stream.newLine()
            }
            stream.endText()
        }

        if (java.lang.Double.isInfinite(height)) {
            println(String.format("Infinite text: %s", text))
        }

        return RenderedDrawable(width, height, draw)
    }

    override fun draw(renderedDoc: RenderedDocument, bounds: Bounds): RenderedDrawable {
        return draw(renderedDoc)
    }
}

data class WordPosition (val word: String, val offset: Double)

class JustifiedText(val getStringWidth: (String) -> Double, val text: String, val width:Double) {

    val parentMap = HashMap<Int, Int>()
    val memoMap = HashMap<Int, Double>()

    fun getText(): List<List<WordPosition>> {
        val originalLines = text.split("\n".toRegex())

        return originalLines.flatMap { originalLine ->
            if (originalLine.isEmpty()) {
                return listOf()
            }

            val lines = getJustifiedLines(originalLine, getStringWidth)
            return lines.mapIndexed { index, words ->
                val NUM_WORDS = words.size
                val wordsWidth = getStringWidth(words.joinToString(""))
                val spaceWidth = (width - wordsWidth) / (NUM_WORDS - 1)
                val xPos = 0.0
                if (lines.indexOf(words) == lines.size - 1) {
                    listOf(WordPosition(words.joinToString(" "), xPos))
                } else {
                    words.map { word -> WordPosition(word, offset=if (words.indexOf(word) == 0) 0.0 else spaceWidth) }
                }
            }
        }
    }

    private fun getJustifiedLines(originalLine: String, getWidth: (String) -> Double): List<List<String>> {
        val words = originalLine.split(" ".toRegex())

        getBadnessRec(getWidth, 0, words)

        val lines = mutableListOf<List<String>>()

        var start = 0
        while (true) {
            if (!parentMap.containsKey(start))
                break
            lines.add(words.subList(start, parentMap[start]!!))
            start = parentMap[start]!!
        }

        return lines

    }

    private fun getBadnessRec(getStringWidth: (String) -> Double, start: Int, words: List<String>): Double {
        return memoMap.computeIfAbsent(start) { start ->
            var badness = java.lang.Double.POSITIVE_INFINITY
            var lowValue = start + 1
            if (getStringWidth(words.subList(start, words.size).joinToString(" ")) < width) {
                memoMap[start] = 0.0
                parentMap[start] = words.size
                0.0
            } else {
                for (j in start + 1 until words.size - 2) {
                    val currentLine = words.subList(start, j)
                    val wordsWidth = getStringWidth(currentLine.joinToString(" "))
                    if (wordsWidth < width) {
                        val lineWidth = getStringWidth(currentLine.joinToString(" "))

                        val newBadness = Math.pow(width - lineWidth, 3.0) + getBadnessRec(getStringWidth, j + 1, words)
                        if (newBadness < badness) {
                            badness = newBadness
                            lowValue = j
                        }
                    } else {
                        break
                    }
                }
                parentMap[start] = lowValue
                badness
            }
        }
    }

}
