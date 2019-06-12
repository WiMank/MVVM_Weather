package ui.fragment

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import org.jetbrains.anko.AnkoLogger
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein


abstract class KodeinFragment : Fragment(), KodeinAware, AnkoLogger {
    override val kodein by kodein()

    fun hideSoftInput(activity: Activity) {
        val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val windowToken = activity.currentFocus
        if (windowToken != null) {
            inputManager.hideSoftInputFromWindow(windowToken.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}

