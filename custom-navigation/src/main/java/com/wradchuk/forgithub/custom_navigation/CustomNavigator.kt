package com.wradchuk.forgithub.custom_navigation

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.wradchuk.forgithub.custom_navigation.navigator.IBackPressActivity
import com.wradchuk.forgithub.custom_navigation.navigator.INavigator
import java.util.Stack

/**
 * Навигацтор по экранам приложения
 * @param containerID - id FragmentContainerView для размещения фрагментов
 * @param fragmentManager - FragmentManager для осуществления транзакций
 * @param backPressActivity - сомнительный интерфейс для управления логикой backstack
 */
class CustomNavigator(
    private val containerID: Int,
    private val fragmentManager: FragmentManager,
    private val backPressActivity: IBackPressActivity
): INavigator {

    /**
     * Хранит историю открытых экранов
     */
    private val screenStack: Stack<Fragment> = Stack()

    /**
     * Реагирует на нажатие системной кнопки back, переопределяет её поведение
     */
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (screenStack.isNotEmpty()) backPress()
            else backPressActivity.iBackPressActivityExitCommand()
        }
    }

    /**
     * Экземпляр перехватчика логики кнопки back для переопределения её поведения в CustomNavigation
     */
    fun getBackPressCallback() = onBackPressedCallback

    /**
     * Доступ к истории перехода по экранам
     */
    fun getScreenBackStack() = screenStack

    /**
     * Добавить новый экран
     *
     * Поместит новый экран внутрь [FragmentContainerView] и добавит в конец [screenStack]
     *
     * @param screen - инстанс наследников Fragment
     */
    override fun addScreen(screen: Fragment) {
        screenStack.push(screen)
        fragmentManager
            .beginTransaction()
            .add(containerID, screen, screen.tag)
            .commit()
    }

    /**
     * Заменить последний экран на новый
     *
     * Заменит текущий экран внутри [FragmentContainerView] удалит старый и добавит новый
     * в конец [screenStack]
     *
     * Если в [screenStack] нет экранов, то отработает как [addScreen]
     *
     * @param screen - инстанс наследников Fragment
     */
    override fun replaceScreen(screen: Fragment) {
        if(screenStack.isEmpty()) addScreen(screen)
        else {
            screenStack.pop()
            screenStack.push(screen)
            fragmentManager
                .beginTransaction()
                .replace(containerID, screen, screen.tag)
                .commit()
        }

    }

    /**
     * Добавить новый экран и удалить все предыдущие
     *
     * Очистит [screenStack] и добавит новый экран в пустой [screenStack], затем поместит новый
     * экран внутрь [FragmentContainerView]
     *
     *
     * @param screen - инстанс наследников Fragment
     */
    override fun addRootScreen(screen: Fragment) {
        screenStack.clear()
        screenStack.push(screen)
        fragmentManager
            .beginTransaction()
            .add(containerID, screen, screen.tag)
            .commit()
    }

    /**
     * Возврат к любому экрану из стека
     *
     * Очистит в [screenStack] все экраны до указанного и заменит последний экран на
     * указанный в параметрах функции, обновит экран в [FragmentContainerView]
     *
     * Если [screenStack] был пуст или в нём нет указанного в параметрах функции экрана
     * то ничего не произойдёт
     *
     * @param screen - инстанс наследников Fragment
     */
    override fun backTo(screen: Fragment) {
        val indexOf = if(screenStack.isNotEmpty()) screenStack.indexOf(screen) else -1
        if(indexOf != -1) {
            screenStack.removeIf { fragment ->
                screenStack.indexOf(fragment) > indexOf // удаляем из стека все экраны после того на который нужен возврат
            }

            fragmentManager
                .beginTransaction()
                .replace(containerID, screen, screen.tag) // Меняем последний экран на новый
                .commit()
        }

    }

    /**
     * Вернуться назад
     *
     * Если в [screenStack] есть экраны до текущего то сделает возврат к предыдущему экрану,
     * иначе вызовет [exit]
     *
     */
    override fun backPress() {
        if (screenStack.isNotEmpty()) screenStack.pop()

        if(screenStack.isNotEmpty()) {
            fragmentManager
                .beginTransaction()
                .replace(containerID, screenStack.last(), screenStack.last().tag) // меняем последний экран на тот который был до удалённого
                .commit()
        } else backPressActivity.iBackPressActivityExitCommand()

    }

    /**
     * Завершить активность
     *
     * Чистит [screenStack] и вызывает [backPressActivity] для оповещения активности о том
     * что её хотят завершить
     */
    override fun exit() {
        screenStack.clear()
        backPressActivity.iBackPressActivityExitCommand()
    }


}