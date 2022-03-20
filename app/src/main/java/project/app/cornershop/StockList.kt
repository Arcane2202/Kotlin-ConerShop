package project.app.cornershop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.ArrayList

class StockList : Navigation(),StockListAdapter.ClickListener {

    private var layoutManager : RecyclerView.LayoutManager?=null
    private var adapter : RecyclerView.Adapter<StockListAdapter.StockListViewHolder>?=null

    var itemlist = ArrayList<ItemList>()

    private lateinit var recyclerView : RecyclerView
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val v: View = inflater.inflate(R.layout.activity_stock_list,null,false)
        drawerLayout.addView(v,1)

        val titleName: TextView = findViewById(R.id.titleNav)

        val ocrClick : Button = findViewById(R.id.ocrClick)

        val addStockButton : ImageView = findViewById(R.id.addStockButton)

        addStockButton.setOnClickListener {
            startActivity(Intent(this@StockList, AddStock::class.java))
        }

        ocrClick.setOnClickListener{
            Toast.makeText(this,"Done",Toast.LENGTH_LONG).show()
        }

        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
        val shopClick = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Shop_Id",null).toString()
        titleName.setText("Stock")
        reference = database.getReference("items")
        val FirebaseListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemlist.clear()
                val child = snapshot.child(shopClick).children
                child.forEach{
                    if(it.child("stock").value.toString()!="0") {
                        var items = ItemList(it.child("item").value.toString(),
                            it.child("price").value.toString(),
                            it.child("unit").value.toString(),
                            it.child("des").value.toString(),
                            it.child("image").value.toString(),
                            it.child("item_id").value.toString(),
                            it.child("stock").value.toString()
                        )
                        itemlist.add(items)
                    }
                }
                adapter = StockListAdapter(itemlist,this@StockList)
                recyclerView.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        reference.addValueEventListener(FirebaseListener)
        layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager

    }


    override fun clickedItem(position: Int) {
        
    }
}

