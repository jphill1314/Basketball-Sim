package com.appdev.jphil.basketballcoach.simdialog

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.appdev.jphil.basketballcoach.databinding.DialogSimulatingBinding
class SimDialog : DialogFragment() {

    private lateinit var binding: DialogSimulatingBinding
    private val adapter = SimDialogAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSimulatingBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }


    fun addNewGame(dataModel: SimDialogDataModel) = adapter.addItem(dataModel)
}

