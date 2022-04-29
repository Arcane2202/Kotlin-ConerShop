package project.app.cornershop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.content.SharedPreferences
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*

class PendingPackaginAdapter(private var itemList: MutableList<orderData>, private var clickListener: ClickListener,var context: Context): OrderDetailsAdapter.ClickListener,
    RecyclerView.Adapter<PendingPackaginAdapter.PendingPackaginViewHolder>(){

    val sharedPreferences : SharedPreferences = context.getSharedPreferences("Shared_Pref",Context.MODE_PRIVATE)
    val currUser : String = sharedPreferences.getString("Phone",null).toString()
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
    private var reference: DatabaseReference = database.getReference("Cart")
    private var reference2: DatabaseReference = database.getReference("Cart")
    private var reference3: DatabaseReference = database.getReference("Cart")
    private lateinit var layoutManager : RecyclerView.LayoutManager
    private var cntxt : Context = context
    var list = ArrayList<ItemCartData>()

    private var adapter : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingPackaginViewHolder {
        val layout: View = LayoutInflater.from(parent.context).inflate(R.layout.pending_packaging_card_view,parent,false)
        return PendingPackaginViewHolder(layout)
    }
    override fun onBindViewHolder(holder: PendingPackaginViewHolder, position: Int) {
        holder.customerNumber.text = itemList[position].order_no
        holder.totalOrderPrice.text = itemList[position].total_price

       // adapter = OrderDetailsAdapter(itemList[2].items_det,this)
        holder.recyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.recyclerView.setHasFixedSize(true)
        holder.recyclerView.adapter = adapter

        holder.expand.setOnClickListener {
            holder.recyclerView.visibility = RecyclerView.VISIBLE
            holder.expand.visibility = ImageView.INVISIBLE
            holder.collapse.visibility = ImageView.VISIBLE
            reference = database.getReference("pending_orders").child(itemList[position].order_no).child("items_det")
            val FirebaseListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    val child = snapshot.children
                    child.forEach{
                        var items = ItemCartData(it.child("item").value.toString(),
                            it.child("quantity").value.toString(),
                            it.child("shop_Id").value.toString(),
                            it.child("uprice").value.toString(),
                            it.child("item_id").value.toString(),
                            it.child("image").value.toString()
                        )
                        list.add(items)
                    }
                    adapter = OrderDetailsAdapter(list,this@PendingPackaginAdapter)
                    holder.recyclerView.setHasFixedSize(true)
                    holder.recyclerView.adapter = adapter
                }
                override fun onCancelled(error: DatabaseError) {

                }
            }
            reference.addValueEventListener(FirebaseListener)
            layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.recyclerView.layoutManager = layoutManager
        }

        holder.comp.setOnClickListener {
            reference2 = database.getReference("completed_orders").child(itemList[position].order_no)
            database.getReference("pending_orders").child(itemList[position].order_no).get().addOnSuccessListener {
                reference2.child("customer").setValue(it.child("customer").value.toString())
                reference2.child("order_no").setValue(it.child("order_no").value.toString())
                reference2.child("shop_id").setValue(it.child("shop_id").value.toString())
                reference2.child("status").setValue("completed")
                reference2.child("total_price").setValue(it.child("total_price").value.toString())
            }
            reference3 = database.getReference("completed_orders").child(itemList[position].order_no).child("items_det")
            database.getReference("pending_orders").child(itemList[position].order_no).child("items_det").get().addOnSuccessListener {
                var child = it.children
                child.forEach{

                    reference3.child(it.child("item_id").value.toString()).child("item").setValue(it.child("item").value.toString())
                    reference3.child(it.child("item_id").value.toString()).child("quantity").setValue(it.child("quantity").value.toString())
                    reference3.child(it.child("item_id").value.toString()).child("shop_Id").setValue(it.child("shop_Id").value.toString())
                    reference3.child(it.child("item_id").value.toString()).child("uprice").setValue(it.child("uprice").value.toString())
                    reference3.child(it.child("item_id").value.toString()).child("item_id").setValue(it.child("item_id").value.toString())
                    reference3.child(it.child("item_id").value.toString()).child("image").setValue(it.child("image").value.toString())
                }
            }
            database.getReference("pending_orders").child(itemList[position].order_no).removeValue()
        }

        holder.collapse.setOnClickListener {
            holder.recyclerView.visibility = RecyclerView.INVISIBLE
            holder.expand.visibility = ImageView.VISIBLE
            holder.collapse.visibility = ImageView.INVISIBLE
        }
        holder.itemView.setOnClickListener{
            clickListener.clickedItem(position)
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class PendingPackaginViewHolder(view: View): RecyclerView.ViewHolder(view){
        var customerNumber : TextView = view.findViewById(R.id.titleProd)
        var totalOrderPrice : TextView = view.findViewById(R.id.totprice)
        var expand : ImageView = view.findViewById(R.id.expandM)
        var collapse : ImageView = view.findViewById(R.id.expandL)
        var recyclerView : RecyclerView = view.findViewById(R.id.recyclerViewDet)
        //var scroller : NestedScrollView = view.findViewById(R.id.nesterscrollView)
       // var childlay : RelativeLayout = view.findViewById(R.id.childlay)
         var parlay : RelativeLayout = view.findViewById(R.id.parLay)
        var comp : ImageView = view.findViewById(R.id.changeStatus)
    }
    interface ClickListener {
        fun clickedItem(position: Int)
    }

    override fun clickedItem(position: Int) {
        TODO("Not yet implemented")
    }
}