package com.wimank.mvvm.weather

import android.content.SharedPreferences
import android.os.Bundle
import org.kodein.di.generic.instance

class MainActivity : KodeinActivity() {

    private val mSharedPreferences: SharedPreferences by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}
