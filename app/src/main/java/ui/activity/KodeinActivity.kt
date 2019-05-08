package ui.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import org.jetbrains.anko.AnkoLogger
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

abstract class KodeinActivity : AppCompatActivity(), KodeinAware, AnkoLogger, LifecycleOwner {
    override val kodein by kodein()
}