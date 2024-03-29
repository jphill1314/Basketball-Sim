package com.appdev.jphil.basketballcoach.compose.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.main.NavigationManager
import com.appdev.jphil.basketballcoach.theme.appDarkColors
import com.appdev.jphil.basketballcoach.theme.appLightColors
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
                    colors = if (isSystemInDarkTheme()) appDarkColors else appLightColors
                ) {
                    SetContent()
                }
            }
        }
    }

    @Composable
    abstract fun SetContent()

    protected fun setToolbarTitle(text: String) {
        requireActivity().findViewById<Toolbar>(R.id.toolbar).title = text
    }
}
