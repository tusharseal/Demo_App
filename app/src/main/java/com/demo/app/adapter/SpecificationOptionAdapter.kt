package com.demo.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.app.databinding.RowItemSpecificationOptionsBinding
import com.demo.app.model.DataModel.Specification.DataList
import com.demo.app.utils.makeViewGone
import com.demo.app.utils.makeViewVisible

class SpecificationOptionAdapter(
    val context: Context,
    var specOptList: MutableList<DataList?>,
    var userCanAddSpecificationQuantity: Boolean?,
    val onClickListener: OnClickInterface
) :
    RecyclerView.Adapter<SpecificationOptionAdapter.SelectServicesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectServicesViewHolder {
        val binding =
            RowItemSpecificationOptionsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return SelectServicesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectServicesViewHolder, position: Int) {
        holder.bind(specOptList[position], position)

    }

    inner class SelectServicesViewHolder(private val binding: RowItemSpecificationOptionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(specOpt: DataList?, position: Int) {


            binding.itemSpecOptCheckboxCb.setOnCheckedChangeListener { buttonView, isChecked ->
                specOpt?.isOptionSelected = !specOpt?.isOptionSelected!!
                notifyDataSetChanged()

                onClickListener.onOptionClicked(specOptList)

                if (specOpt?.isOptionSelected == true) {

                    //For Default
                    var count = 1;
                    binding.specQtyTv.text = "$count"
                    specOpt.quantitySelected = count


                    //For Minus
                    binding.specQtyMinusIv.setOnClickListener {
                        if (count != 1) {
                            --count
                            binding.specQtyTv.text = "$count"
                            specOpt.quantitySelected = count

                            onClickListener.onOptionClicked(specOptList)


                        }
                    }

                    //For Plus
                    binding.specQtyPlusIv.setOnClickListener {
                        ++count
                        binding.specQtyTv.text = "$count"
                        specOpt.quantitySelected = count

                        onClickListener.onOptionClicked(specOptList)


                    }

                } else {
                    //Resetting to 0
                    var count = 0;
                    binding.specQtyTv.text = "$count"
                }
            }

            binding.itemSpecOptNameTv.text = specOpt?.name!![0].toString()
            binding.itemSpecOptPriceTv.text = "₹${specOpt?.price}.00"

            if (specOpt.isOptionSelected == true && userCanAddSpecificationQuantity == true && specOpt.price != 0) {
                binding.itemSpecOptQtyLl.makeViewVisible()
            } else {
                binding.itemSpecOptQtyLl.makeViewGone()
            }

        }
    }

    fun getAll(): MutableList<DataList?> {
        return specOptList
    }

    fun addAll(serviceList: ArrayList<DataList>) {
        serviceList.clear()
        for (i in 0 until serviceList.size) {
            addSingle(serviceList[i])
        }
        notifyDataSetChanged()
    }

    private fun addSingle(data: DataList) {
        specOptList.add(data)
    }

    fun getSelected(): ArrayList<DataList?> {
        val selected: ArrayList<DataList?> = ArrayList()
        for (i in 0 until specOptList.size) {
            if (specOptList[i]?.isOptionSelected == true) {
                selected.add(specOptList[i])
            }
        }
        return selected
    }

    interface OnClickInterface {
        fun onOptionClicked(optionsList: MutableList<DataList?>)
    }

    override fun getItemCount(): Int {
        return specOptList.size
    }
}