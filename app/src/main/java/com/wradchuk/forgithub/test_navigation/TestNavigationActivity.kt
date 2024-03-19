package com.wradchuk.forgithub.test_navigation

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.wradchuk.forgithub.R
import com.wradchuk.forgithub.custom_navigation.CustomNavigator
import com.wradchuk.forgithub.custom_navigation.navigator.IBackPressActivity

class TestNavigationActivity : AppCompatActivity(), IBackPressActivity {

    val customNavigator: CustomNavigator = CustomNavigator(
        containerID = R.id.fragmentContainerView,
        fragmentManager = supportFragmentManager,
        backPressActivity = this
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_test_navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.testNavigation)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        onBackPressedDispatcher.addCallback(this, customNavigator.getBackPressCallback())

        findViewById<Button>(R.id.addFragment).setOnClickListener {
            customNavigator.addScreen(
                ForTestNavigationFragment.newInstance("Был добавлен")
            )
        }

        findViewById<Button>(R.id.replaceFragment).setOnClickListener {
            customNavigator.replaceScreen(
                ForTestNavigationFragment.newInstance("Добавленный последний был заменён")
            )
        }

        findViewById<Button>(R.id.addRootScreen).setOnClickListener {
            customNavigator.addRootScreen(
                ForTestNavigationFragment.newInstance("Добавил как новый и удалил все другие")
            )
        }

        findViewById<Button>(R.id.backTo).setOnClickListener {
            if (customNavigator.getScreenBackStack().isNotEmpty()) {
                customNavigator.backTo(customNavigator.getScreenBackStack().firstElement())
            }

        }

        findViewById<Button>(R.id.back).setOnClickListener {
            customNavigator.backPress()
        }

        findViewById<Button>(R.id.exit).setOnClickListener {
            customNavigator.exit()
        }


    }

    override fun iBackPressActivityExitCommand() {
        finish()
    }

}