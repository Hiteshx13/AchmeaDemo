package com.achmea.demo.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.achmea.demo.R
import com.achmea.demo.databinding.ItemLayoutBinding
import com.achmea.demo.domain.model.Employer

class EmployerAdapter(private val items: List<Employer>) :
    RecyclerView.Adapter<EmployerAdapter.ViewHolder>() {

    class ViewHolder( itemView: ItemLayoutBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemLayoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_layout, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEmployer = items[position]
        holder.binding.employerData=currentEmployer
    }

    override fun getItemCount(): Int {
        return items.size
    }
}