package com.demo.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.app.databinding.RowItemCartBinding
import com.demo.app.model.SelectionObject

class CartAdapter(val context: Context, val listener: CartSelection) :
    ListAdapter<SelectionObject, CartAdapter.SpecificationViewHolder>(
        ComparatorDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecificationViewHolder {
        val binding =
            RowItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpecificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpecificationViewHolder, position: Int) {
        val coupon = getItem(position)
        coupon?.let {
            holder.bind(it, position)
        }

    }

    inner class SpecificationViewHolder(private val binding: RowItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(specification: SelectionObject, position: Int) {

            val parentName = "${specification.parent?.name!![0]}"
            var childrenName = ""
            var priceTotal = 0
            specification.child.forEach {
                if (it?.isOptionSelected == true) {
                    childrenName += it.name!![0] + "(${it.quantitySelected}),"
                    priceTotal += it.price!! * it.quantitySelected
                }
            }
            priceTotal += (specification.parent!!.price?.times(specification.parent!!.quantitySelected)!!)

            binding.cartDescriptionTv.text = "${parentName}, ${childrenName}"
            binding.cartPriceTv.text = "₹${priceTotal}.00"

            binding.cartCloseIconIv.setOnClickListener {
                listener.cartSelectedForRemove(specification)
            }

        }
    }

    interface CartSelection {
        fun cartSelectedForRemove(cartObj: SelectionObject)
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<SelectionObject>() {
        override fun areItemsTheSame(
            oldItem: SelectionObject,
            newItem: SelectionObject,
        ): Boolean {
            return oldItem.parent?.id == newItem.parent?.id
        }

        override fun areContentsTheSame(
            oldItem: SelectionObject,
            newItem: SelectionObject,
        ): Boolean {
            return oldItem == newItem
        }

    }
}