package project.app.cornershop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class DrugsAdapter(private var shopList: MutableList<ShopList>, private var clickListener: ClickListener):RecyclerView.Adapter<DrugsAdapter.DrugViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugViewHolder {
        val layout:View = LayoutInflater.from(parent.context).inflate(R.layout.drugs_card_view,parent,false)
        return DrugViewHolder(layout)
    }

    override fun onBindViewHolder(holder: DrugViewHolder, position: Int) {
        holder.shopName.text = shopList[position].name
        holder.shopAddress.text = shopList[position].address
        Picasso.get().load(shopList[position].img).into(holder.shopImage)
        holder.itemView.setOnClickListener{
            clickListener.clickedItem(position)
        }
    }

    override fun getItemCount(): Int {
        return shopList.size
    }
    class DrugViewHolder(view: View):RecyclerView.ViewHolder(view){
        var shopImage: ImageView = view.findViewById(R.id.sage)
        var shopName: TextView = view.findViewById(R.id.titleProd)
        var shopAddress: TextView = view.findViewById(R.id.descripProd)

    }
    interface ClickListener {
       fun clickedItem(position: Int)
    }
}


















