package adapter

import activity.OtpConfirmation
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.akriti.foodiezone.R
import com.akriti.foodiezone.RestaurantDetails
import com.akriti.foodiezone.util.Restaurantdatabase
import com.squareup.picasso.Picasso
import database.RestaurantEntity
import model.Restaurant

class RestaurantRecyclerAdapter(val context: Context,val itemList:ArrayList<Restaurant>):RecyclerView.Adapter<RestaurantRecyclerAdapter.RestaurantViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_restaurant_single_row, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = itemList[position]
        holder.txtRestaurantName.text = restaurant.RestaurantName
        holder.txtRestaurantPrice.text = restaurant.RestaurantCostForOne
        holder.txtRestaurantPrice1.text = restaurant.RestaurantCostForOne
        holder.txtRestaurantRating.text = restaurant.RestaurantRating
        //holder.imgRestaurantImage.setImageResource(restaurant.RestaurantImage)
        Picasso.get().load(restaurant.RestaurantImage).into(holder.imgRestaurantImage)
        holder.llContent.setOnClickListener {
            Toast.makeText(context,"clicked on ${holder.txtRestaurantName.text}",Toast.LENGTH_SHORT).show()



            
        }

    }


    class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtRestaurantName: TextView = view.findViewById(R.id.txtRestaurantName)
        val txtRestaurantPrice: TextView = view.findViewById(R.id.txtRestaurantPrice)
        val txtRestaurantPrice1: TextView = view.findViewById(R.id.txtRestaurant1Price)
        val txtRestaurantRating: TextView = view.findViewById(R.id.txtRestaurantRating)
        val imgRestaurantImage: ImageView = view.findViewById(R.id.imgBookImage)
        val llContent: LinearLayout = view.findViewById(R.id.llcontent)


    }


    class DBAsyncTask(val context: Context, val restaurantEntity: RestaurantEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {
        val db =
            Room.databaseBuilder(context, Restaurantdatabase::class.java, "restaurant_db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {
            when (mode) {
                1 -> {
                    //check db if the book is favourite or not
                    val restaurant: RestaurantEntity? = db.restaurantDao()
                        .getRestaurantById(restaurantEntity.RestaurantId.toString())
                    db.close()
                    return restaurant != null
                }
                2 -> {
                    //Save the book into db as favourite
                    db.restaurantDao().insertRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
                3 -> {
                    //Remove the book from favourites
                    db.restaurantDao().deleteRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
            }
            return false
        }


    }
}