package mx.tec.healthsyncapp.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
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

    fun update(
        ticketId: String,
        idTechnician: String,
        status: String,
        subdomain: String,
        callback: (JSONObject?) -> Unit,
        errorCallback: (VolleyError) -> Unit
    ) {
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


    fun postEvidenceSolution(
        ticketId: String,
        revisionProcess: String,
        diagnosis: String,
        solutionProcess: String,
        imageData: String,
        components: String,
        subdomain: String,
        callback: (JSONObject?) -> Unit,
        errorCallback: (VolleyError) -> Unit
    ) {

        val urlEvidenceSolution = "$subdomain/tickets/$ticketId/solved"

        val requestBody = JSONObject().apply {
            put("ticketId", ticketId)
            put("revisionProcess", revisionProcess)
            put("diagnosis", diagnosis)
            put("solutionProcess", solutionProcess)
            put("imageData", imageData)
            put("components", components)
        }

        val listener = Response.Listener<JSONObject> { response ->
            callback(response)
        }

        val errorListener = Response.ErrorListener { error ->
            errorCallback(error)
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            urlEvidenceSolution,
            requestBody,
            listener,
            errorListener
        )

        queue.add(jsonObjectRequest)
    }


    fun postEvidenceNoSolution(
        ticketId: String,
        failedReason: String,
        imageData: String,
        subdomain: String,
        callback: (JSONObject?) -> Unit,
        errorCallback: (VolleyError) -> Unit
    ) {

        val urlEvidenceNoSolution = "$subdomain/tickets/$ticketId/not-solved"

        val requestBody = JSONObject().apply {
            put("failedReason", failedReason)
            put("imageData", imageData)
        }

        val listener = Response.Listener<JSONObject> { response ->
            callback(response)
        }

        val errorListener = Response.ErrorListener { error ->
            errorCallback(error)
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            urlEvidenceNoSolution,
            requestBody,
            listener,
            errorListener
        )

        queue.add(jsonObjectRequest)
    }

    fun decodeBase64Image(base64Image: String): Bitmap? {
        return try {
            // Decodifica la imagen en Base64 a un array de bytes
            val decodedString = Base64.decode(base64Image, Base64.DEFAULT)
            // Convierte el array de bytes a un Bitmap
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}
