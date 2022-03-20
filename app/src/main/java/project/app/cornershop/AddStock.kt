package project.app.cornershop

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class AddStock : AppCompatActivity() {


    private lateinit var database:FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var confirmStock : Button

    private lateinit var shopIdCurr : String
    private lateinit var itemIdCurr : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_stock)

        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")

        val itemName:EditText = findViewById(R.id.ItemName)
        val itemPrice:EditText = findViewById(R.id.ItemPrice)
        val itemUnit:EditText = findViewById(R.id.ItemUnit)
        val itemStock:EditText = findViewById(R.id.ItemStock)
        val itemDes:EditText = findViewById(R.id.ItemDes)

        reference = database.getReference("ItemVal")
        val FirebaseListenerS = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val child = snapshot.children
                child.forEach{
                    itemIdCurr = (it.value.toString().toInt() + 1).toString()
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        reference.addValueEventListener(FirebaseListenerS)


        val sharedPreferences: SharedPreferences = getSharedPreferences("Shared_Pref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        shopIdCurr =  getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Shop_Id",null).toString()

        confirmStock = findViewById(R.id.confirmStock)

        confirmStock.setOnClickListener{
            val nameString : String = itemName.text.toString()
            val priceString : String = itemPrice.text.toString()
            val unitString : String = "per "+itemUnit.text.toString()
            val stockString : String = itemStock.text.toString()
            val desString : String = itemDes.text.toString()


            val imgString = "https://firebasestorage.googleapis.com/v0/b/cornershopmanagement.appspot.com/o/1200px-Shop.svg.png?alt=media&token=6396b1bc-2b8c-40d2-acdf-82378eec4f62"


            database.getReference("ItemVal").child("CurrVal").setValue(itemIdCurr)
            reference = database.getReference("items")
            val info = ItemList(nameString,priceString,unitString,desString,imgString,itemIdCurr,stockString)

            reference.child(shopIdCurr).child(itemIdCurr).setValue(info)
            Toast.makeText(this@AddStock, "Stock Added Successfully", Toast.LENGTH_LONG).show()
            val intent = Intent(this@AddStock, StockList:: class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}
