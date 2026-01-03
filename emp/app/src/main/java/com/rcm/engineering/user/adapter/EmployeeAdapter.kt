package com.rcm.engineering.user.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rcm.engineering.user.databinding.ItemEmployeeBinding
import com.rcm.engineering.user.models.Employee
import java.util.Locale


class EmployeeAdapter(
    private var list: MutableList<Employee>,
    private val on: (Employee) -> Unit,
    private val onDelete: (Employee) -> Unit
) : RecyclerView.Adapter<EmployeeAdapter.UserViewHolder>() {

    private var fullList: MutableList<Employee> = mutableListOf()
    fun setList(newList: List<Employee>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        val filtered = if (query.isEmpty()) {
            fullList
        } else {
            fullList.filter { it.name.contains(query, ignoreCase = true) }.toMutableList()
        }

        val diffResult = DiffUtil.calculateDiff(EmployeeDiffCallback(list, filtered))
        list.clear()
        list.addAll(filtered)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemEmployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = list[position]
        holder.binding.tvName.text = user.name
        holder.binding.tvEmail.text = user.email
        holder.binding.tvMobile.text = user.mobile
        holder.binding.tvGender.text = user.gender
        holder.binding.tvDesignation.text = user.designation
        holder.binding.tvDepartment.text = user.department
        holder.binding.tvAddress.text = user.address
        holder.binding.tvManager.text = user.manager
        holder.binding.tvDob.text = formatDate(user.dateOfBirth)
        holder.binding.tvDateOfJoining.text = formatDate(user.dateOfJoining)
        holder.binding.tvOhr.text = user.ohr
        holder.binding.tvSalary.text = user.salary
        holder.binding.tvCity.text = user.city
        holder.binding.tvState.text = user.state
        holder.binding.tvCountry.text = user.country

        holder.binding.btn.setOnClickListener { on(user) }
        holder.binding.btnDelete.setOnClickListener { onDelete(user) }

        holder.binding.layoutHeader.setOnClickListener {
            if (holder.binding.layoutDetails.visibility == View.VISIBLE) {
                holder.binding.layoutDetails.visibility = View.GONE
            } else {
                holder.binding.layoutDetails.visibility = View.VISIBLE
            }
        }
    }

    private fun formatDate(dateString: String): String {
        return try {
            val input = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val output = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            output.format(input.parse(dateString)!!)
        } catch (e: Exception) {
            dateString
        }
    }


    override fun getItemCount(): Int = list.size

    inner class UserViewHolder(val binding: ItemEmployeeBinding) :
        RecyclerView.ViewHolder(binding.root)

    class EmployeeDiffCallback(
        private val oldList: List<Employee>,
        private val newList: List<Employee> ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}