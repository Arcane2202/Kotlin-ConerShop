package project.app.cornershop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.content.SharedPreferences
import android.media.Image
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ItemsAdapter(private var itemList: MutableList<ItemCartData>, private var clickListener: ClickListener,var context: Context):
    RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>() {

    val sharedPreferences : SharedPreferences = context.getSharedPreferences("Shared_Pref",Context.MODE_PRIVATE)
    val currUser : String = sharedPreferences.getString("Phone",null).toString()
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
    private var reference: DatabaseReference = database.getReference("Cart")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val layout: View = LayoutInflater.from(parent.context).inflate(R.layout.items_card_view,parent,false)
        return ItemsViewHolder(layout)
    }
    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        Picasso.get().load(itemList[position].image).into(holder.itemImage)
        holder.itemName.text = itemList[position].item
        holder.itemQuantity.text = itemList[position].quantity
        //holder.itemUnitPrice.text = ("Unit Price: BDT"+itemList[position].uprice)
        holder.itemTotalPrice.text = ("BDT "+(itemList[position].quantity.toInt()*itemList[position].uprice.toInt()).toString())
        holder.itemView.setOnClickListener{
            clickListener.clickedItem(position)
        }

        holder.addButton.setOnClickListener{
            holder.itemQuantity.text = (holder.itemQuantity.text.toString().toInt()+1).toString()
            updateCart(itemList[position].item_id, holder.itemQuantity.text.toString())
        }
        holder.redButton.setOnClickListener {
            if(holder.itemQuantity.text.toString()=="1") {
                deleteFromCart(itemList[position].item_id)
            }
            else {
                holder.itemQuantity.text = (holder.itemQuantity.text.toString().toInt()-1).toString()
                updateCart(itemList[position].item_id,holder.itemQuantity.text.toString())
            }
        }
        holder.deleteButton.setOnClickListener {
            deleteFromCart(itemList[position].item_id)
        }
    }

    private fun updateCart(position: String, quant : String) {
        reference.child(currUser).child(position).child("quantity").setValue(quant)
    }

    private fun deleteFromCart(position: String) {
        reference.child(currUser).child(position).removeValue()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    class ItemsViewHolder(view: View): RecyclerView.ViewHolder(view){
        var itemImage : ImageView = view.findViewById(R.id.itemImage)
        var itemName: TextView = view.findViewById(R.id.ItemName)
        var itemQuantity : TextView = view.findViewById(R.id.Quantity)
        var itemUnitPrice : TextView = view.findViewById(R.id.UnitPrice)
        var itemTotalPrice : TextView = view.findViewById(R.id.TotalPrice)
        val addButton : ImageView = view.findViewById(R.id.itemAddButton)
        val redButton : ImageView = view.findViewById(R.id.itemRedButton)
        var deleteButton : ImageView = view.findViewById(R.id.deleteItem)
    }
    interface ClickListener {
        fun clickedItem(position: Int)
    }
}