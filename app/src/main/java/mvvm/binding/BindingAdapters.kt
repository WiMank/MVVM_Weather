package mvvm.binding

import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import java.util.logging.Logger


@BindingAdapter("android:bindSrc")
fun bindSrc(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}

@BindingAdapter("android:queryTextListener")
fun setOnQueryTextListener(searchView: SearchView, listener: SearchView.OnQueryTextListener) {
    searchView.setOnQueryTextListener(listener)
}


@BindingAdapter("android:collapse")
fun collapse(searchView: SearchView, collapse: Boolean) {
    log.info("collapse $collapse")
    searchView.setQuery("", false)
    searchView.clearFocus()
    searchView.isIconified = true

}

private val log = Logger.getLogger("BINDING")