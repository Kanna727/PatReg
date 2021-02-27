package ne.patreg.com.patreg1

import android.os.Bundle
import android.util.DisplayMetrics
import android.app.Activity
import android.content.Context
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.security.AccessController.getContext

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : Activity() {

    private var paintView: PaintView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        paintView = findViewById<PaintView?>(R.id.paintView)
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        paintView!!.init(metrics)

        val button_clear = findViewById<Button>(R.id.clear)
        val button_upload = findViewById<Button>(R.id.Upload)

        button_clear.setOnClickListener {
            paintView!!.clear()
        }

        button_upload.setOnClickListener {
            val file = File(Environment.getExternalStorageDirectory(), "Android/data/ne.patreg.com.patreg1/files/XY_coordinates.txt")
            val lines:List<String> = file.readLines()
            val jsonArray: JSONArray = JSONArray()
            for(line in lines){
                println(line)
                val jsonObject: JSONObject = JSONObject()
                jsonObject.put("coordinates", line)
                jsonArray.put(jsonObject)
            }
            sendGet(jsonArray)
        }
    }

    private fun sendGet(jsonArray: JSONArray) {
        val queue = Volley.newRequestQueue(this)
        val url: String = "https://damp-plains-82912.herokuapp.com/search"

        val jsonObject: JSONObject = JSONObject()
        jsonObject.put("COORDINATES", jsonArray)

        val jsonObjReq = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                Response.Listener<JSONObject> { response ->
                    val jsonArray: JSONArray = response.getJSONArray("result")
//                    var str_name: String = ""
////                    var jsonInner: JSONObject = jsonArray.getJSONObject(0)
//                    str_name = jsonObj.get("name") as String
//                    println(str_name)

                    println(jsonArray.toString())
                    Toast.makeText(this, jsonArray.toString(), Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener { println("Didn't Work") })
        queue.add(jsonObjReq)
    }

}
