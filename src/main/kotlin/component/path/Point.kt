package com.andrewinscore.docmachine.component.path

/**
 * @author ainscore
 */
data class Point(val x: Double, val y: Double) {

    fun getAngle(center: Point): Double {
        return Math.atan2(y - center.y, x - center.x)
    }

    fun getDist(center: Point): Double {
        return Math.sqrt(Math.pow(x - center.x, 2.0) + Math.pow(y - center.y, 2.0))
    }

    fun rotate(rotation: Double, center: Point) : Point {
        val start = Vector(x - center.x, y - center.y)
        val end = start.addTheta(rotation)
        val trans = end - start
        return Point(x + trans.x, y + trans.y);
    }

    operator fun plus(vector: Vector) : Point {
        return Point(x + vector.x, y + vector.y);
    }

    companion object {
        fun fromPolar(r: Double, theta: Double): Point {
            return Point(r * Math.cos(theta), r * Math.sin(theta))
        }
    }
}
