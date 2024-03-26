package com.wradchuk.forgithub.android_game_core

import android.graphics.Canvas
import android.graphics.Paint
import com.wradchuk.forgithub.android_game_core.common.Vector2

class Player(
    private val position: Vector2<Double>,
    private val radius: Double,
    private val color: Int,
) {

    private val paint: Paint = Paint().apply {
        color = this@Player.color
    }

    private var cord: Vector2<Double> = position


    private var velocityX: Double = 0.0
    private var velocityY: Double = 0.0

    fun draw(canvas: Canvas) {
        canvas.drawCircle(cord.positionX.toFloat(), cord.positionY.toFloat(), radius.toFloat(), paint)
    }

    fun update(joystick: Joystick) {
        velocityX = joystick.actuatorX * MAX_SPEED
        velocityY = joystick.actuatorY * MAX_SPEED
        this.cord.positionX += velocityX
        this.cord.positionY += velocityY
    }

    companion object {
        private const val SPEED_PIXELS_PER_SECOND = 400.0
        private const val MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS
    }

}


