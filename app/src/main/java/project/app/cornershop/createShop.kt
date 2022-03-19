package project.app.cornershop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
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
    private lateinit var confirmShop : Button

    private lateinit var shopIdCurr : String
    private lateinit var businessCurr : String

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

        val shopName:EditText = findViewById(R.id.ShopName)
        val shopAddress:EditText = findViewById(R.id.ShopAddress)

        reference = database.getReference("ShopVal")
        val FirebaseListenerS = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val child = snapshot.children
                child.forEach{
                    shopIdCurr = (it.value.toString().toInt() + 1).toString()
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        reference.addValueEventListener(FirebaseListenerS)
        //reference.child("CurrVal").setValue(shopIdCurr)

        var tempBusId = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Business_Id",null).toString()
        if(tempBusId!="-1") {
            businessCurr = tempBusId
        } else {
            reference = database.getReference("BusVal")
            val FirebaseListenerB = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val child = snapshot.children
                    child.forEach{
                        businessCurr = (it.value.toString().toInt() + 1).toString()
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            }
            reference.addValueEventListener(FirebaseListenerB)
            //reference.child("CurentVal").setValue(businessCurr)
        }

        confirmShop = findViewById(R.id.confirmShop)
        confirmShop.setOnClickListener{
            val nameString : String = shopName.text.toString()
            val adrsString : String = shopAddress.text.toString()
            val locString : String = locations.selectedItem.toString()
            val catString: String = category.selectedItemPosition.toString()
            val imgString = "https://firebasestorage.googleapis.com/v0/b/cornershopmanagement.appspot.com/o/1200px-Shop.svg.png?alt=media&token=6396b1bc-2b8c-40d2-acdf-82378eec4f62"

            if(tempBusId!=businessCurr) {
                database.getReference("BusVal").child("CurentVal").setValue(businessCurr)
            }
            database.getReference("ShopVal").child("CurrVal").setValue(shopIdCurr)
            reference = database.getReference("Shops")
            val info = shopDetails(adrsString,catString,imgString,locString,nameString,businessCurr,shopIdCurr)
            reference.child(catString).child(shopIdCurr).setValue(info)
            finish()
        }
    }
}
