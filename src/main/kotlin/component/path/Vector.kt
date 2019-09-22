package com.andrewinscore.docmachine.component.path

/**
 * @author ainscore
 */
class Vector(val x: Double, val y: Double) {

    val r: Double
        get() = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0))

    val theta: Double
        get() = Math.atan2(y, x)

    fun addTheta(thetaDelta: Double): Vector {
        val newTheta = theta + thetaDelta
        return Vector(r * Math.cos(newTheta), r * Math.sin(newTheta))
    }

    fun addR(rDelta: Double): Vector {
        val newR = theta + rDelta
        return Vector(newR * Math.cos(theta), newR * Math.sin(theta))
    }

    operator fun minus(delta: Vector): Vector {
        return Vector(x - delta.x, y - delta.y)
    }

    companion object {
        fun fromPolar(r: Double, theta: Double): Vector {
            return Vector(r * Math.cos(theta), r * Math.sin(theta))
        }
    }
}
