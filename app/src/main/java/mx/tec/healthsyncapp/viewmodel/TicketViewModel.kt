package mx.tec.healthsyncapp.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Spinner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import mx.tec.healthsyncapp.model.Component
import mx.tec.healthsyncapp.model.Ticket
import mx.tec.healthsyncapp.repository.TicketRepository
import org.json.JSONArray
import org.json.JSONObject


class TicketViewModel: ViewModel() {

    private val _ticket = MutableLiveData<Ticket>()
    val ticket: LiveData<Ticket> get() = _ticket

    private val _response = MutableLiveData<JSONObject?>()
    val response: LiveData<JSONObject?> = _response

    private val _responseArray = MutableLiveData<JSONArray?>()
    val responseArray: LiveData<JSONArray?> = _responseArray


    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _imageBitmap = MutableLiveData<Bitmap?>()
    val imageBitmap: LiveData<Bitmap?> get() = _imageBitmap


    // LiveData para los componentes
    private val _componentsLiveData = MutableLiveData<JSONObject?>()
    val componentsLiveData: LiveData<JSONObject?> = _componentsLiveData



    fun fetchTicket(ticketId: String, urlTicket: String, queue: RequestQueue, context: Context) {

        val sharedPref = context.getSharedPreferences("sesion", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null) ?: return


        val listenerTicket = Response.Listener<JSONArray> { result ->
            Log.e("Result my tickets", result.toString())
            if (result.length() > 0) {
                val ticketJson = result.getJSONObject(0)

                val componentsArray = ticketJson.optJSONArray("components")
                val componentsList = mutableListOf<String>()

                if (componentsArray != null && componentsArray.length() > 0) {
                    for (i in 0 until componentsArray.length()) {
                        val component = componentsArray.getJSONObject(i)
                        val componentName = component.getString("name") // Obtener el nombre de cada componente
                        componentsList.add(componentName)
                    }
                } else {
                    componentsList.add("Sin componentes") // Añade un valor predeterminado si no hay componentes
                }

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
                    components = componentsList
                )
                _ticket.value = ticket // Actualiza el LiveData con el objeto Ticket
            }
        }

        val errorListener = Response.ErrorListener { error ->
            Log.e("Error", error.message.toString())
            _error.value = error.message // Actualiza el LiveData con el error
        }

        val ticketsDetails = object: JsonArrayRequest(
            Request.Method.GET, urlTicket,
            null, listenerTicket, errorListener
        ){
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token" // Añadir el token al encabezado
                return headers
            }
        }

        queue.add(ticketsDetails)
    }

    fun updateTicket(
        ticketId: String,
        idTechnician: String,
        status: String, subdomain: String,
        ticketRepository: TicketRepository,
        context: Context
    ) {
        ticketRepository.update(
            ticketId,
            idTechnician,
            status,
            subdomain,
            { response ->
            _response.postValue(response)
            }, { error ->
                _error.postValue(error.message)
            },
            context
        )
    }

    fun submitEvidenceSolution(
        ticketId: String,
        revisionProcess: String,
        diagnosis: String,
        solutionProcess: String,
        imageData: String,
        components: List<Component>,
        subdomain: String,
        ticketRepository: TicketRepository,
        context: Context
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
            },
            context
        )
    }

    fun submitEvidenceNoSolution(
        ticketId: String,
        failedReason: String,
        imageData: String,
        subdomain: String,
        ticketRepository: TicketRepository,
        context: Context
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
            },
            context
        )
    }

    fun loadImage(
        base64Image: String,
        ticketRepository: TicketRepository
    ) {
        val bitmap = ticketRepository.decodeBase64Image(base64Image)
        _imageBitmap.value = bitmap
    }


    fun getComponents(
        context: Context,
        spinner: Spinner,
        subdomain: String,
        ticketRepository: TicketRepository
    ) {
        ticketRepository.getComponents(
            context,
            spinner,
            subdomain,
            { response ->
                // Actualizar el LiveData con la respuesta obtenida
                _responseArray.postValue(response)
            },
            { error ->
                // Actualizar el LiveData con el error obtenido
                _error.postValue(error.message)
            }
        )
    }

}