package com.appdev.jphil.basketballcoach.compose.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.appdev.jphil.basketballcoach.main.NavigationManager
import com.appdev.jphil.basketballcoach.theme.lightColors
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class ComposeFragment : Fragment() {

    protected abstract val presenter: BasicComposePresenter<*>

    @Inject
    protected lateinit var navManager: NavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)

        lifecycle.addObserver(presenter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme(
                    colors = lightColors
                ) {
                    SetContent()
                }
            }
        }
    }

    @Composable
    abstract fun SetContent()
}
