package mx.tec.healthsyncapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.tec.healthsyncapp.R
import mx.tec.healthsyncapp.databinding.ItemTicketBinding
import mx.tec.healthsyncapp.model.TicketSummary

class TicketSummaryAdapter(val data: List<TicketSummary>) :

    RecyclerView.Adapter<TicketSummaryAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemTicketBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = data[position]
        with(holder.binding){
            txtEstado.text = element.status
            txtFecha.text = element.date
            txtDescripcion.text = element.problemType
        }
    }
}

