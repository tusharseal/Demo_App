package com.demo.app.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.app.BaseFragment
import com.demo.app.adapter.CartAdapter
import com.demo.app.databinding.FragmentViewCartBinding
import com.demo.app.model.SelectionObject
import com.demo.app.viewmodel.SharedViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewCartFragment : BaseFragment() {

    private var _binding: FragmentViewCartBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCartRv()
        onClick()
        initObserver()
    }

    private fun initCartRv() {
        cartAdapter = CartAdapter(requireContext(), object : CartAdapter.CartSelection {
            override fun cartSelectedForRemove(cartObj: SelectionObject) {
                sharedViewModel.removeFromSelectionObjectList(cartObj)
            }

        })
        binding.cartRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.cartRv.adapter = cartAdapter
    }

    private fun initObserver() {
        sharedViewModel.selectionObjectList.observe(viewLifecycleOwner) { selectionObjList ->
            if (!selectionObjList.isNullOrEmpty()) {
                cartAdapter.submitList(selectionObjList)
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

        binding.checkOutBtn.text = "Checkout ₹${totalPrice}"
    }

    private fun onClick() {
        binding.backFromCartIv.setOnClickListener {
            getMainActivity()?.onBackPressed()
        }

        binding.checkOutBtn.setOnClickListener {
            getMainActivity()?.addFragment(
                true,
                getMainActivity()?.getVisibleFrame()!!,
                CheckoutFragment()
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}