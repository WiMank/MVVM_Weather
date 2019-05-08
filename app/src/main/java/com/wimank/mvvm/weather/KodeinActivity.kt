package com.wimank.mvvm.weather

import androidx.appcompat.app.AppCompatActivity
import di.KodeinApp
import org.jetbrains.anko.AnkoLogger
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

abstract class KodeinActivity : AppCompatActivity(), KodeinAware, AnkoLogger {
    override val kodein: Kodein by lazy { (applicationContext as KodeinApp).kodein }
}