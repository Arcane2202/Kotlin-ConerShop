package project.app.cornershop

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.ArrayList

class ShopListerClass : Navigation(),ShopListerClassAdapter.ClickListener {

    private var layoutManager : RecyclerView.LayoutManager?=null
    private var adapter : RecyclerView.Adapter<ShopListerClassAdapter.DrugViewHolder>?=null

    var shoplist = ArrayList<ShopList>()

    private lateinit var recyclerView : RecyclerView
    private lateinit var database:FirebaseDatabase
    private lateinit var reference:DatabaseReference

    private lateinit var cartButton: ImageView
    private lateinit var ocrbutton: Button
    private lateinit var notiButton: ImageView

    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val v: View = inflater.inflate(R.layout.activity_shop_lister_class,null,false)
        drawerLayout.addView(v,0)

        val titleName:TextView = findViewById(R.id.titleNav)

        cartButton = findViewById(R.id.cartClick)
        ocrbutton = findViewById(R.id.ocrClick)
        notiButton = findViewById(R.id.notiClick)

        cartButton.setOnClickListener{
            val intent = Intent(this@ShopListerClass, Items::class.java)
            startActivity(intent)
        }
        ocrbutton.setOnClickListener{
            val intent = Intent(this@ShopListerClass, Items::class.java)
            startActivity(intent)
        }
        notiButton.setOnClickListener{
            val intent = Intent(this@ShopListerClass, Notifications::class.java)
            startActivity(intent)
        }

        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
        reference = database.getReference("Categories")
        val categoryClick = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("category_Id",null).toString()
        reference.get().addOnSuccessListener {
            titleName.setText(it.child(categoryClick).value.toString()+" Stores")
        }.addOnFailureListener{
            titleName.setText("Stores")
        }

        reference = database.getReference("Shops")
        val userLoc = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Location",null).toString()

        val FirebaseListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                shoplist.clear()
                val child = snapshot.child(categoryClick).children
                    child.forEach{
                        if(it.child("location").value.toString()==userLoc){
                            var shops = ShopList(it.child("image").value.toString(),
                                it.child("name").value.toString(),
                                it.child("address").value.toString(),
                                it.child("shop_Id").value.toString())
                            shoplist.add(shops)
                        }
                    }
                    adapter = ShopListerClassAdapter(shoplist,this@ShopListerClass)
                    recyclerView.adapter = adapter
                }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        reference.addValueEventListener(FirebaseListener)
        layoutManager = GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager

        sharedPreferences = getSharedPreferences("Shared_Pref", MODE_PRIVATE)
        editor = sharedPreferences.edit()

    }


    override fun clickedItem(position: Int) {
        editor.apply{
            putString("Shop_Id",shoplist[position].id)
            apply()
        }
        startActivity(Intent(this@ShopListerClass, ShopProducts::class.java))
    }
}





















