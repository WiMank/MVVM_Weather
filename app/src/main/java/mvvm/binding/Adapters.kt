package mvvm.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter


@BindingAdapter("android:bindSrc")
fun setImageViewResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}