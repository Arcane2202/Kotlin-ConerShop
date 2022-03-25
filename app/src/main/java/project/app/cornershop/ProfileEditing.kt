package project.app.cornershop

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.*
import java.util.ArrayList

class ProfileEditing : AppCompatActivity() {

    private lateinit var database:FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private var locationlist = ArrayList<String>()
    private lateinit var locEdit : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_editing)

        val fullnameEdit : EditText = findViewById(R.id.fullnameEdit)
        val phoneEdit : EditText = findViewById(R.id.PhoneEdit)
        locEdit  = findViewById(R.id.locEdit)

        val sharedPreferences: SharedPreferences = getSharedPreferences("Shared_Pref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val prevUserName = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Name",null).toString()
        val prevPhone = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null).toString()
        val prevLoc = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Location",null).toString()

        fullnameEdit.setText(prevUserName)
        phoneEdit.setText(prevPhone)

        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
        reference = database.getReference("Locations")
        locationlist.add("Select Area")
        val firebaseListenerLoc = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val child = snapshot.children
                child.forEach{
                    locationlist.add(it.value.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        reference.addValueEventListener(firebaseListenerLoc)
        val adapterloc = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locationlist)
        locEdit.adapter = adapterloc
        locEdit.setSelection(locationlist.indexOf(prevLoc))

        val confirmEdit:Button = findViewById(R.id.confirmEdit)
        confirmEdit.setOnClickListener{
            val uName = fullnameEdit.text.toString()
            val uPhone = phoneEdit.text.toString()
            val area = locEdit.selectedItem.toString()
            val pass = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Pass",null).toString()
            val business_ID = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Business_Id",null).toString()
            val img = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("userImg",null).toString()
            val users = User(uName, uPhone, area, pass,business_ID,img)
            reference = database.getReference("Users")
            reference.child(uPhone).setValue(users).addOnSuccessListener {
                Toast.makeText(this@ProfileEditing, "Successfully Edited!", Toast.LENGTH_LONG).show()
                reference.child(prevPhone).removeValue()
                editor.apply{
                    putString("Phone",uPhone)
                    putString("Pass", pass)
                    putString("Name",uName)
                    putString("Location",area)
                    apply()
                }
            }.addOnFailureListener {
                Toast.makeText(this@ProfileEditing, "Editing Failed!", Toast.LENGTH_LONG).show()
            }
            val intent = Intent(this@ProfileEditing, Profile::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}