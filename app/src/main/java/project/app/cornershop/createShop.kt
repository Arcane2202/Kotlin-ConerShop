package project.app.cornershop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.database.*
import java.util.ArrayList

class createShop : AppCompatActivity() {

    private lateinit var category: Spinner
    var categorylist = ArrayList<String>()
    private lateinit var database:FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_shop)

        category = findViewById(R.id.ShopCat)
        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
        reference = database.getReference("Categories")
        categorylist.add("Select Category")
        val FirebaseListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val child = snapshot.children
                child.forEach{
                    categorylist.add(it.value.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        reference.addValueEventListener(FirebaseListener)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorylist)
        category.adapter = adapter
        category.setSelection(0)
    }
}