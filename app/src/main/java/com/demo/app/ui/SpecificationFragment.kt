package com.demo.app.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.app.BaseFragment
import com.demo.app.adapter.ApartmentAdapter
import com.demo.app.adapter.SpecificationAdapter
import com.demo.app.databinding.FragmentSpecificationBinding
import com.demo.app.model.DataModel
import com.demo.app.model.SelectionObject
import com.demo.app.viewmodel.SharedViewModel
import com.demo.app.viewmodel.SpecificationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpecificationFragment : BaseFragment() {

    private var _binding: FragmentSpecificationBinding? = null
    private val binding get() = _binding!!
    private val specsViewModel by viewModels<SpecificationViewModel>()
    private lateinit var apartmentAdapter: ApartmentAdapter
    private lateinit var specificationAdapter: SpecificationAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpecificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initApartmentRv()
        initSpecificationRv()

        //1
        fetchSpecificationData()

        pushEmptySpecList()
        onClick()
    }

    private fun onClick() {
        binding.cartValueBtn.setOnClickListener {
            val currentData = specsViewModel.sumAndSpecification.value
            if (currentData != null) {
                val parent = currentData.second.first {
                    it?.specificationGroupId == "621da754abb8a52242c181d8"
                }
                currentData.second.remove(parent)
                val selectionObject = SelectionObject(
                    parent = parent,
                    child = currentData.second
                )
                sharedViewModel.addToSelectionObjectList(selectionObject)
                getMainActivity()?.onBackPressed()
            }
        }
    }

    private fun pushEmptySpecList() {
        specsViewModel.pushEmptySpecificationList(mutableListOf())
    }

    private fun initSpecificationRv() {
        specificationAdapter =
            SpecificationAdapter(
                requireContext(),
                object : SpecificationAdapter.SpecificationSelection {
                    override fun specificationSelected(specificationList: MutableList<DataModel.Specification.DataList?>) {
                        specsViewModel.addOrReplaceSpecification(specificationList)
                    }


                })
        binding.specificationSelectionRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.specificationSelectionRv.adapter = specificationAdapter
    }

    private fun initApartmentRv() {

        apartmentAdapter = ApartmentAdapter(object : ApartmentAdapter.ApartmentSelection {
            override fun apartmentSelected(apartment: DataModel.Specification.DataList) {
                pushEmptySpecList()

                specsViewModel.addOrReplaceSpecification(mutableListOf(apartment))

                computeListAccordingToApartment(apartment)
            }

        })
        binding.apartmentSelectionRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.apartmentSelectionRv.adapter = apartmentAdapter

    }

    private fun computeListAccordingToApartment(apartment: DataModel.Specification.DataList) {
        val allData = specsViewModel.dataModel.value
        if (allData != null) {

            val selectedApartmentId = apartment.id

            val servicesList = allData.specifications?.filter {
                it?.modifierId == selectedApartmentId
            }

            setDataToSpecificationRv(servicesList)
        }
    }

    private fun fetchSpecificationData() {
        // Trigger data loading
        specsViewModel.loadDataFromJson(requireContext(), "item_data.json")
    }

    private fun initObserver() {

        specsViewModel.dataModel.observe(viewLifecycleOwner, Observer { dataModel ->
            if (dataModel != null) {
                Log.d("xoxo-->", "initObserver: ----Data Read Successfully----")
                setDataToApartmentRv(dataModel)
            } else {
                Log.d("xoxo-->", "initObserver: ----Data Read Error----")
            }
        })

        specsViewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                Log.d("xoxo-->", "initObserver: ----Loading Started----")
            } else {
                Log.d("xoxo-->", "initObserver: ----Loading Ended----")
            }
        })

        specsViewModel.selectedSpecification.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                val selectedSpecsList = it.filter {
                    it?.isOptionSelected == true
                }.toMutableList()
                specsViewModel.calculateCartValue(selectedSpecsList)
            }

        })

        specsViewModel.sumAndSpecification.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val sum = it.first
                binding.cartValueBtn.text = "Add to Cart- ₹${sum}.00"
            }

        })

        sharedViewModel.selectionObjectList.observe(viewLifecycleOwner) { newList ->

        }

    }

    private fun setDataToApartmentRv(dataModel: DataModel) {

        binding.apartmentSelectionTitleTv.text =
            dataModel.specifications?.get(0)?.name!![0].toString()
        if (dataModel.specifications?.get(0)?.maxRange == 0) {
            binding.apartmentChoiceTitleTv.text = "Choose 1"
        } else {
            binding.apartmentChoiceTitleTv.text =
                "Choose up to ${dataModel.specifications?.get(0)?.maxRange}"
        }

        apartmentAdapter.submitList(dataModel.specifications?.get(0)?.list)

        val selectedApartment = dataModel.specifications?.get(0)?.list?.find {
            it?.isDefaultSelected == true
        }
        val selectedApartmentId = selectedApartment?.id

        if (selectedApartmentId != null) {
            apartmentAdapter.setSelectedServiceId(selectedApartmentId)
        }

    }

    private fun setDataToSpecificationRv(servicesList: List<DataModel.Specification?>?) {
        specificationAdapter.submitList(servicesList)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}