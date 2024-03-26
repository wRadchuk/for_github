package com.wradchuk.forgithub.android_game_core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import com.wradchuk.forgithub.android_game_core.common.Vector2

/**
 * Управляет всеми объектами в игре
 */
class Game(
    private val context: Context,
): SurfaceView(context), SurfaceHolder.Callback {

    private var gameLoop: GameLoop? = GameLoop(this, holder)

    private val joystick = Joystick(
        outerCircleRadius = 40,
        outerCircleCenterPosition = Vector2(80, context.resources.displayMetrics.heightPixels-120),
        innerCircleRadius = 25,
        innerCircleCenterPosition =  Vector2(80, context.resources.displayMetrics.heightPixels-120),

    )
    private val player = Player(
        position = Vector2(
            (context.resources.displayMetrics.widthPixels/2).toDouble(),
            (context.resources.displayMetrics.heightPixels/2).toDouble()
        ),
        radius = 20.0,
        color = ContextCompat.getColor(context, R.color.player)
    )

    private val paint = Paint()

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event == null) return false

        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                if(joystick.isPressed(event.x.toDouble(), event.y.toDouble())) {
                    joystick.setIsPressed(true)
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if(joystick.getIsPressed()) {
                    joystick.setActuator(event.x.toDouble(), event.y.toDouble())
                }

                return true
            }
            MotionEvent.ACTION_UP -> {
                joystick.setIsPressed(false)
                joystick.resetActuator()
            }
        }

        return super.onTouchEvent(event)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if(gameLoop?.state != null && gameLoop?.state!! == Thread.State.TERMINATED) {
            gameLoop = GameLoop(this, holder)
        }
        gameLoop?.startLoop()

    }
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceDestroyed(holder: SurfaceHolder) {}

    private val bitmap = Bitmap.createBitmap(
        context.resources.displayMetrics.widthPixels,
        context.resources.displayMetrics.heightPixels,
        Bitmap.Config.ARGB_8888
    )

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        bitmap.eraseColor(Color.DKGRAY)

        val mCanvas = Canvas(bitmap)
        mCanvas.drawColor(Color.DKGRAY)
        drawFPS(mCanvas)
        player.draw(mCanvas)
        joystick.draw(mCanvas)

        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }
    private fun drawFPS(canvas: Canvas) {
        val averageFPS = gameLoop?.averageFPS?.toInt().toString()
        val color = ContextCompat.getColor(context, R.color.magenta)
        paint.color = color
        paint.textSize = 30f
        canvas.drawText("FPS: $averageFPS", 10f, 30f, paint)
    }

    fun update() {
        joystick.update()
        player.update(joystick)
    }


    init {
        // Для реакции на игровой ввод
        holder.addCallback(this)
    }
}