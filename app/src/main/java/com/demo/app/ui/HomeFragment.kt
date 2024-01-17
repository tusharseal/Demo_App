package com.demo.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.demo.app.BaseFragment
import com.demo.app.databinding.FragmentHomeBinding
import com.demo.app.utils.makeViewGone
import com.demo.app.utils.makeViewVisible
import com.demo.app.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()
        initObserver()
    }

    private fun initObserver() {
        sharedViewModel.selectionObjectList.observe(viewLifecycleOwner) { selectionObjList ->
            if (selectionObjList.isNullOrEmpty()) {
                binding.homeQtyLl.makeViewGone()
                binding.homeCustomizeTv.makeViewVisible()
                binding.homeViewCartBtn.makeViewGone()
            } else {
                binding.homeQtyLl.makeViewVisible()
                binding.homeCustomizeTv.makeViewGone()
                binding.homeViewCartBtn.makeViewVisible()


                binding.homeQtyTv.text = "${selectionObjList.size}"
                binding.homeViewCartBtn.text = "View Cart(${selectionObjList.size})"
            }
        }

    }

    private fun onClick() {

        binding.homeViewCartBtn.setOnClickListener {
            getMainActivity()?.addFragment(
                true, getMainActivity()?.getVisibleFrame()!!, ViewCartFragment()
            )
        }

        binding.homeCustomizeTv.setOnClickListener {
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                SpecificationFragment()
            )
        }

        binding.homeQtyLl.setOnClickListener {
            val optionFragment =
                OptionsFragment()

            optionFragment.setCallback { selection ->
                if (selection == "CUSTOMIZE") {
                    getMainActivity()?.addFragment(
                        true,
                        getMainActivity()?.getVisibleFrame()!!,
                        SpecificationFragment()
                    )
                } else if (selection == "REPEAT") {

                    val currentList = sharedViewModel.selectionObjectList.value
                    if (!currentList.isNullOrEmpty()) {
                        val lastElement = currentList.lastOrNull()
                        if (lastElement != null) {
                            sharedViewModel.addToSelectionObjectList(lastElement)
                        }
                    }
                }

            }
            optionFragment.isCancelable = false
            optionFragment.show(childFragmentManager, "option_fragment")
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}