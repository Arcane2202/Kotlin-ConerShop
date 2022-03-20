package project.app.cornershop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ShopProductsAdapter(private var itemList: MutableList<ItemList>, private var clickListener: ClickListener):
    RecyclerView.Adapter<ShopProductsAdapter.ShopProductsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopProductsViewHolder {
        val layout: View = LayoutInflater.from(parent.context).inflate(R.layout.shop_products_card_view,parent,false)
        return ShopProductsViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ShopProductsViewHolder, position: Int) {
        holder.itemName.text = itemList[position].item
        holder.itemUnit.text = itemList[position].unit
        holder.itemPrice.text = (itemList[position].price + " BDT")
        Picasso.get().load(itemList[position].image).into(holder.itemImage)
        holder.itemView.setOnClickListener{
            clickListener.clickedItem(position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    class ShopProductsViewHolder(view: View): RecyclerView.ViewHolder(view){
        var itemImage: ImageView = view.findViewById(R.id.sage)
        var itemName: TextView = view.findViewById(R.id.titleProd)
        var itemUnit: TextView = view.findViewById(R.id.Unit)
        var itemPrice : TextView = view.findViewById(R.id.price)
    }
    interface ClickListener {
        fun clickedItem(position: Int)
    }
}