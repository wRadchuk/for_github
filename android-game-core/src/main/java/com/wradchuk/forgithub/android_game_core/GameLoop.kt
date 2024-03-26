package com.wradchuk.forgithub.android_game_core

import android.graphics.Canvas
import android.util.Log
import android.view.SurfaceHolder

/**
 * Класс, отвечающий за игровой цикл.
 *
 * @param game Игровой объект.
 * @param surfaceHolder Объект [SurfaceHolder] для управления поверхностью.
 */
class GameLoop(
    private val game: Game,
    private val surfaceHolder: SurfaceHolder,
) : Thread() {

    /**
     * Состояние цикла
     */
    private var isRunning = false

    /**
     * Cредний UPS
     */
    var averageUPS: Double = 0.0
        private set

    /**
     * Cредний FPS
     */
    var averageFPS: Double = 0.0
        private set

    /**
     * Метод для запуска игрового цикла
     */
    fun startLoop() {
        Log.d(TAG, "startLoop()")
        isRunning = true
        start()
    }

    /**
     * В методе происходит обработка рендеринга и логики обновления игрового цикла
     */
    override fun run() {
        Log.d(TAG, "run()")
        super.run()

        // Объявление переменных для времени и подсчета циклов
        var updateCount: Int = 0
        var frameCount: Int = 0

        var startTime: Long
        var elapsedTime: Long
        var sleepTime: Long

        // Игровой цикл
        var canvas: Canvas? = null
        startTime = System.currentTimeMillis()
        while (isRunning) {

            // Попытка обновить и отобразить игру
            try {
                canvas = surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    game.update()
                    updateCount++

                    canvas?.let { game.draw(it) }

                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                        frameCount++
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            // Приостановка игрового цикла, чтобы не превысить целевое количество UPS
            elapsedTime = System.currentTimeMillis() - startTime
            sleepTime = (updateCount * UPS_PERIOD - elapsedTime).toLong()
            if (sleepTime > 0) {
                try {
                    sleep(sleepTime)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

            // Пропуск кадров, чтобы держать количество UPS в пределах целевого значения
            while (sleepTime < 0 && updateCount < MAX_UPS - 1) {
                game.update()
                updateCount++

                elapsedTime = System.currentTimeMillis() - startTime
                sleepTime = (updateCount * UPS_PERIOD - elapsedTime).toLong()
            }

            // Расчет среднего UPS и FPS
            elapsedTime = System.currentTimeMillis() - startTime
            if (elapsedTime >= ONE_SECOND) {

                averageUPS = updateCount / (E3_NEGATIVE * elapsedTime)
                averageFPS = frameCount / (E3_NEGATIVE * elapsedTime)

                updateCount = 0
                frameCount = 0
                startTime = System.currentTimeMillis()
            }
        }
    }


    /**
     * Метод для остановки игрового цикла
     */
    fun stopLoop() {
        Log.d(TAG, "stopLoop()")
        isRunning = false
        try {
            join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    /**
     * Константы для игрового цикла
     */
    companion object GameLoopConst {
        private const val TAG: String = "GameLoop.kt"

        private const val ONE_SECOND: Int = 1_000

        /**
         * 1E-3
         */
        private const val E3_NEGATIVE: Double = 1E-3
        /**
         * 1E+3
         */
        private const val E3_POSITIVE: Double = 1E+3
        /**
         * Максимальное количество обновлений в секунду
         */
        const val MAX_UPS: Double = 60.0
        /**
         * Период обновлений в миллисекундах
         */
        private const val UPS_PERIOD: Double = E3_POSITIVE / MAX_UPS
    }
}