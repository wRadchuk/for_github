package com.wradchuk.forgithub.android_game_core

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.wradchuk.forgithub.android_game_core.common.Vector2
import kotlin.math.pow
import kotlin.math.sqrt

class Joystick(
    private var outerCircleRadius: Int,
    private var outerCircleCenterPosition: Vector2<Int>,
    private var innerCircleRadius: Int,
    private var innerCircleCenterPosition: Vector2<Int>,
) {

    var actuatorX: Double = 0.0
        get() = field
        private set
    var actuatorY: Double = 0.0
        get() = field
        private set

    private var joystickCenterToTouchDistance: Double = 0.0
    private var isPressed = false

    private val outerCirclePaint: Paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL_AND_STROKE
    }
    private val innerCirclePaint: Paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL_AND_STROKE
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(
            outerCircleCenterPosition.positionX.toFloat(),
            outerCircleCenterPosition.positionY.toFloat(),
            outerCircleRadius.toFloat(),
            outerCirclePaint
        )

        canvas.drawCircle(
            innerCircleCenterPosition.positionX.toFloat(),
            innerCircleCenterPosition.positionY.toFloat(),
            innerCircleRadius.toFloat(),
            innerCirclePaint
        )
    }

    fun update() {
        updateInnerCirclePosition()
    }

    private fun updateInnerCirclePosition() {
        innerCircleCenterPosition = Vector2(
            (outerCircleCenterPosition.positionX + actuatorX * outerCircleRadius).toInt(),
            (outerCircleCenterPosition.positionY + actuatorY * outerCircleRadius).toInt()
        )
    }

    fun isPressed(touchPositionX: Double, touchPositionY: Double): Boolean {
        joystickCenterToTouchDistance = sqrt(
            (outerCircleCenterPosition.positionX - touchPositionX).pow(2.0) +
                    (outerCircleCenterPosition.positionY - touchPositionY).pow(2.0)
        )
        return joystickCenterToTouchDistance < outerCircleRadius
    }

    fun setIsPressed(isPressed: Boolean) {
        this.isPressed = isPressed
    }

    fun getIsPressed() = isPressed
    fun setActuator(touchPositionX: Double, touchPositionY: Double) {
        val deltaX = touchPositionX - outerCircleCenterPosition.positionX
        val deltaY = touchPositionY - outerCircleCenterPosition.positionY
        val deltaDistance = sqrt(deltaX.pow(2.0) + deltaY.pow(2.0))

        if(deltaDistance < outerCircleRadius) {
            actuatorX = deltaX/outerCircleRadius
            actuatorY = deltaY/outerCircleRadius
        } else {
            actuatorX = deltaX/deltaDistance
            actuatorY = deltaY/deltaDistance
        }
    }

    fun resetActuator() {
        actuatorX = 0.0
        actuatorY = 0.0
    }

}