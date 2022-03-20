package project.app.cornershop

import android.app.ActivityOptions
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Pair
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.log

class SignIn : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var PhoneNo: EditText
    private lateinit var Pasword: EditText
    private lateinit var signinClicked: Button
    private lateinit var logo: TextView
    private lateinit var layout: RelativeLayout
    private lateinit var logoim: View
    private lateinit var forgot : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val signupClick =findViewById<TextView>(R.id.signup)

        PhoneNo = findViewById(R.id.Phone)
        Pasword = findViewById(R.id.LoginPass)
        logo = findViewById(R.id.login)
        layout = findViewById(R.id.relout)
        logoim = findViewById(R.id.logoim)
        forgot = findViewById(R.id.forgetButton)

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

        /*forgot.setOnClickListener{
            val intent = Intent(this@SignIn, Forgot_password::class.java)
            PhoneNo.text.clear()
            Pasword.text.clear()
            startActivity(intent)
        }*/

        val sharedPreferences:SharedPreferences = getSharedPreferences("Shared_Pref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

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
                        editor.apply {
                            putString("Phone",logPhone)
                            putString("Pass", logPass)
                            apply()
                        }
                        val intent = Intent(this@SignIn, Home::class.java)
                        intent.putExtra("uNam",it.child("name").value.toString())
                        PhoneNo.text.clear()
                        Pasword.text.clear()
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
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

