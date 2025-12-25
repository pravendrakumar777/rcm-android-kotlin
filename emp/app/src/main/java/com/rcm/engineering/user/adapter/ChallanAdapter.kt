package com.rcm.engineering.user.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rcm.engineering.user.databinding.ItemChallanBinding
import com.rcm.engineering.user.models.Challan
import java.util.Locale

class ChallanAdapter(
    private var list: MutableList<Challan>,
    private val onView: (Challan) -> Unit,
    private val btnPDF: (Challan) -> Unit,
    private val btnExcel: (Challan) -> Unit,
    private val btnCSV: (Challan) -> Unit

) : RecyclerView.Adapter<ChallanAdapter.ChallanViewHolder>() {

    fun setList(newList: List<Challan>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallanViewHolder {
        val binding = ItemChallanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChallanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChallanViewHolder, position: Int) {
        val challan = list[position]
        holder.binding.tvChallanNo.text = challan.challanNo
        holder.binding.tvChallanRefNo.text = challan.refChNo
        holder.binding.tvCustomerName.text = challan.customerName
        holder.binding.tvDate.text = formatDate(challan.date)
        holder.binding.btnView.setOnClickListener { onView(challan) }
        holder.binding.btnPDF.setOnClickListener { btnPDF(challan) }
        holder.binding.btnExcel.setOnClickListener { btnExcel(challan) }
        holder.binding.btnCSV.setOnClickListener { btnCSV(challan) }

    }

    private fun formatDate(dateString: String): String {
        return try {
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val output = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
            output.format(input.parse(dateString)!!)
        } catch (e: Exception) {
            dateString
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ChallanViewHolder(val binding: ItemChallanBinding) :
        RecyclerView.ViewHolder(binding.root)
}