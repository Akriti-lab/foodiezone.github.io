package fragment

import adapter.RestaurantRecyclerAdapter
import android.app.DownloadManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akriti.foodiezone.R
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import model.Restaurant

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AllrestaurantFragment : Fragment() {
  lateinit var recyclerView: RecyclerView
    lateinit var LayoutManager:RecyclerView.LayoutManager

   /* val restaurantList= arrayListOf(
        "Burgatory",
        "Pind tadka",
        "Garbar Burger",
        "Baco Tell",
        "Heera Mahal",
        "Smokin chick",
        "Burger jack",
        "Baasa Menu",
        "Pind Dubai",
        "Moti Mahal"

    )*/
    lateinit var recyclerAdapter: RestaurantRecyclerAdapter

    val restaurantInfoList= arrayListOf<Restaurant>()
    /*(
        Restaurant("Burgatory","1","4.5","299",R.drawable.splashicon),
        Restaurant("Burgatory","1","4.5","299",R.drawable.splashicon),
        Restaurant("Burgatory","1","4.5","299",R.drawable.splashicon),
        Restaurant("Burgatory","1","4.5","299",R.drawable.splashicon),
        Restaurant("Burgatory","1","4.5","299",R.drawable.splashicon),
        Restaurant("Burgatory","1","4.5","299",R.drawable.splashicon),
        Restaurant("Burgatory","1","4.5","299",R.drawable.splashicon),
        Restaurant("Burgatory","1","4.5","299",R.drawable.splashicon),
        Restaurant("Burgatory","1","4.5","299",R.drawable.splashicon),
        Restaurant("Burgatory","1","4.5","299",R.drawable.splashicon)
    )*/
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view =inflater.inflate(R.layout.fragment_allrestaurant, container, false)

        recyclerView=view.findViewById(R.id.RecyclerRestaurants)
        LayoutManager=LinearLayoutManager(activity)

        val queue= Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v2/restaurants/fetch_result/"

        val jsonObjectRequest=object:JsonObjectRequest(Method.GET,url,null,
            Response.Listener {
                val data1=it.getJSONObject("data")
                val success = data1.getBoolean("success")
                if (success)
                {
                    val data2=data1.getJSONArray("data")
                    for(i in 0 until data2.length()) {
                        val restaurantJsonObject = data2.getJSONObject(i)
                        val restaurantObject = Restaurant(
                            restaurantJsonObject.getString("id"),
                            restaurantJsonObject.getString("name"),
                            restaurantJsonObject.getString("rating"),
                            restaurantJsonObject.getString("cost_for_one"),
                            restaurantJsonObject.getString("image_url")
                        
                        )

                        restaurantInfoList.add(restaurantObject)

                        recyclerAdapter= RestaurantRecyclerAdapter(activity as Context,restaurantInfoList)
                        recyclerView.adapter=recyclerAdapter
                        recyclerView.layoutManager=LayoutManager
                        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context,
                            (LayoutManager as LinearLayoutManager).orientation)
                        )
                    }
                }
              else{
                    Toast.makeText(activity as Context,"Error",Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener {
                println("Error is $it")
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()

                    headers["Content-type"] = "application/json"
                    headers["token"] = "807e08a454fde6"
                    return headers

            }
        }


queue.add(jsonObjectRequest)

        return view
    }

    companion object {


        fun newInstance(param1: String, param2: String) =
            AllrestaurantFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}