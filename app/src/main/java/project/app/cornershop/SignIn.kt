package project.app.cornershop

import android.app.ActivityOptions
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Pair
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignIn : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var PhoneNo: EditText
    private lateinit var Pasword: EditText
    private lateinit var signinClicked: Button
    private lateinit var logo: TextView
    private lateinit var layout: RelativeLayout
    private lateinit var logoim: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val signupClick =findViewById<TextView>(R.id.signup)

        PhoneNo = findViewById(R.id.Phone)
        Pasword = findViewById(R.id.LoginPass)
        logo = findViewById(R.id.login)
        layout = findViewById(R.id.relout)
        logoim = findViewById(R.id.logoim)

        val anim = AnimationUtils.loadAnimation(this,R.anim.fadein)

        Handler(Looper.getMainLooper()).postDelayed({
            run{
                layout.visibility = View.VISIBLE
                layout.startAnimation(anim)
            }
       }, 300)


        signupClick.setOnClickListener {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@SignIn,
                Pair.create(logoim, "logo")
            )
            val intent = Intent(this@SignIn, Registration::class.java)
            PhoneNo.text.clear()
            Pasword.text.clear()
            startActivity(intent, options.toBundle())
        }


        signinClicked = findViewById(R.id.loginButton)

        signinClicked.setOnClickListener{


            val logPhone = PhoneNo.text.toString()
            val logPass = Pasword.text.toString()

            database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
            database.child(logPhone).get().addOnSuccessListener {
                if(it.exists()) {
                    val corPass = it.child("pass").value
                    if(logPass == corPass) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@SignIn, HomePage::class.java)
                        intent.putExtra("uNam",it.child("name").value.toString())
                        PhoneNo.text.clear()
                        Pasword.text.clear()
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@SignIn, "Incorrect Credentials", Toast.LENGTH_LONG).show()
                        Pasword.text.clear()
                    }
                } else {
                    Toast.makeText(this@SignIn, "No user found with this phone number", Toast.LENGTH_LONG).show()
                    PhoneNo.text.clear()
                    Pasword.text.clear()
                }
            }.addOnFailureListener{
                Toast.makeText(this@SignIn, "Login Failed", Toast.LENGTH_LONG).show()
            }
        }
    }
}

