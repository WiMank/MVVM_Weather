package ui.fragment

import androidx.fragment.app.Fragment
import org.jetbrains.anko.AnkoLogger
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class KodeinFragment : Fragment(), KodeinAware, AnkoLogger {

    override val kodein by kodein()

}
