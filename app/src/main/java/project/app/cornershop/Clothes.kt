package project.app.cornershop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.text.FieldPosition
import java.util.ArrayList

class Clothes : Navigation(),ClothesAdapter.ClickListener{

    private var layoutManager : RecyclerView.LayoutManager?=null
    private var adapter : RecyclerView.Adapter<ClothesAdapter.ClothesViewHolder>?=null

    var shoplist = ArrayList<ShopList>()

    private lateinit var recyclerView : RecyclerView
    private lateinit var database:FirebaseDatabase
    private lateinit var reference:DatabaseReference

    private lateinit var cartButton: ImageView
    private lateinit var ocrbutton: Button
    private lateinit var notiButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val v: View = inflater.inflate(R.layout.activity_clothes,null,false)
        drawerLayout.addView(v,0)

        val titleName:TextView = findViewById(R.id.titleNav)
        titleName.setText("Clothing Stores")

        cartButton = findViewById(R.id.cartClick)
        ocrbutton = findViewById(R.id.ocrClick)
        notiButton = findViewById(R.id.notiClick)

        cartButton.setOnClickListener{
            val intent = Intent(this@Clothes, Items::class.java)
            startActivity(intent)
        }
        ocrbutton.setOnClickListener{
            val intent = Intent(this@Clothes, Items::class.java)
            startActivity(intent)
        }
        notiButton.setOnClickListener{
            val intent = Intent(this@Clothes, Notifications::class.java)
            startActivity(intent)
        }


        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
        reference = database.getReference("Shops")

        val FirebaseListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val child = snapshot.child("5").children
                child.forEach{
                    var shops = ShopList(it.child("image").value.toString(),
                        it.child("name").value.toString(),
                        it.child("address").value.toString())
                    shoplist.add(shops)
                }
                adapter = ClothesAdapter(shoplist,this@Clothes)
                recyclerView.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {

            }

        }
        reference.addValueEventListener(FirebaseListener)
        layoutManager = GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager

    }


    override fun clickedItem(position: Int) {
        startActivity(Intent(this@Clothes, ShopProducts::class.java))
    }
}





















