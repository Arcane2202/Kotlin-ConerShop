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
import com.nex3z.notificationbadge.NotificationBadge
import com.squareup.picasso.Picasso
import project.app.cornershop.databinding.ActivityNotificationsBinding

class OrderDetailsAdapter(private var itemList: MutableList<ItemCartData>, private var clickListener: ClickListener,var context: Context, var totalPrice:TextView):
    RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>(){

    val sharedPreferences : SharedPreferences = context.getSharedPreferences("Shared_Pref",Context.MODE_PRIVATE)
    val currUser : String = sharedPreferences.getString("Phone",null).toString()
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
    private var reference: DatabaseReference = database.getReference("Cart")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val layout: View = LayoutInflater.from(parent.context).inflate(R.layout.pending_packaging_card_view,parent,false)
        return OrderDetailsViewHolder(layout)
    }
    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {

    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    class OrderDetailsViewHolder(view: View): RecyclerView.ViewHolder(view){

    }
    interface ClickListener {
        fun clickedItem(position: Int)
    }
}