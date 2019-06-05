package mvvm.binding

import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter


@BindingAdapter("android:bindSrc")
fun bindSrc(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}

@BindingAdapter("android:queryTextListener")
fun setOnQueryTextListener(searchView: SearchView, listener: SearchView.OnQueryTextListener) {
    searchView.setOnQueryTextListener(listener)
}