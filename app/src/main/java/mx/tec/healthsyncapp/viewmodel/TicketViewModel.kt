package mx.tec.healthsyncapp.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.model.Ticket
import mx.tec.healthsyncapp.repository.TicketRepository
import org.json.JSONArray
import org.json.JSONObject


class TicketViewModel: ViewModel() {

    private val _ticket = MutableLiveData<Ticket>()
    val ticket: LiveData<Ticket> get() = _ticket

    private val _response = MutableLiveData<JSONObject?>()
    val response: LiveData<JSONObject?> = _response

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _imageBitmap = MutableLiveData<Bitmap?>()
    val imageBitmap: LiveData<Bitmap?> get() = _imageBitmap

    fun fetchTicket(ticketId: String, urlTicket: String, queue: RequestQueue) {
        val listenerTicket = Response.Listener<JSONArray> { result ->
            Log.e("Result my tickets", result.toString())
            if (result.length() > 0) {
                val ticketJson = result.getJSONObject(0)
                val ticket = Ticket(
                    idTicket = ticketJson.getInt("idTicket"),
                    status = ticketJson.getString("status"),
                    senderName = ticketJson.getString("senderName"),
                    dateOpened = ticketJson.getString("dateOpened"),
                    description = ticketJson.getString("description"),
                    resolutionTimeHours = ticketJson.getString("resolutionTimeHours"),
                    areaName = ticketJson.getString("areaName"),
                    extensionNumber = ticketJson.getString("extensionNumber"),
                    deviceName = ticketJson.getString("deviceName"),
                    problemName = ticketJson.getString("problemName"),
                    technicianName = ticketJson.optString("technicianName", null),
                    revisionProcess = ticketJson.optString("revisionProcess", null),
                    diagnosis = ticketJson.optString("diagnosis", null),
                    solutionProcess = ticketJson.optString("solutionProcess", null),
                    failedReason = ticketJson.optString("failedReason", null),
                    ticketImage = ticketJson.optString("ticketImage", null),
                    //components = ticketJson.optString("components", null)
                )
                _ticket.value = ticket // Actualiza el LiveData con el objeto Ticket
            }
        }

        val errorListener = Response.ErrorListener { error ->
            Log.e("Error", error.message.toString())
            _error.value = error.message // Actualiza el LiveData con el error
        }

        val ticketsDetails = JsonArrayRequest(
            Request.Method.GET, urlTicket,
            null, listenerTicket, errorListener
        )

        queue.add(ticketsDetails)
    }

    fun updateTicket(
        ticketId: String,
        idTechnician: String,
        status: String, subdomain: String,
        ticketRepository: TicketRepository
    ) {
        ticketRepository.update(ticketId, idTechnician, status, subdomain, { response ->
            _response.postValue(response)
        }, { error ->
            _error.postValue(error.message)
        })
    }

    fun submitEvidenceSolution(
        ticketId: String,
        revisionProcess: String,
        diagnosis: String,
        solutionProcess: String,
        imageData: String,
        components: String,
        subdomain: String,
        ticketRepository: TicketRepository
    ) {
        ticketRepository.postEvidenceSolution(
            ticketId,
            revisionProcess,
            diagnosis,
            solutionProcess,
            imageData,
            components,
            subdomain,
            { response ->
                _response.postValue(response)
            },
            { error ->
                _error.postValue(error.message)
            }
        )
    }

    fun submitEvidenceNoSolution(
        ticketId: String,
        failedReason: String,
        imageData: String,
        subdomain: String,
        ticketRepository: TicketRepository
    ) {
        ticketRepository.postEvidenceNoSolution(
            ticketId,
            failedReason,
            imageData,
            subdomain,
            { response ->
                _response.postValue(response)
            },
            { error ->
                _error.postValue(error.message)
            }
        )
    }

    fun loadImage(
        base64Image: String,
        ticketRepository: TicketRepository
    ) {
        val bitmap = ticketRepository.decodeBase64Image(base64Image)
        _imageBitmap.value = bitmap
    }
}