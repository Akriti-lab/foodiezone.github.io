package activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.widget.*
import androidx.core.app.ActivityCompat
import com.akriti.foodiezone.R
import com.akriti.foodiezone.util.ConnectionManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ForgotPassword : AppCompatActivity() {
    lateinit var txtRegisterYourself: TextView
    lateinit var etMobile: EditText
    lateinit var etEmail: EditText
    lateinit var btnNext: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        title = "Reset Password"

        etMobile = findViewById(R.id.etMobile)
        etEmail = findViewById(R.id.etEmail)
        btnNext = findViewById(R.id.btnNext)
        txtRegisterYourself = findViewById(R.id.txtRegisterYourself)




        btnNext.setOnClickListener {
            val email = etEmail.text.toString()
            val mobile = etMobile.text.toString()

            if (validate()) {
                val queue = Volley.newRequestQueue(this@ForgotPassword)
                val url = "http://13.235.250.119/v2/forgot_password/fetch_result"

                val jsonParams = JSONObject()

                jsonParams.put("mobile_number", mobile)
                jsonParams.put("email", email)

                if (ConnectionManager().CheckConnectivity(this@ForgotPassword)) {
                    val jsonObjectRequest = object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                            //responselistener
                            try {
                                val data = it.getJSONObject("data")
                                val success = data.getBoolean("success")

                                if (success) {
                                    val intent=Intent(this@ForgotPassword,OtpConfirmation::class.java)
                                    intent.putExtra("Keynumber",mobile)

                                   val dialog=AlertDialog.Builder(this@ForgotPassword)
                                    dialog.setTitle("Information")
                                    dialog.setMessage("Please refer to the previous mail for OTP.")
                                    dialog.setPositiveButton("OK"){text, listener->
                                        startActivity(Intent(intent))
                                        finish()
                                    }
                                    dialog.create()
                                    dialog.show()
                                } else {
                                    val error = data.getString("errorMessage")
                                    Toast.makeText(
                                        this@ForgotPassword,
                                        error,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@ForgotPassword,
                                    "Some error occur",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        },
                            Response.ErrorListener {
                                //error
                                Toast.makeText(
                                    this@ForgotPassword,
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
                    val dialog = AlertDialog.Builder(this@ForgotPassword)
                    dialog.setTitle("Error")
                    dialog.setMessage("No Internet connection found.Open Settings")
                    dialog.setPositiveButton("Go to Settings") { text, listener ->
                        val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingIntent)
                        finish()
                    }
                    dialog.setNegativeButton("Exit") { text, listener ->
                        ActivityCompat.finishAffinity(this@ForgotPassword)

                    }
                    dialog.create()
                    dialog.show()
                }


            }
        }
    }


        fun validate(): Boolean {
            if (etEmail.text.toString().isEmpty()) {
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
            }
            return true

        }
    }
