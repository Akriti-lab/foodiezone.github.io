package activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.akriti.foodiezone.R
import com.akriti.foodiezone.util.ConnectionManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etMobile: EditText
    lateinit var etAddress: EditText
    lateinit var etPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var btnRegister: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        title = "Register Yourself"

        toolbar = findViewById(R.id.toolbar)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etMobile = findViewById(R.id.etMobile)
        etAddress = findViewById(R.id.etAddress)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)



        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val mobile = etMobile.text.toString()
            val address = etAddress.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (validate()) {
                val queue = Volley.newRequestQueue(this@RegisterActivity)
                val url = "http://13.235.250.119/v2/register/fetch_result"


                val jsonParams = JSONObject()
                jsonParams.put("name", name)
                jsonParams.put("mobile_number", mobile)
                jsonParams.put("email", email)
                jsonParams.put("address", address)
                jsonParams.put("password", password)

                if (ConnectionManager().CheckConnectivity(this@RegisterActivity)) {
                    val jsonObjectRequest = object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                            //responselistener
                            try {
                                val data1 = it.getJSONObject("data")
                                val success = data1.getBoolean("success")

                                if (success) {
                                    val data2 = data1.getJSONObject("data")
                                    val id = data2.getString("user_id")
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        id,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    startActivity(
                                        Intent(
                                            this@RegisterActivity,
                                            HomeActivity::class.java
                                        )
                                    )
                                } else {
                                    val error = data1.getString("errorMessage")
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        error,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Some error occur",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        },
                            Response.ErrorListener {
                                //error
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Volley error $it",
                                    Toast.LENGTH_LONG
                                ).show()
                            }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["content-type"] = "application/json"
                            headers["token"] = "807e08a454fde6"
                            return headers
                        }

                    }
                    queue.add(jsonObjectRequest)
                } else {
                    val dialog = AlertDialog.Builder(this@RegisterActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("No Internet connection found.Open Settings")
                    dialog.setPositiveButton("Go to Settings") { text, listener ->
                        val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingIntent)
                        finish()
                    }
                    dialog.setNegativeButton("Exit") { text, listener ->
                        ActivityCompat.finishAffinity(this@RegisterActivity)

                    }
                    dialog.create()
                    dialog.show()
                }


            }
        }
    }

        fun validate(): Boolean {
            if (etName.text.toString().isEmpty()) {
                etName.error = "This field is required"
                return false
            } else if (etName.text.toString().length < 3) {
                etName.error = "Minimum three characters required"
                return false
            } else if (etEmail.text.toString().isEmpty()) {
                etEmail.error = "This field is required"
                return false
            } else if (!(Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches())) {

                etEmail.error = "Invalid Email Address"
                return false
            } else if (etMobile.text.toString().isEmpty()) {
                etMobile.error = "Number cannot be blank"
                return false
            } else if (etMobile.text.toString().length < 10) {
                etMobile.error = "Require 10 digits"
                return false
            } else if (etAddress.text.toString().isEmpty()) {
                etAddress.error = "Adress cannot be blank"
                return false
            } else if (etPassword.text.toString().isEmpty()) {
                etPassword.error = "Password cannot be empty"
                return false
            } else if (etPassword.text.toString().length < 4) {
                etPassword.error = "Create a strong password (Min.4 characters)"
                return false
            } else if (etConfirmPassword.text.toString().isEmpty()) {
                etConfirmPassword.error = "This field is required"
                return false
            } else if (!(etConfirmPassword.text.toString() == etPassword.text.toString())) {
                etConfirmPassword.error = "Enter the Password you created above"
                return false


            }
            return true

        }
    }






