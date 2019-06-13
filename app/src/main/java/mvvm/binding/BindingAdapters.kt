package mvvm.binding

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.interpolator.view.animation.FastOutSlowInInterpolator


@BindingAdapter("android:bindSrc")
fun bindSrc(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}


private val INTERPOLATOR = FastOutSlowInInterpolator()
@BindingAdapter("android:alphaAnim")
fun alphaAnim(view: View, go: Boolean) {
    val animation = view.animation
    when {
        go -> {
            view.startAnimation(createAnimation())
            view.animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    view.visibility = View.INVISIBLE
                }

                override fun onAnimationStart(animation: Animation?) {}
            })
        }
        animation != null -> {
            animation.cancel()
            view.visibility = View.VISIBLE
        }
        else -> view.visibility = View.VISIBLE
    }
}


private fun createAnimation(): Animation {
    val anim = AlphaAnimation(1.0f, 0.0f)
    anim.interpolator = INTERPOLATOR
    anim.duration = 10000
    return anim
}