package com.demo.app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.demo.app.BaseFragment
import com.demo.app.R
import com.demo.app.databinding.FragmentCheckoutBinding
import com.demo.app.databinding.FragmentHomeBinding
import com.demo.app.model.SelectionObject
import com.demo.app.utils.makeViewGone
import com.demo.app.utils.makeViewVisible
import com.demo.app.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutFragment : BaseFragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        onClick()

    }

    private fun onClick() {
        binding.backFromCheckoutIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }
    }

    private fun initObserver() {
        sharedViewModel.selectionObjectList.observe(viewLifecycleOwner) { selectionObjList ->
            if (!selectionObjList.isNullOrEmpty()) {
                calculateTotal(selectionObjList)
            } else {
                Toast.makeText(requireContext(), "Cart is empty!", Toast.LENGTH_SHORT).show()
                getMainActivity()?.onBackPressed()
            }
        }


    }

    private fun calculateTotal(selectionObjList: List<SelectionObject>) {
        val totalPrice = selectionObjList.sumByDouble { selectionObject ->
            val parentPrice =
                selectionObject.parent?.price?.times(selectionObject.parent!!.quantitySelected)
            val childPrice = selectionObject.child.sumByDouble {
                (it?.price?.times(it.quantitySelected) ?: 0).toDouble()
            }
            (parentPrice?.plus(childPrice) ?: 0) as Double
        }

        binding.itemPriceValue1Tv.text = "₹${totalPrice}"
        binding.itemPriceValue2Tv.text = "₹${totalPrice}"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}