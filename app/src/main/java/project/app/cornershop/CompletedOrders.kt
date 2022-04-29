package project.app.cornershop

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.ArrayList

class CompletedOrders:AppCompatActivity(), CompletedOrdersAdapter.ClickListener {

    private var layoutManager : RecyclerView.LayoutManager?=null
    private var adapter : RecyclerView.Adapter<CompletedOrdersAdapter.CompletedOrdersViewHolder>?=null
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    private lateinit var orderIdCurr:String
    var itemlist = ArrayList<orderData>()
    var itemdet = ArrayList<ItemCartData>()

    private lateinit var recyclerView : RecyclerView
    private lateinit var recyclerView2 : RecyclerView
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var currOrderId : String

    // private lateinit var notiBadge : NotificationBadge

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_orders)

        sharedPreferences = getSharedPreferences("Shared_Pref", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
        val shopInteract = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Shop_Inter",null).toString()
        val currUser = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null).toString()
        reference = database.getReference("completed_orders")
        val FirebaseListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemlist.clear()
                val child = snapshot.children
                child.forEach{
                    if(it.child("shop_id").value.toString()==shopInteract) {
                        var items = orderData(it.child("customer").value.toString(),it.child("order_no").value.toString(),it.child("shop_id").value.toString(),it.child("status").value.toString(),it.child("total_price").value.toString())
                        itemlist.add(items)
                    }
                    // Toast.makeText(this@CompletedOrdersg,it.value.toString(),Toast.LENGTH_LONG).show()
                }
                adapter = CompletedOrdersAdapter(itemlist,this@CompletedOrders, this@CompletedOrders)
                recyclerView.adapter = adapter

                editor.apply {
                    putString("CountCart",itemlist.size.toString())
                    apply()
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        reference.addValueEventListener(FirebaseListener)
        layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.recyclerViewPackage)
        recyclerView.layoutManager = layoutManager
        /* recyclerView2 = findViewById(R.id.recyclerViewDet)
         recyclerView2.layoutManager = layoutManager*/
    }

    override fun clickedItem(position: Int) {
        //startActivity(Intent(this@Items, StockEdit::class.java))
        //Toast.makeText(this@Items,"Item Clicked",Toast.LENGTH_LONG).show()
    }
}