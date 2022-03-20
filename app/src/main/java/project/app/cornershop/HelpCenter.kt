package project.app.cornershop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*

class HelpCenter : Navigation() {


    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var repCnt : String

    override fun onCreate(savedInstanceState: Bundle?) {

        if(getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null)==null) {
            Toast.makeText(this, "Please Login", Toast.LENGTH_LONG).show()
            val intent = Intent(this@HelpCenter, SignIn::class.java)
            startActivity(intent)
        }

        super.onCreate(savedInstanceState)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val v: View = inflater.inflate(R.layout.activity_help_center,null,false)
        drawerLayout.addView(v,0)
        val titleName: TextView = findViewById(R.id.titleNav)
        titleName.setText("Help Center")

        val Prob : EditText = findViewById(R.id.problem)
        val submitProb : Button = findViewById(R.id.submitProb)



        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")

        reference = database.getReference("repcnt")
        val FirebaseListenerS = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val child = snapshot.children
                child.forEach{
                    repCnt = (it.value.toString().toInt() + 1).toString()
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        reference.addValueEventListener(FirebaseListenerS)

        reference = database.getReference("report")

        submitProb.setOnClickListener {

            if(Prob.text.toString().length==0) {
                Toast.makeText(this,"Please Write Your Issue",Toast.LENGTH_LONG).show()
            } else {
                reference.child(repCnt).child("issue").setValue(Prob.text.toString())
                reference.child(repCnt).child("phone").setValue(getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null).toString())
                database.getReference("repcnt").child("crval").setValue(repCnt)
                Toast.makeText(this,"Issue submitted successfully",Toast.LENGTH_LONG).show()
                Prob.setText("")
            }
        }
    }
}
