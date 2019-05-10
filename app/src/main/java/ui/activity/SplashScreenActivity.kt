package ui.activity

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wimank.mvvm.weather.R
import org.jetbrains.anko.startActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import utils.GPS


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splas_screen)
        checkPermission()
    }

    @AfterPermissionGranted(GPS)
    private fun checkPermission() {
        val perms = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            startActivity<MainActivity>()
            finish()
        } else {
            startActivity<PermissionActivity>()
            finish()
        }
    }
}
