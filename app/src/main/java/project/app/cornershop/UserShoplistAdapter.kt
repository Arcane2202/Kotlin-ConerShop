package project.app.cornershop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class UserShoplistAdapter(private var shopList: MutableList<ShopList>, private var clickListener: ClickListener): RecyclerView.Adapter<UserShoplistAdapter.UserShoplistViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserShoplistViewHolder {
        val layout: View = LayoutInflater.from(parent.context).inflate(R.layout.usershoplist_card_view,parent,false)
        return UserShoplistViewHolder(layout)
    }

    override fun onBindViewHolder(holder: UserShoplistViewHolder, position: Int) {
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
    class UserShoplistViewHolder(view: View): RecyclerView.ViewHolder(view){
        var shopImage: ImageView = view.findViewById(R.id.sageus)
        var shopName: TextView = view.findViewById(R.id.titleProdus)
        var shopAddress: TextView = view.findViewById(R.id.descripProdus)
    }
    interface ClickListener {
        fun clickedItem(position: Int)
    }
}