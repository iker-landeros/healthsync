package mx.tec.healthsyncapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicketSummaryClass {
}

data class Ticket(
    val status: String,
    val date: String,
    val problemType: String
)


class TicketsSummaryAdapter(private val ticketsList: List<Ticket>) : RecyclerView.Adapter<TicketsSummaryAdapter.TicketViewHolder>() {

    // Clase interna que define el ViewHolder (representa un ítem de la lista)
    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ticketStatus: TextView = itemView.findViewById(R.id.txtEstado)
        val ticketDate: TextView = itemView.findViewById(R.id.txtFecha)
        val ticketProblemType: TextView = itemView.findViewById(R.id.txtDescripcion)

        // Método para vincular los datos del ticket con las vistas
        fun bind(ticket: Ticket) {
            ticketStatus.text = ticket.status
            ticketDate.text = ticket.date
            ticketProblemType.text = ticket.problemType
        }
    }

    // Infla el layout del ítem para cada fila del RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ticket, parent, false)  // Aquí inflamos el layout item_ticket.xml
        return TicketViewHolder(view)
    }

    // Vincula los datos del ticket con las vistas del ViewHolder
    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = ticketsList[position]
        holder.bind(ticket)
    }

    // Retorna el tamaño de la lista de tickets
    override fun getItemCount(): Int {
        return ticketsList.size
    }
}