package project.app.cornershop

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import java.util.ArrayList

class Settings : Navigation() {
    private var locationlist = ArrayList<String>()
    private lateinit var locEdit : Spinner

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        if (getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone", null) == null) {
            Toast.makeText(this, "Please Login", Toast.LENGTH_LONG).show()
            val intent = Intent(this@Settings, SignIn::class.java)
            startActivity(intent)
        }
        super.onCreate(savedInstanceState)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val v: View = inflater.inflate(R.layout.activity_settings, null, false)
        drawerLayout.addView(v, 0)
        val titleName: TextView = findViewById(R.id.titleNav)
        titleName.setText("Settings")

        val sharedPreferences:SharedPreferences = getSharedPreferences("Shared_Pref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val delete : TextView = findViewById(R.id.title_delAcc)
        delete.setOnClickListener {
            val confirmView = LayoutInflater.from(this).inflate(R.layout.confirm_delete,null)
            val builderVar = AlertDialog.Builder(this)
                .setView(confirmView)
            val alertDiag = builderVar.show()
            confirmView.findViewById<Button>(R.id.confirmDelete).setOnClickListener {
                Toast.makeText(this@Settings,"Confirmed",Toast.LENGTH_LONG).show()
                alertDiag.dismiss()
                database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
                reference = database.getReference("Users")
                reference.child(getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null).toString()).removeValue()
                val intent = Intent(this@Settings, SignIn::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            confirmView.findViewById<Button>(R.id.cancelDelete).setOnClickListener {
                Toast.makeText(this@Settings,"Cancelled",Toast.LENGTH_LONG).show()
                alertDiag.dismiss()
            }
        }
        val changepass : TextView = findViewById(R.id.title_oldpass)
        changepass.setOnClickListener {
            val confirmView = LayoutInflater.from(this).inflate(R.layout.change_pass,null)
            val builderVar = AlertDialog.Builder(this)
                .setView(confirmView)
            val alertDiag = builderVar.show()
            confirmView.findViewById<Button>(R.id.confirmEdit).setOnClickListener {

                val prevPass = confirmView.findViewById<EditText>(R.id.currentPass).text.toString()
                val newPass = confirmView.findViewById<EditText>(R.id.newPass).text.toString()
                val conPass = confirmView.findViewById<EditText>(R.id.confirmNewPass).text.toString()
                if(prevPass!=getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Pass",null).toString()){
                    Toast.makeText(this@Settings,"Current Password Incorrect",Toast.LENGTH_LONG).show()
                } else if(newPass!=conPass) {
                    Toast.makeText(this@Settings,"New Passwords Do Not Match",Toast.LENGTH_LONG).show()
                } else {
                    alertDiag.dismiss()
                    database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
                    reference = database.getReference("Users")
                    reference.child(getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null).toString()).child("pass").setValue(newPass)
                    Toast.makeText(this@Settings,"Password Changed Succesfully",Toast.LENGTH_LONG).show()
                    editor.apply{
                        putString("Pass", newPass)
                        apply()
                    }
                }
            }
            confirmView.findViewById<Button>(R.id.cancelEdit).setOnClickListener {
                Toast.makeText(this@Settings,"Cancelled",Toast.LENGTH_LONG).show()
                alertDiag.dismiss()
            }
        }

        val terms : TextView = findViewById(R.id.title_terms)
        terms.setOnClickListener {
            startActivity(Intent(this@Settings,TermsAndConditions::class.java))
        }
    }
}