package project.app.cornershop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class StockListAdapter (private var itemList: MutableList<ItemList>, private var clickListener: ClickListener):
    RecyclerView.Adapter<StockListAdapter.StockListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockListViewHolder {
        val layout: View = LayoutInflater.from(parent.context).inflate(R.layout.stock_list_card_view,parent,false)
        return StockListViewHolder(layout)
    }
    override fun onBindViewHolder(holder: StockListViewHolder, position: Int) {
        holder.itemName.text = itemList[position].item
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    class StockListViewHolder(view: View): RecyclerView.ViewHolder(view){
        var itemName: TextView = view.findViewById(R.id.titleProd)

    }
    interface ClickListener {
        fun clickedItem(position: Int)
    }
}