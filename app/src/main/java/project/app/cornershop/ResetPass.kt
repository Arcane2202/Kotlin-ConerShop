package project.app.cornershop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ResetPass : AppCompatActivity() {

    private lateinit var pass : EditText
    private lateinit var conpass : EditText
    private lateinit var confirmChange : Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pass)

        val uPhone = intent.getStringExtra("uPhone")

        pass = findViewById(R.id.newPass)
        conpass = findViewById(R.id.newPassConfirm)
        confirmChange = findViewById(R.id.confirmOTPp)

        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")

        confirmChange.setOnClickListener{
            database.child(uPhone.toString()).child("pass").setValue(pass).addOnSuccessListener {
                Toast.makeText(this@ResetPass, "Successfully changed!", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(this@ResetPass, "Failed!", Toast.LENGTH_LONG).show()
            }
            finish()
        }
    }
}