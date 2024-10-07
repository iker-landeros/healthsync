package mx.tec.healthsyncapp.model

data class Ticket(
    val idTicket: Int,
    val status: String,
    val senderName: String,
    val dateOpened: String,
    val description: String,
    val resolutionTimeHours: String,
    val areaName: String,
    val extensionNumber: String,
    val deviceName: String,
    val problemName: String,
    val technicianName: String?,
    val revisionProcess: String?,
    val diagnosis: String?,
    val solutionProcess: String?,
    val failedReason: String?,
    val ticketImage: String?,
    //val components: String?
)
