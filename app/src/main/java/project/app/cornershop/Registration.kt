package project.app.cornershop

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Pair
import android.util.Patterns
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.getInstance
import com.google.firebase.database.*
import java.util.concurrent.TimeUnit

open class Registration : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var fullName: EditText
    protected lateinit var PhonNo:EditText
    private lateinit var Password:EditText
    private lateinit var ConfirmPassword:EditText
    private lateinit var confirmClicked: Button
    private lateinit var dropdownSelect: Spinner
    private lateinit var layout2: RelativeLayout
    private lateinit var logoim2: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        layout2 = findViewById(R.id.relout2)
        logoim2 = findViewById(R.id.logoim2)

        val anim = AnimationUtils.loadAnimation(this,R.anim.fadein)

        Handler(Looper.getMainLooper()).postDelayed({
            run{
                layout2.visibility = View.VISIBLE
                layout2.startAnimation(anim)
            }
        }, 300)


        dropdownSelect = findViewById(R.id.dropdownBox)
        val items = arrayOf("Select Area", "Dhanmondi", "Mohammadpur", "Shyamoli", "Moghbazar","Gulshan")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        dropdownSelect.adapter = adapter
        dropdownSelect.setSelection(0)

        fullName = findViewById(R.id.fullname)
        PhonNo = findViewById(R.id.Phone)
        Password = findViewById(R.id.SignupPass)
        ConfirmPassword = findViewById(R.id.ConfirmPass)
        confirmClicked = findViewById(R.id.confirmSignup)
        val signinClick =findViewById<TextView>(R.id.loginnow)

        signinClick.setOnClickListener {
            val intent = Intent(this@Registration, SignIn::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@Registration,
                Pair.create(logoim2, "logo")
            )
            fullName.text.clear()
            PhonNo.text.clear()
            Password.text.clear()
            ConfirmPassword.text.clear()
            dropdownSelect.setSelection(0)
            startActivity(intent, options.toBundle())
        }

        confirmClicked.setOnClickListener {
            val uName = fullName.text.toString()
            val uPhone = PhonNo.text.toString()
            val passwrd = Password.text.toString()
            val cpasswrd = ConfirmPassword.text.toString()
            val area = dropdownSelect.selectedItem.toString()



            if (uName.isEmpty() || uPhone.isEmpty() || passwrd.isEmpty() || cpasswrd.isEmpty()) {
                Toast.makeText(this, "Please fill up all the information!", Toast.LENGTH_LONG).show()
            }  else if (passwrd != cpasswrd) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_LONG).show()
            } else if(area=="Select Area") {
                Toast.makeText(this, "Please select an area!", Toast.LENGTH_LONG).show()
            } else if(!isPhoneValid(uPhone)){
                Toast.makeText(this, "Enter a valid phone number.", Toast.LENGTH_LONG).show()
            } else if(passwrd.length<6) {
                Toast.makeText(this, "Password must at least be 6 characters long.", Toast.LENGTH_LONG).show()
            } else {

                database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
                database.child(uPhone).get().addOnSuccessListener {
                    if(it.exists()) {
                        Toast.makeText(this, "This phone number is already registered!", Toast.LENGTH_LONG).show()
                    } else {

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+88" + uPhone,
                            60,
                            TimeUnit.SECONDS,
                            this@Registration,
                            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                                    TODO("Not yet implemented")
                                }
                                override fun onVerificationFailed(p0: FirebaseException) {
                                    TODO("Not yet implemented")
                                }
                                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                                    val intent = Intent(this@Registration, OTP_Verification::class.java)
                                    intent.putExtra("codeSent",p0)
                                    intent.putExtra("uName",uName)
                                    intent.putExtra("uPhone",uPhone)
                                    intent.putExtra("area",area)
                                    intent.putExtra("passwrd",passwrd)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        )
                    }
                }
            }
        }

    }
    private fun isPhoneValid(phone:String):Boolean {
        val pat = Patterns.PHONE
        return pat.matcher(phone).matches()
    }
}

