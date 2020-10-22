package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akriti.foodiezone.R
import kotlinx.android.synthetic.main.fragment_restaurant_details.view.*

class DetailsRecyclerAdapter(val context: Context,val itemList:ArrayList<String>):RecyclerView.Adapter<DetailsRecyclerAdapter.DetailsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_details_single_row, parent, false)
        return DetailsRecyclerAdapter.DetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
      val text=itemList[position]
        holder.textView.text=text
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    class DetailsViewHolder(view:View):RecyclerView.ViewHolder(view){
        val textView:TextView=view.findViewById(R.id.txtRestaurantsno)
    }


}