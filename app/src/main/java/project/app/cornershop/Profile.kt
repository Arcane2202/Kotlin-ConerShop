package project.app.cornershop

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Profile : Navigation() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        var titleName : TextView

        if(getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null)==null) {
            Toast.makeText(this, "Please Login", Toast.LENGTH_LONG).show()
            val intent = Intent(this@Profile, SignIn::class.java)
            startActivity(intent)
        }

        val sharedPreferences: SharedPreferences = getSharedPreferences("Shared_Pref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        super.onCreate(savedInstanceState)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val v: View = inflater.inflate(R.layout.activity_profile,null,false)
        drawerLayout.addView(v,0)

        titleName = findViewById(R.id.titleNav)
        titleName.setText("Profile")

        val name:TextView = findViewById(R.id.title_fullname)
        val phoneNo:TextView = findViewById(R.id.title_phone)
        val location : TextView = findViewById(R.id.locate)
        val editBut : Button = findViewById(R.id.editButton)
        val history:TextView = findViewById(R.id.title_orderHistory)
        val business : LinearLayout = findViewById(R.id.manageBusiness)



        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
        val userPhone = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null).toString()

        database.child(userPhone).get().addOnSuccessListener {
            name.setText(name.text.toString()+it.child("name").value.toString())
            phoneNo.setText(phoneNo.text.toString()+it.child("phone").value.toString())
            location.setText(location.text.toString()+it.child("loc").value.toString())

            editor.apply {
                putString("Name",it.child("name").value.toString())
                putString("Location",it.child("loc").value.toString())
                putString("Business_Id",it.child("business_ID").value.toString())
                apply()
            }
        }

        business.setOnClickListener{
            database.child(userPhone).get().addOnSuccessListener {
                    val intent = Intent(this@Profile, createShop :: class.java)
                    startActivity(intent)
            }
        }
    }
}
