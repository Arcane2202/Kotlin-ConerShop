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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.ArrayList

class ManageBusiness  : Navigation(),ManageBusinessAdapter.ClickListener {

    private var layoutManager : RecyclerView.LayoutManager?=null
    private var adapter : RecyclerView.Adapter<ManageBusinessAdapter.ManageBusinessViewHolder>?=null

    var shoplist = ArrayList<ShopList>()

    private lateinit var recyclerView : RecyclerView
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var addButton : ImageView

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor : SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val v: View = inflater.inflate(R.layout.activity_manage_business,null,false)
        drawerLayout.addView(v,0)

        val titleName: TextView = findViewById(R.id.titleNav)
        titleName.setText("User Stores")

        addButton = findViewById(R.id.addShopButton)
        addButton.setOnClickListener {
            val intent = Intent(this@ManageBusiness, createShop :: class.java)
            startActivity(intent)
        }

        sharedPreferences = getSharedPreferences("Shared_Pref", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
        reference = database.getReference("Shops")

        val bId : String = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Business_Id",null).toString()

        val firebaseListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                shoplist.clear()
                val par = snapshot.children
                par.forEach { snapshot2 ->
                    val child = snapshot2.children
                    child.forEach{
                        if(it.child("business_ID").value.toString()==bId){
                            var shops = ShopList(it.child("image").value.toString(),
                                it.child("name").value.toString(),
                                it.child("address").value.toString(),
                                it.child("shop_Id").value.toString()
                            )
                            shoplist.add(shops)
                        }
                    }
                    adapter = ManageBusinessAdapter(shoplist,this@ManageBusiness)
                    recyclerView.adapter = adapter
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        reference.addValueEventListener(firebaseListener)
        layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager

    }

    override fun clickedItem(position: Int) {
        editor.apply{
            putString("Shop_Inter", shoplist[position].id)
            apply()
        }
        startActivity(Intent(this@ManageBusiness, SellerHome::class.java))
    }
}