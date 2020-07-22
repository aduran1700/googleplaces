package com.googleplaces.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.googleplaces.R
import com.googleplaces.data.model.Result
import com.googleplaces.databinding.ResultListItemBinding

class LocationRecyclerViewAdapter: RecyclerView.Adapter<LocationRecyclerViewAdapter.ViewHolder>() {

    private val locations = mutableListOf<Result>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater  = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ResultListItemBinding>(
            inflater, R.layout.result_list_item, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = locations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locations[position]
        holder.bind(location)
    }

    fun updateList(updates: List<Result>) {
        this.locations.clear()
        this.locations.addAll(updates)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ResultListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.result = result
            binding.executePendingBindings()
        }
    }
}