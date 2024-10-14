package mx.tec.healthsyncapp.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonArrayRequest
import mx.tec.healthsyncapp.R
import mx.tec.healthsyncapp.model.Component
import org.json.JSONArray
import org.json.JSONObject


class TicketRepository(private val queue: RequestQueue) {


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
        errorCallback: (VolleyError) -> Unit,
        context: Context
    ) {
        val sharedPref = context.getSharedPreferences("sesion", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null) ?: return

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

        val jsonObjectRequest = object: JsonObjectRequest(
            Request.Method.PUT,
            urlUpdate,
            body,
            listener,
            errorListener
        ){
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token" // Añadir el token al encabezado
                return headers
            }
        }

        queue.add(jsonObjectRequest)
    }


    fun postEvidenceSolution(
        ticketId: String,
        revisionProcess: String,
        diagnosis: String,
        solutionProcess: String,
        imageData: String,
        components: List<Component>, // Aquí ya tienes una lista de objetos Component
        subdomain: String,
        callback: (JSONObject?) -> Unit,
        errorCallback: (VolleyError) -> Unit,
        context: Context
    ) {
        val sharedPref = context.getSharedPreferences("sesion", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null) ?: return

        val urlEvidenceSolution = "$subdomain/tickets/$ticketId/solved"

        // Construir un JSONArray con los componentes seleccionados
        val componentsArray = JSONArray()
        for (component in components) {
            // Cada componente es un objeto JSON con idComponent y quantity
            val componentJson = JSONObject().apply {
                put("idComponent", component.idComponent) // Usar el id del componente
                put("quantity", 1) // O la cantidad que tú quieras; aquí es 1 por defecto
            }
            componentsArray.put(componentJson) // Añadir el JSON del componente al array
        }

        // Construir el cuerpo de la solicitud con todos los datos
        val requestBody = JSONObject().apply {
            put("ticketId", ticketId)
            put("revisionProcess", revisionProcess)
            put("diagnosis", diagnosis)
            put("solutionProcess", solutionProcess)
            put("imageData", imageData)
            put("components", componentsArray) // Añadir el array de componentes
        }

        // Listener para manejar la respuesta exitosa
        val listener = Response.Listener<JSONObject> { response ->
            callback(response)
        }

        // Listener para manejar los errores
        val errorListener = Response.ErrorListener { error ->
            errorCallback(error)
        }

        // Crear la solicitud JSON
        val jsonObjectRequest = object: JsonObjectRequest(
            Request.Method.POST,
            urlEvidenceSolution,
            requestBody,
            listener,
            errorListener
        ){
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token" // Añadir el token al encabezado
                return headers
            }
        }

        // Añadir la solicitud a la cola de peticiones
        queue.add(jsonObjectRequest)
    }


    fun postEvidenceNoSolution(
        ticketId: String,
        failedReason: String,
        imageData: String,
        subdomain: String,
        callback: (JSONObject?) -> Unit,
        errorCallback: (VolleyError) -> Unit,
        context: Context
    ) {
        val sharedPref = context.getSharedPreferences("sesion", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null) ?: return

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

        val jsonObjectRequest = object: JsonObjectRequest(
            Request.Method.POST,
            urlEvidenceNoSolution,
            requestBody,
            listener,
            errorListener
        ){
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token" // Añadir el token al encabezado
                return headers
            }
        }

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

    fun getComponents(
        context: Context,
        spinner: Spinner,
        subdomain: String,
        successCallback: (JSONArray?) -> Unit,
        errorCallback: (VolleyError) -> Unit,
    ) {

        val sharedPref = context.getSharedPreferences("sesion", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null) ?: return

        val url = "$subdomain/tickets/components"

        val responseListener = Response.Listener<JSONArray> { response ->
            val dataList = ArrayList<Component>()

            for (i in 0 until response.length()) {
                val jsonObject: JSONObject = response.getJSONObject(i)
                val componentId = jsonObject.getInt("idComponent")
                val componentName = jsonObject.getString("componentName")
                dataList.add(Component(componentId, componentName))
            }

            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, dataList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            // Llamar al callback de éxito con la respuesta
            successCallback(response)
        }

        // Listener para manejar errores
        val errorListener = Response.ErrorListener { error ->
            error.printStackTrace() // Manejar el error de la solicitud
            errorCallback(error) // Invocar el callback de error
        }

        // Crear la petición de datos usando JsonArrayRequest
        val componentsRequest = object: JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            responseListener,
            errorListener
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token" // Añadir el token al encabezado
                return headers
            }
        }

        // Añadir la solicitud a la cola
        queue.add(componentsRequest)
    }

}
