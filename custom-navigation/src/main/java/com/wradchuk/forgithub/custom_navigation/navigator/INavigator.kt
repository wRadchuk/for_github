package com.wradchuk.forgithub.custom_navigation.navigator

import androidx.fragment.app.Fragment

/**
 * Содержит список команд доступных нашей навигации
 */
interface INavigator {

    /**
     * Добавляет экран в список и делает переход на него
     */
    fun addScreen(screen: Fragment)

    /**
     * Меняет текущий экран списка на указанный
     */
    fun replaceScreen(screen: Fragment)

    /**
     * Чистит список экранов и добавляет указанный экран в список с переходом на него
     */
    fun addRootScreen(screen: Fragment)

    /**
     * Делает возврат к указанному экрану и удаляет все экраны добавленные после него
     */
    fun backTo(screen: Fragment)

    /**
     * Удаляет открытый экран из списка и делает возврат к предыдущему экрану списка
     */
    fun backPress()

    /**
     * Чистит список экранов
     */
    fun exit()

}