package project.app.cornershop

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class CompletedOrdersAdapter(private var itemList: MutableList<orderData>, private var clickListener: ClickListener,var context: Context): COrderDetailsAdapter.ClickListener,
        RecyclerView.Adapter<CompletedOrdersAdapter.CompletedOrdersViewHolder>(){

        val sharedPreferences : SharedPreferences = context.getSharedPreferences("Shared_Pref",
            Context.MODE_PRIVATE)
        val currUser : String = sharedPreferences.getString("Phone",null).toString()
        private var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
        private var reference: DatabaseReference = database.getReference("Cart")
        private var reference2: DatabaseReference = database.getReference("Cart")
        private var reference3: DatabaseReference = database.getReference("Cart")
        private lateinit var layoutManager : RecyclerView.LayoutManager
        private var cntxt : Context = context
        var list = ArrayList<ItemCartData>()

        private var adapter : RecyclerView.Adapter<COrderDetailsAdapter.COrderDetailsViewHolder>?=null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedOrdersViewHolder {
            val layout: View = LayoutInflater.from(parent.context).inflate(R.layout.completed_orders_card_view,parent,false)
            return CompletedOrdersViewHolder(layout)
        }
        override fun onBindViewHolder(holder: CompletedOrdersViewHolder, position: Int) {
            holder.customerNumber.text = itemList[position].order_no
            holder.totalOrderPrice.text = itemList[position].total_price

            // adapter = COrderDetailsAdapter(itemList[2].items_det,this)
            holder.recyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.recyclerView.setHasFixedSize(true)
            holder.recyclerView.adapter = adapter

            holder.expand.setOnClickListener {
                holder.recyclerView.visibility = RecyclerView.VISIBLE
                holder.expand.visibility = ImageView.INVISIBLE
                holder.collapse.visibility = ImageView.VISIBLE
                reference = database.getReference("completed_orders").child(itemList[position].order_no).child("items_det")
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
                        adapter = COrderDetailsAdapter(list,this@CompletedOrdersAdapter)
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

        class CompletedOrdersViewHolder(view: View): RecyclerView.ViewHolder(view){
            var customerNumber : TextView = view.findViewById(R.id.titleProd)
            var totalOrderPrice : TextView = view.findViewById(R.id.totprice)
            var expand : ImageView = view.findViewById(R.id.expandM)
            var collapse : ImageView = view.findViewById(R.id.expandL)
            var recyclerView : RecyclerView = view.findViewById(R.id.recyclerViewDet)
            var parlay : RelativeLayout = view.findViewById(R.id.parLay)
        }
        interface ClickListener {
            fun clickedItem(position: Int)
        }

        override fun clickedItem(position: Int) {
            TODO("Not yet implemented")
        }
    }