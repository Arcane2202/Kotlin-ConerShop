package project.app.cornershop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.database.*

class Registration : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var fullName: EditText
    private lateinit var PhonNo:EditText
    private lateinit var Password:EditText
    private lateinit var ConfirmPassword:EditText
    private lateinit var confirmClicked: Button
    private lateinit var dropdownSelect: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
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
        val signin_click =findViewById<TextView>(R.id.loginnow)

        signin_click.setOnClickListener(View.OnClickListener {
            var intent = Intent(this@Registration, MainActivity::class.java)
            startActivity(intent)
        })

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
                        val users = User(uName, uPhone, area, passwrd)
                        database.child(uPhone).setValue(users).addOnSuccessListener {
                            Toast.makeText(
                                this@Registration,
                                "Successfully Signed Up!",
                                Toast.LENGTH_LONG
                            ).show()

                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this@Registration, "Sign-Up Failed!", Toast.LENGTH_LONG)
                                .show()
                        }
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

