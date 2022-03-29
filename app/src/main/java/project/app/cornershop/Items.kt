package project.app.cornershop

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.nex3z.notificationbadge.NotificationBadge
import java.util.ArrayList

class Items : Navigation(),ItemsAdapter.ClickListener {

    private var layoutManager : RecyclerView.LayoutManager?=null
    private var adapter : RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>?=null
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    private lateinit var orderIdCurr:String
    var itemlist = ArrayList<ItemCartData>()

    private lateinit var recyclerView : RecyclerView
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

   // private lateinit var notiBadge : NotificationBadge

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val v: View = inflater.inflate(R.layout.activity_items,null,false)
        drawerLayout.addView(v,1)

        val titleName: TextView = findViewById(R.id.titleNav)
        titleName.setText("Cart")

        val checkout : Button = findViewById(R.id.checkout)
        val totalPrice : TextView = findViewById(R.id.totalPrice)

        sharedPreferences = getSharedPreferences("Shared_Pref", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
        val shopClick = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Shop_Id",null).toString()
        val currUser = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null).toString()
        reference = database.getReference("Cart").child(currUser).child("cart_items")
        val FirebaseListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemlist.clear()
                val child = snapshot.children
                child.forEach{
                    var items = ItemCartData(it.child("item").value.toString(),
                        it.child("quantity").value.toString(),
                        it.child("shop_Id").value.toString(),
                        it.child("uprice").value.toString(),
                        it.child("item_id").value.toString(),
                        it.child("image").value.toString()
                    )
                    totalPrice.text = (totalPrice.text.toString().toInt() + it.child("uprice").value.toString().toInt()*it.child("quantity").value.toString().toInt()).toString()
                    itemlist.add(items)
                }
                adapter = ItemsAdapter(itemlist,this@Items, this@Items,totalPrice)
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
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager


        reference = database.getReference("ordereVal")
        val FirebaseListenerS = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val child = snapshot.children
                child.forEach{
                    orderIdCurr = (it.value.toString().toInt() + 1).toString()
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        reference.addValueEventListener(FirebaseListenerS)


        checkout.setOnClickListener {

            reference = database.getReference("Orders")

            for(i in 0..itemlist.size) {
                reference.child(itemlist[0].shop_Id).child(orderIdCurr).child("item_id").child(itemlist[i].item_id).child("item").setValue(itemlist[i].item)
                reference.child(itemlist[0].shop_Id).child(orderIdCurr).child("item_id").child(itemlist[i].item_id).child("quantity").setValue(itemlist[i].quantity)
                reference.child(itemlist[0].shop_Id).child(orderIdCurr).child("item_id").child(itemlist[i].item_id).child("uprice").setValue(itemlist[i].uprice)
            }
            reference.child(itemlist[0].shop_Id).child(orderIdCurr).child("phone").setValue(currUser)
            reference.child(itemlist[0].shop_Id).child(orderIdCurr).child("status").setValue("pending")


            val FirebaseListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    itemlist.clear()
                    val child = snapshot.child(currUser).children
                    child.forEach{
                        var items = ItemCartData(it.child("item").value.toString(),
                            it.child("quantity").value.toString(),
                            it.child("shop_Id").value.toString(),
                            it.child("uprice").value.toString(),
                            it.child("item_id").value.toString(),
                            it.child("image").value.toString()
                        )
                        totalPrice.text = (totalPrice.text.toString().toInt() + it.child("uprice").value.toString().toInt()*it.child("quantity").value.toString().toInt()).toString()
                        itemlist.add(items)
                    }
                    adapter = ItemsAdapter(itemlist,this@Items,this@Items,totalPrice)
                    recyclerView.adapter = adapter
                }
                override fun onCancelled(error: DatabaseError) {

                }
            }
            reference.addValueEventListener(FirebaseListener)

        }
    }

    override fun clickedItem(position: Int) {
        //startActivity(Intent(this@Items, StockEdit::class.java))
        Toast.makeText(this@Items,"Item Clicked",Toast.LENGTH_LONG).show()
    }
}

