package project.app.cornershop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var PhoneNo: EditText
    private lateinit var Pasword: EditText
    private lateinit var signinClicked: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signup_click =findViewById<TextView>(R.id.signup)

        PhoneNo = findViewById(R.id.Phone)
        Pasword = findViewById(R.id.LoginPass)

        signup_click.setOnClickListener(View.OnClickListener {
            var intent = Intent(this@MainActivity, Registration::class.java)
            PhoneNo.text.clear()
            Pasword.text.clear()
            startActivity(intent)
        })


        signinClicked = findViewById(R.id.loginButton)

        signinClicked.setOnClickListener{


            val logPhone = PhoneNo.text.toString()
            val logPass = Pasword.text.toString()

            database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
            database.child(logPhone).get().addOnSuccessListener {
                if(it.exists()) {
                    val corPass = it.child("pass").value
                    if(logPass.equals(corPass)) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()
                        var intent = Intent(this@MainActivity, HomePage::class.java)
                        intent.putExtra("uNam",it.child("name").value.toString())
                        PhoneNo.text.clear()
                        Pasword.text.clear()
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@MainActivity, "Incorrect Credentials", Toast.LENGTH_LONG).show()
                        Pasword.text.clear()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "No user found with this phone number", Toast.LENGTH_LONG).show()
                    PhoneNo.text.clear()
                    Pasword.text.clear()
                }
            }.addOnFailureListener{
                Toast.makeText(this@MainActivity, "Login Failed", Toast.LENGTH_LONG).show()
            }
        }
    }
}