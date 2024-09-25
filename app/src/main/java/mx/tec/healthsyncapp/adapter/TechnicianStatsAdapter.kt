package mx.tec.healthsyncapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.tec.healthsyncapp.model.TechnicianStats
import mx.tec.healthsyncapp.databinding.TechnicianStatsLayoutBinding


class TechnicianStatsAdapter(val data: List<TechnicianStats>):

    RecyclerView.Adapter<TechnicianStatsAdapter.ViewHolder>() {
    class ViewHolder(val binding: TechnicianStatsLayoutBinding):
        RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TechnicianStatsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = data[position]
        with(holder.binding){
            txtNombreTecnico.text = element.name
            txtTktsTotalesRes.text = element.resolvedTicketsCount
            txtTiempoProm.text = element.avgResolutionTimeHours
        }
    }

}

