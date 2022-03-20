package project.app.cornershop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.ArrayList

class ShopProducts : Navigation(),ShopProductsAdapter.ClickListener {

    private var layoutManager : RecyclerView.LayoutManager?=null
    private var adapter : RecyclerView.Adapter<ShopProductsAdapter.ShopProductsViewHolder>?=null

    var itemlist = ArrayList<ItemList>()

    private lateinit var recyclerView : RecyclerView
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var cartButton: ImageView
    private lateinit var ocrbutton: Button
    private lateinit var notiButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val v: View = inflater.inflate(R.layout.activity_shop_products,null,false)
        drawerLayout.addView(v,0)

        val titleName: TextView = findViewById(R.id.titleNav)

        cartButton = findViewById(R.id.cartClick)
        ocrbutton = findViewById(R.id.ocrClick)
        notiButton = findViewById(R.id.notiClick)

        cartButton.setOnClickListener{
            val intent = Intent(this@ShopProducts, Items::class.java)
            startActivity(intent)
        }
        ocrbutton.setOnClickListener{
            val intent = Intent(this@ShopProducts, Items::class.java)
            startActivity(intent)
        }
        notiButton.setOnClickListener{
            val intent = Intent(this@ShopProducts, Notifications::class.java)
            startActivity(intent)
        }

        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
        val shopClick = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Shop_Id",null).toString()
        titleName.setText("Items")
        reference = database.getReference("items")
        val FirebaseListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemlist.clear()
                val child = snapshot.child(shopClick).children
                child.forEach{
                        var items = ItemList(it.child("item").value.toString(),
                            it.child("price").value.toString(),
                            it.child("unit").value.toString(),
                            it.child("des").value.toString(),
                            it.child("image").value.toString(),
                            it.child("item_id").value.toString()
                            )
                        itemlist.add(items)
                }
                adapter = ShopProductsAdapter(itemlist,this@ShopProducts)
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
        startActivity(Intent(this@ShopProducts, ShopProducts::class.java))
    }
}

