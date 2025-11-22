package com.rcm.engineering.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rcm.engineering.user.databinding.ItemEmployeeBinding
import com.rcm.engineering.user.models.Employee

class EmployeeAdapter(
    private var list: MutableList<Employee>,
    private val on: (Employee) -> Unit,
    private val onDelete: (Employee) -> Unit
) : RecyclerView.Adapter<EmployeeAdapter.UserViewHolder>() {

    fun setList(newList: List<Employee>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
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
        holder.binding.tvDob.text = user.dateOfBirth
        holder.binding.tvManager.text = user.manager
        holder.binding.tvDateOfJoining.text = user.dateOfJoining
        holder.binding.tvEmpCode.text = user.empCode

        holder.binding.btn.setOnClickListener { on(user) }
        holder.binding.btnDelete.setOnClickListener { onDelete(user) }
    }

    override fun getItemCount(): Int = list.size

    inner class UserViewHolder(val binding: ItemEmployeeBinding) :
        RecyclerView.ViewHolder(binding.root)
}