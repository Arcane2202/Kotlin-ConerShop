package project.app.cornershop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import project.app.cornershop.databinding.ActivityNotificationsBinding

class OrderDetailsAdapter(private var itemList: MutableList<ItemCartData>, private var clickListener: ClickListener):
    RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>(){

  //  val sharedPreferences : SharedPreferences = context.getSharedPreferences("Shared_Pref",Context.MODE_PRIVATE)
   // val currUser : String = sharedPreferences.getString("Phone",null).toString()
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
    private var reference: DatabaseReference = database.getReference("Cart")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val layout: View = LayoutInflater.from(parent.context).inflate(R.layout.order_details_card_view,parent,false)
        return OrderDetailsViewHolder(layout)
    }
    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.itemname.text = itemList[position].item
        holder.quantity.text = itemList[position].quantity
        holder.itemView.setOnClickListener{
            clickListener.clickedItem(position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class OrderDetailsViewHolder(view: View): RecyclerView.ViewHolder(view){
        var itemname : TextView = view.findViewById(R.id.titleOrd)
        var quantity : TextView = view.findViewById(R.id.quantityOrd)

    }
    interface ClickListener {
        fun clickedItem(position: Int)
    }
}

