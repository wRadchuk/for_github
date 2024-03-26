package com.wradchuk.forgithub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wradchuk.forgithub.android_game_core.Game


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Game(this))
    }
}