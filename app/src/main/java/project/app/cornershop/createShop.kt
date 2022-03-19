package project.app.cornershop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class createShop : AppCompatActivity() {

    private lateinit var category: Spinner
    private lateinit var locations : Spinner
    var categorylist = ArrayList<String>()
    var locationlist = ArrayList<String>()
    private lateinit var database:FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_shop)

        category = findViewById(R.id.ShopCat)
        locations = findViewById(R.id.ShopLoc)

        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
        reference = database.getReference("Categories")

        categorylist.add("Select Category")
        locationlist.add("Select Location")

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

        reference = database.getReference("Locations")
        val FirebaseListenerLoc = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val child = snapshot.children
                child.forEach{
                    locationlist.add(it.value.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        reference.addValueEventListener(FirebaseListenerLoc)
        val adapterloc = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locationlist)
        locations.adapter = adapterloc
        locations.setSelection(0)

        reference = database.getReference("Shops")
        reference.child(uPhone).get().addOnSuccessListener {
            
        }

    }
}
