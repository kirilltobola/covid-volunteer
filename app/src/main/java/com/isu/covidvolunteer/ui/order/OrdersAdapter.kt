package com.isu.covidvolunteer.ui.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.models.order.OrderDto
import com.isu.covidvolunteer.ui.OnItemClickListener

class OrdersAdapter(val orders: List<OrderDto>) : RecyclerView.Adapter<OrdersAdapter.ActiveOrdersViewHolder>() {
    private lateinit var _clickListener: OnItemClickListener

    class ActiveOrdersViewHolder(itemView: View, clickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val textView1 = itemView.findViewById<TextView>(R.id.textView1)
        val textView2 = itemView.findViewById<TextView>(R.id.textView2)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(absoluteAdapterPosition)
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        _clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveOrdersViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return ActiveOrdersViewHolder(itemView, _clickListener)
    }

    override fun onBindViewHolder(holder: ActiveOrdersViewHolder, position: Int) {
        holder.textView1.text = orders[position].id.toString()
        holder.textView2.text = orders[position].headline
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}