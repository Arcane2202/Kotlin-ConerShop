package project.app.cornershop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class CreatingBusinessProfile : Navigation(),UserShoplistAdapter.ClickListener {

    private var layoutManager : RecyclerView.LayoutManager?=null
    private var adapter : RecyclerView.Adapter<UserShoplistAdapter.UserShoplistViewHolder>?=null

    var shoplist = ArrayList<ShopList>()

    private lateinit var recyclerView : RecyclerView
    private lateinit var database:FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var addButton : ImageView
    private lateinit var businessId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val v: View = inflater.inflate(R.layout.activity_creating_business_profile,null,false)
        drawerLayout.addView(v,1)

        val titleName: TextView = findViewById(R.id.titleNav)
        titleName.setText("User Stores")

        addButton = findViewById(R.id.addShopButton)

        addButton.setOnClickListener{
            val intent = Intent(this@CreatingBusinessProfile, createShop::class.java)
            startActivity(intent)
        }
        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
        reference = database.getReference("Users")

        val userPhone = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null).toString()
        reference.child(userPhone).get().addOnSuccessListener {
            businessId = it.child("business_ID").value.toString()
        }
        reference = database.getReference("Shops")
        val FirebaseListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val parent = snapshot.children
                parent.forEach {
                    val child = it.children
                    child.forEach {
                        if(it.child("business_ID").value.toString()==businessId) {
                            var shops = ShopList(
                                it.child("Image").value.toString(),
                                it.child("Name").value.toString(),
                                it.child("Address").value.toString()
                            )
                            shoplist.add(shops)
                        }
                    }
                }
                adapter = UserShoplistAdapter(shoplist,this@CreatingBusinessProfile)
                recyclerView.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }

        reference.addValueEventListener(FirebaseListener)
        layoutManager = GridLayoutManager(this,1, GridLayoutManager.VERTICAL,false)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager

    }

    override fun clickedItem(position: Int) {
        TODO("Not yet implemented")
    }
}