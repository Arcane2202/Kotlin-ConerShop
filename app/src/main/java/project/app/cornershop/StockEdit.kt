package project.app.cornershop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class StockEdit : AppCompatActivity() {

    private lateinit var itemimage : ImageView
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_edit)

        itemimage = findViewById(R.id.sage)


        val itemName : TextView = findViewById(R.id.titleProd)
        val unititem : TextView = findViewById(R.id.Unit)
        val itemPrice : TextView = findViewById(R.id.price)
        val addButton : Button = findViewById(R.id.AddButton)
        val removeButton : Button = findViewById(R.id.RemoveButton)
        val itemCount : EditText = findViewById(R.id.EditCount)
        val confrimEdit : Button = findViewById(R.id.ConfirmEditStock)

        val deleteItem : Button = findViewById(R.id.DeleteEditStock)

        val currItemId = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("item_id",null).toString()
        val currShopId = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Shop_Id",null).toString()

        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
        val shopClick = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Shop_Id",null).toString()
        reference = database.getReference("items")
        val FirebaseListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val child = snapshot.child(shopClick).children
                child.forEach{
                    if(it.child("item_id").value.toString()==currItemId) {
                        Picasso.get().load(it.child("image").value.toString()).into(itemimage)
                        itemName.setText(it.child("item").value.toString())
                        unititem.setText(it.child("unit").value.toString())
                        itemPrice.setText(it.child("price").value.toString())
                        itemCount.setText(it.child("stock").value.toString())
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        reference.addValueEventListener(FirebaseListener)

        removeButton.isEnabled = itemCount.text.toString() != "0"

        addButton.setOnClickListener {
            itemCount.setText((itemCount.text.toString().toInt()+1).toString())
            removeButton.isEnabled = true
        }
        removeButton.setOnClickListener {
            itemCount.setText((itemCount.text.toString().toInt()-1).toString())
            if(itemCount.text.toString()=="0") {
                removeButton.isEnabled = false
            }
        }

        deleteItem.setOnClickListener {
            reference.child(currShopId).child(currItemId).removeValue()
            Toast.makeText(this@StockEdit, "Item Removed Successfully", Toast.LENGTH_LONG).show()
            val intent = Intent(this@StockEdit, StockList :: class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        confrimEdit.setOnClickListener {
            val newCount = itemCount.text.toString()
            reference.child(currShopId).child(currItemId).child("stock").setValue(newCount)
            Toast.makeText(this@StockEdit, "Item Stock Updated Successfully", Toast.LENGTH_LONG).show()
            val intent = Intent(this@StockEdit, StockList :: class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}