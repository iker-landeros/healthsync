package mx.tec.healthsyncapp.repository

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonArrayRequest
import mx.tec.healthsyncapp.R
import org.json.JSONArray
import org.json.JSONObject


class TicketRepository(private val queue: RequestQueue) {

    private val baseUrl = "http://example.com/api/tickets"  // URL base genérica

    fun getDetails() {

    }

    fun getSummary(callback: (JSONArray?) -> Unit, errorCallback: (VolleyError) -> Unit) {

    }

    fun update(ticketId: String, idTechnician: String, status: String, subdomain: String, callback: (JSONObject?) -> Unit, errorCallback: (VolleyError) -> Unit) {
        val urlUpdate = "$subdomain/tickets/$ticketId"
        val body = JSONObject().apply {
            put("idTechnician", idTechnician)
            put("status", status)
        }

        val listener = Response.Listener<JSONObject> { result ->
            callback(result)
        }

        val errorListener = Response.ErrorListener { error ->
            errorCallback(error)
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.PUT, urlUpdate, body, listener, errorListener)
        queue.add(jsonObjectRequest)
    }


    fun postEvidenceSolution(ticketId: String, idTechnician: String, status: String, subdomain: String, callback: (JSONObject?) -> Unit, errorCallback: (VolleyError) -> Unit) {
        val urlEvidenceSolution = "$subdomain/tickets/$ticketId/solved"

        val listener = Response.Listener<JSONObject> { response ->
            callback(response)
        }

        val errorListener = Response.ErrorListener { error ->
            errorCallback(error)
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, urlEvidenceSolution, null, listener, errorListener)
        queue.add(jsonObjectRequest)
    }

    fun postEvidenceNoSolution(ticketId: String, idTechnician: String, status: String, subdomain: String, callback: (JSONObject?) -> Unit, errorCallback: (VolleyError) -> Unit) {
        val urlEvidenceSolution = "$subdomain/tickets/$ticketId/solved"

        val listener = Response.Listener<JSONObject> { response ->
            callback(response)
        }

        val errorListener = Response.ErrorListener { error ->
            errorCallback(error)
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, urlEvidenceSolution, null, listener, errorListener)
        queue.add(jsonObjectRequest)
    }
}
