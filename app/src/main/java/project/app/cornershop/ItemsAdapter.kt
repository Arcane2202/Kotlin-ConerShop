package project.app.cornershop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemsAdapter(private var itemList: MutableList<ItemCartData>, private var clickListener: ClickListener):
    RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val layout: View = LayoutInflater.from(parent.context).inflate(R.layout.items_card_view,parent,false)
        return ItemsViewHolder(layout)
    }
    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.itemName.text = itemList[position].item
        holder.itemQuantity.text = itemList[position].quantity
        holder.itemUnitPrice.text = (itemList[position].uprice + " BDT")
        holder.itemTotalPrice.text = ((itemList[position].quantity.toInt()*itemList[position].uprice.toInt()).toString()+ " BDT")
        holder.itemView.setOnClickListener{
            clickListener.clickedItem(position)
        }
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    class ItemsViewHolder(view: View): RecyclerView.ViewHolder(view){
        var itemName: TextView = view.findViewById(R.id.ItemName)
        var itemQuantity : TextView = view.findViewById(R.id.Quantity)
        var itemUnitPrice : TextView = view.findViewById(R.id.UnitPrice)
        var itemTotalPrice : TextView = view.findViewById(R.id.TotalPrice)
    }
    interface ClickListener {
        fun clickedItem(position: Int)
    }
}