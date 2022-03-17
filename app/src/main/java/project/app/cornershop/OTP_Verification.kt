package project.app.cornershop

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.concurrent.TimeUnit

class OTP_Verification : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var otpConfirm:Button
    lateinit var inp1:EditText
    lateinit var inp2:EditText
    lateinit var inp3:EditText
    lateinit var inp4:EditText
    lateinit var inp5:EditText
    lateinit var inp6:EditText
    lateinit var test:TextView
    lateinit var auth: FirebaseAuth
    lateinit var resendToken:PhoneAuthProvider.ForceResendingToken
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var codeBySystem: String? = ""
    lateinit var phoneAuthCredential : PhoneAuthCredential

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)

        otpConfirm = findViewById(R.id.confirmOTP)
        inp1 = findViewById(R.id.otpText1)
        inp2 = findViewById(R.id.otpText2)
        inp3 = findViewById(R.id.otpText3)
        inp4 = findViewById(R.id.otpText4)
        inp5 = findViewById(R.id.otpText5)
        inp6 = findViewById(R.id.otpText6)
        test = findViewById(R.id.test)

        codeBySystem = intent.getStringExtra("codeSent")

        val uName = intent.getStringExtra("uName")
        val uPhone = intent.getStringExtra("uPhone")
        val area = intent.getStringExtra("area")
        val passwrd = intent.getStringExtra("passwrd")

        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")

        otpConfirm.setOnClickListener {
            val codeByUser:String = inp1.text.toString()+inp2.text.toString()+inp3.text.toString()+inp4.text.toString()+inp5.text.toString()+inp6.text.toString()
            if(codeBySystem!=null) {
                phoneAuthCredential = PhoneAuthProvider.getCredential(
                    codeBySystem!!,codeByUser
                )
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                        override fun onComplete(task: Task<AuthResult>) {
                            if(task.isSuccessful()) {
                                Toast.makeText(this@OTP_Verification, "Successfully Signed Up!", Toast.LENGTH_LONG).show()
                                val users = User(uName, uPhone, area, passwrd)
                                database.child(uPhone.toString()).setValue(users).addOnSuccessListener {
                                    Toast.makeText(this@OTP_Verification, "Successfully Signed Up!", Toast.LENGTH_LONG).show()
                                }.addOnFailureListener {
                                    Toast.makeText(this@OTP_Verification, "Sign-Up Failed!", Toast.LENGTH_LONG).show()
                                }
                                finish()
                            }
                        }
                    })
            }
        }
    }
}
