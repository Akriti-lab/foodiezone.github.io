package activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.akriti.foodiezone.R
import com.akriti.foodiezone.util.ConnectionManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    lateinit var etMobile:EditText
    lateinit var etPassword:EditText
    lateinit var btnLogin:Button
    lateinit var txtForgotPassword:TextView
    lateinit var txtRegisterYourself:TextView
    lateinit var sharedpreferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedpreferences=getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        val isLoggedIn=sharedpreferences.getBoolean("isLoggedIn",false)

        if(isLoggedIn){
            val intent=Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
        }else{
            setContentView(R.layout.activity_login)
        }


        setContentView(R.layout.activity_login)
        Toast.makeText(this,"Welcome",Toast.LENGTH_SHORT).show()
        title ="The Foodie Zone"

    etMobile=findViewById(R.id.etMobile)
    etPassword=findViewById(R.id.etPassword)
        btnLogin=findViewById(R.id.btnLogin)
        txtForgotPassword=findViewById(R.id.txtForgotPassword)
        txtRegisterYourself=findViewById(R.id.txtRegisterYourself)




        txtForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity,
                ForgotPassword::class.java))
        }
        txtRegisterYourself.setOnClickListener {
            startActivity(Intent(this@LoginActivity,
                RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener{
            val number = etMobile.text.toString()
            val password=etPassword.text.toString()
            if(number.length<10){
                Toast.makeText(this@LoginActivity,"Invalid number",Toast.LENGTH_SHORT).show()
            }else if(password.length<4){
                Toast.makeText(this@LoginActivity,"Invalid Password",Toast.LENGTH_SHORT).show()
            }else{
                val queue=Volley.newRequestQueue(this@LoginActivity)
                val url ="http://13.235.250.119/v2/login/fetch_result/"
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number",number)
                jsonParams.put("password",password)

                if(ConnectionManager().CheckConnectivity(this@LoginActivity))
                {


                    val jsonObjectRequest=object : JsonObjectRequest(Request.Method.POST,url,jsonParams,Response.Listener {
                        //responselistener
                        try{
                            val data1 = it.getJSONObject("data")
                            val success = data1.getBoolean("success")
                            if (success){
                                val data2 =data1.getJSONObject("data")
                                val name = data2.getString("name")
                                Toast.makeText(this@LoginActivity,name,Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@LoginActivity,
                                    HomeActivity::class.java))
                                saveSharedpreference()

                            }
                            else{
                                val error=data1.getString("errorMessage")
                                Toast.makeText(this@LoginActivity,error,Toast.LENGTH_SHORT).show()
                            }
                        }catch (e:Exception){
                            Toast.makeText(this@LoginActivity,"Some error occur",Toast.LENGTH_SHORT).show()

                        }
                    },
                        Response.ErrorListener {
                            //error
                            Toast.makeText(this@LoginActivity,"Volley error $it",Toast.LENGTH_LONG).show()
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
                    val dialog = AlertDialog.Builder(this@LoginActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("No Internet connection found.Open Settings")
                    dialog.setPositiveButton("Go to Settings"){text,listener ->
                        val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingIntent)
                        finish()
                    }
                    dialog.setNegativeButton("Exit"){text,listener ->
                        ActivityCompat.finishAffinity(this@LoginActivity)

                    }
                    dialog.create()
                    dialog.show()
                }


            }
        }

    }
    fun saveSharedpreference(){
        sharedpreferences.edit().putBoolean("isLoggedIn",true).apply()
    }



}