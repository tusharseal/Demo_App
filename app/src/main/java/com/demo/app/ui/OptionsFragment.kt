package com.demo.app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.demo.app.BaseBottomSheet
import com.demo.app.R
import com.demo.app.databinding.FragmentOptionsBinding
import com.demo.app.utils.makeViewGone
import com.demo.app.utils.makeViewVisible
import com.demo.app.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OptionsFragment : BaseBottomSheet() {

    private var _binding: FragmentOptionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var callback: (String) -> Unit
    fun setCallback(callback: (String) -> Unit) {
        this.callback = callback
    }

    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()
        initObserver()

    }

    private fun initObserver() {
        sharedViewModel.selectionObjectList.observe(viewLifecycleOwner) { selectionObjList ->
            if (!selectionObjList.isNullOrEmpty()) {
                val lastElement = selectionObjList.lastOrNull()
                if (lastElement != null) {
                    val parentName = "${lastElement.parent?.name!![0]}"
                    var childrenName = ""
                    lastElement.child.forEach {
                        if (it?.isOptionSelected == true) {
                            childrenName += it.name!![0] + "(${it.quantitySelected}),"
                        }
                    }

                    binding.optionValueTv.text = "${parentName}, ${childrenName}"
                }
            }
        }

    }

    private fun onClick() {
        binding.customizeBtn.setOnClickListener {
            dialog?.dismiss()
            callback.invoke("CUSTOMIZE")
        }

        binding.repeatBtn.setOnClickListener {
            dialog?.dismiss()
            callback.invoke("REPEAT")
        }

        binding.closeOptionIv.setOnClickListener {
            dialog?.dismiss()
            callback.invoke("EXIT")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}