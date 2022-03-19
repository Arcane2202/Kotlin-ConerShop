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

class Drugs : Navigation(),DrugsAdapter.ClickListener {

    private var layoutManager : RecyclerView.LayoutManager?=null
    private var adapter : RecyclerView.Adapter<DrugsAdapter.DrugViewHolder>?=null

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
        val v: View = inflater.inflate(R.layout.activity_drugs,null,false)
        drawerLayout.addView(v,0)

        val titleName:TextView = findViewById(R.id.titleNav)
        titleName.setText("Drug Stores")

        cartButton = findViewById(R.id.cartClick)
        ocrbutton = findViewById(R.id.ocrClick)
        notiButton = findViewById(R.id.notiClick)

        cartButton.setOnClickListener{
            val intent = Intent(this@Drugs, Items::class.java)
            startActivity(intent)
        }
        ocrbutton.setOnClickListener{
            val intent = Intent(this@Drugs, Items::class.java)
            startActivity(intent)
        }
        notiButton.setOnClickListener{
            val intent = Intent(this@Drugs, Notifications::class.java)
            startActivity(intent)
        }


        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
        reference = database.getReference("Shops")

        val FirebaseListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val child = snapshot.child("1").children
                    child.forEach{
                        var shops = ShopList(it.child("image").value.toString(),
                            it.child("name").value.toString(),
                            it.child("address").value.toString())
                        shoplist.add(shops)
                    }
                    adapter = DrugsAdapter(shoplist,this@Drugs)
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
        startActivity(Intent(this@Drugs, ShopProducts::class.java))
    }
}





















