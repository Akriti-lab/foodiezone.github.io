package activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
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

class OtpConfirmation : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var etOtp: EditText
    lateinit var etNewPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var btnSubmit: Button
    lateinit var number:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_confirmation)
        toolbar = findViewById(R.id.toolbar)
        etOtp = findViewById(R.id.etOTP)
        etNewPassword = findViewById(R.id.etNewPassword)
        btnSubmit = findViewById(R.id.btnSubmit)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)



        btnSubmit.setOnClickListener{
            if(intent!=null){
                number=intent.getStringExtra("Keynumber")as String
            }
            val password= etNewPassword.text.toString()
            val otp=etOtp.text.toString()


            if(password!= etConfirmPassword.text.toString()) {
                Toast.makeText(this@OtpConfirmation, "Password do not match", Toast.LENGTH_SHORT)
                    .show()
            }
            else{
                val queue=Volley.newRequestQueue(this@OtpConfirmation)
                val url = "http://13.235.250.119/v2/reset_password/fetch_result"


            val jsonParams = JSONObject()
                jsonParams.put("mobile_number",number)
                jsonParams.put("password",password)
                jsonParams.put("otp",otp)
                if(ConnectionManager().CheckConnectivity(this@OtpConfirmation))
                {

                    val jsonObjectRequest=object : JsonObjectRequest(Request.Method.POST,url,jsonParams,Response.Listener {
                        //responselistener
                        try{
                            val data1 = it.getJSONObject("data")
                            val success = data1.getBoolean("success")
                            if (success){
                                val successMessage =data1.getString("successMessage")
                                Toast.makeText(this@OtpConfirmation,"successMessage",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@OtpConfirmation, LoginActivity::class.java))

                            }
                            else{
                                val error=data1.getString("errorMessage")
                                Toast.makeText(this@OtpConfirmation,error,Toast.LENGTH_SHORT).show()
                            }
                        }catch (e:Exception){
                            Toast.makeText(this@OtpConfirmation,"Some error occur",Toast.LENGTH_SHORT).show()

                        }
                    },
                        Response.ErrorListener {
                            //error
                            Toast.makeText(this@OtpConfirmation,"Volley error $it",Toast.LENGTH_LONG).show()
                        })
                    {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String,String>()
                            headers["content-type"]="application/json"
                            headers["token"]="807e08a454fde6"
                            return headers
                        }

                    }
                    queue.add(jsonObjectRequest)

                }

                else{
                    val dialog = AlertDialog.Builder(this@OtpConfirmation)
                    dialog.setTitle("Error")
                    dialog.setMessage("No Internet connection found.Open Settings")
                    dialog.setPositiveButton("Go to Settings"){text,listener ->
                        val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingIntent)
                        finish()
                    }
                    dialog.setNegativeButton("Exit"){text,listener ->
                        ActivityCompat.finishAffinity(this@OtpConfirmation)

                    }
                    dialog.create()
                    dialog.show()
                }


            }
        }

    }
    }
