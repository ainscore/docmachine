package com.andrewinscore.docmachine.style

/**
 * @author ainscore
 */
data class CMYKColor(val c: Double, val m: Double, val y: Double, val k: Double)

val BLUE = CMYKColor(.96, .16, .1, 0.0)
val GREEN = CMYKColor(.65, 0.0, .56, 0.0)
val RED = CMYKColor(0.0, .87, .70, 0.0)
val GRAY = CMYKColor(.11, .04, .05, 0.0)
val BLACK = CMYKColor(0.0, 0.0, 0.0, 1.0)
val YELLOW = CMYKColor(0.0, .26, .94, 0.0)
val WHITE = CMYKColor(0.0, 0.0, 0.0, 0.0)
val CLEAR = CMYKColor(1.0, 1.0, 1.0, 1.0)
