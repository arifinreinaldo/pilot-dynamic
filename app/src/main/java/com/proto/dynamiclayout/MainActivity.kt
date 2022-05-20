package com.proto.dynamiclayout

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.viewbinding.ViewBinding
import com.proto.dynamiclayout.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val main = "Main"
            supportFragmentManager.commit {
                add(R.id.fragment, DynamicFragment(main))
                addToBackStack(main)
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
        supportFragmentManager.popBackStack()
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