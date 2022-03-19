package project.app.cornershop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class GroceriesAdapter(private var shopList: MutableList<ShopList>, private var clickListener: ClickListener):RecyclerView.Adapter<GroceriesAdapter.GroceriesViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceriesViewHolder {
        val layout: View = LayoutInflater.from(parent.context).inflate(R.layout.groceries_card_view,parent,false)
        return GroceriesViewHolder(layout)
    }

    override fun onBindViewHolder(holder: GroceriesViewHolder, position: Int) {
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
    class GroceriesViewHolder(view: View): RecyclerView.ViewHolder(view){
        var shopImage: ImageView = view.findViewById(R.id.sage)
        var shopName: TextView = view.findViewById(R.id.titleProd)
        var shopAddress: TextView = view.findViewById(R.id.descripProd)

    }
    interface ClickListener {
        fun clickedItem(position: Int)
    }
}