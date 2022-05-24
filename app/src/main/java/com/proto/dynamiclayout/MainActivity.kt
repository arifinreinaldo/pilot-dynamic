package com.proto.dynamiclayout

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.viewbinding.ViewBinding
import com.proto.dynamiclayout.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false
    private val binding by viewBinding(ActivityMainBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val main = "Main"
            supportFragmentManager.commit {
                add(R.id.fragment, DynamicFragment(main))
//                addToBackStack(main)
            }
        }
    }

    fun nextScreen(screenName: String) {
        supportFragmentManager.commit {
            add(R.id.fragment, DynamicFragment(screenName))
            addToBackStack(screenName)
        }
    }

    fun back() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            confirmExit()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onBackPressed() {
        back()
    }

    private fun confirmExit() {
        if (doubleBackToExitPressedOnce) {
            finish()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    fun popBack() {
        supportFragmentManager.popBackStack("Main", 0)
    }
}

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }