package di

import androidx.fragment.app.Fragment
import org.kodein.di.bindings.WeakContextScope

val fragmentScope = WeakContextScope<Fragment>()