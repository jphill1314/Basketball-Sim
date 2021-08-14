package com.appdev.jphil.basketballcoach.simdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.appdev.jphil.basketballcoach.databinding.DialogSimulatingBinding

class SimDialog : DialogFragment() {

    private lateinit var binding: DialogSimulatingBinding
    private val adapter = SimDialogAdapter()
    var onDialogDismissed: () -> Unit = { }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSimulatingBinding.inflate(inflater, container, false)
        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
            cancelButton.setOnClickListener {
                onDialogDismissed()
                binding.cancelButton.isEnabled = false
            }
        }
        return binding.root
    }

    fun setState(state: SimDialogState) {
        state.games.observe(
            this,
            Observer { dataModels ->
                adapter.updateItems(dataModels)
            }
        )
        state.text.observe(
            this,
            Observer { text ->
                binding.simText.text = text
            }
        )
        state.canCancel.observe(
            this,
            Observer { canCancel ->
                binding.cancelButton.isEnabled = canCancel
            }
        )
    }
}
