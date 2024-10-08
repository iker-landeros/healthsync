package mx.tec.healthsyncapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.tec.healthsyncapp.databinding.TechnicianInfoLayoutBinding
import mx.tec.healthsyncapp.model.TechnicianInfo
import android.widget.Toast
import mx.tec.healthsyncapp.EditTechnicianInfo

class TechnicianInfoAdapter(val data: List<TechnicianInfo>) :

    RecyclerView.Adapter<TechnicianInfoAdapter.ViewHolder>() {
    class ViewHolder(val binding: TechnicianInfoLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TechnicianInfoLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = data[position]
        with(holder.binding) {

            txtNombreTecnico.text = element.name

            btnEdit.setOnClickListener {

                val intent = Intent(holder.itemView.context, EditTechnicianInfo::class.java).apply {
                    putExtra("TECHNICIAN_NAME", element.name)
                    putExtra("TECHNICIAN_USERNAME", element.username)
                    putExtra("TECHNICIAN_ID", element.idUser)
                }
                holder.itemView.context.startActivity(intent)
            }
        }
    }
}
