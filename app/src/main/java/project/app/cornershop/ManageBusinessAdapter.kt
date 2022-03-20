package project.app.cornershop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class ManageBusinessAdapter(private var shopList: MutableList<ShopList>, private var clickListener: ClickListener):RecyclerView.Adapter<ManageBusinessAdapter.ManageBusinessViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageBusinessViewHolder {
        val layout:View = LayoutInflater.from(parent.context).inflate(R.layout.manage_business_card_view,parent,false)
        return ManageBusinessViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ManageBusinessViewHolder, position: Int) {
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
    class ManageBusinessViewHolder(view: View):RecyclerView.ViewHolder(view){
        var shopImage: ImageView = view.findViewById(R.id.sage)
        var shopName: TextView = view.findViewById(R.id.titleProd)
        var shopAddress: TextView = view.findViewById(R.id.descripProd)

    }
    interface ClickListener {
        fun clickedItem(position: Int)
    }
}


















