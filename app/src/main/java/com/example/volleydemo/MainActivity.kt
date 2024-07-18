package com.example.volleydemo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Request.Method
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.volleydemo.databinding.ActivityMainBinding
import com.google.gson.Gson
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
  private lateinit var activityMainBinding: ActivityMainBinding
  var users = ArrayList<User>()
    var pageNumber = 1
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        VolleySingleton.initRequestQueue(this)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.btnCreateUser.setOnClickListener {
            addUser()
        }

        activityMainBinding.btnStringRequest.setOnClickListener {
            stringRequest()

        }
        activityMainBinding.btnJSONSObjectRequest.setOnClickListener {
            jsonObjectRequest()
        }
    }


    private fun jsonObjectRequest(){
        var volleyJsonObjectRequest = JsonObjectRequest(Method.GET,
            "https://reqres.in/api/users?page=$pageNumber",
            null,
            JsonObjectRequestSuccessListner(),
            StringRequestErrorListener()
            )

        //add created request to volley requested queue to volleySingletonClass
        VolleySingleton.volleyRequestQueue?.add(volleyJsonObjectRequest)
    }
    inner class JsonObjectRequestSuccessListner : Response.Listener<JSONObject>{
        override fun onResponse(response: JSONObject?) {
            var usersResponse = Gson().fromJson<UserResponse>(response.toString(),UserResponse::class.java)
            users.addAll(usersResponse.user)
            for (eachUser in users){
                Log.d("EachUsers","${eachUser.id} -- ${eachUser.firstName} -- ${eachUser.email}")
            }
        }

    }
    private fun addUser(){
        var jsonObject = JSONObject()
        jsonObject.put("email",activityMainBinding.edtUserEmail.text.toString())
        jsonObject.put("password",activityMainBinding.edtPassword.text.toString())

        var jsonObjectRequestQueue = JsonObjectRequest(
            Request.Method.POST,
            "https://reqres.in/api/register",
            jsonObject,
            AddUserListner(),
            StringRequestErrorListener()
        )

        VolleySingleton.volleyRequestQueue!!.add(jsonObjectRequestQueue)
    }
    inner class AddUserListner : Response.Listener<JSONObject>{
        override fun onResponse(response: JSONObject?) {
            Toast.makeText(this@MainActivity,"Success: ${response.toString()}",Toast.LENGTH_SHORT).show()

        }
    }
    inner class StringRequestErrorListener : Response.ErrorListener{
        override fun onErrorResponse(error: VolleyError?) {
            Toast.makeText(this@MainActivity,"Error: ${error.toString()}",Toast.LENGTH_SHORT).show()
        }
    }

    private fun stringRequest(){
        var volleyStringRequest = StringRequest(
            Request.Method.GET,
            "https://reqres.in/api/users?page=$pageNumber",
            StringRequestSuccessListener(),
            StringRequestErrorListener()
        )
        VolleySingleton.volleyRequestQueue!!.add(volleyStringRequest)
    }
    inner class StringRequestSuccessListener: Response.Listener<String>{
        override fun onResponse(response: String?) {
            var userResponse = Gson().fromJson<UserResponse>(
                response,
                UserResponse::class.java
            )
            users.addAll(userResponse.user)
            for(eachUsers in users){
                Log.d("Tag ","${eachUsers.id} -- ${eachUsers.email} -- ${eachUsers.firstName}")
            }
        }
    }
}