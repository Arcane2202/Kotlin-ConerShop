package project.app.cornershop

import android.content.ContentValues.TAG
import android.content.Intent
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
    lateinit var inp:EditText
    lateinit var Resend:TextView
    lateinit var auth: FirebaseAuth
    lateinit var resendToken:PhoneAuthProvider.ForceResendingToken
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var codeBySystem: String? = ""
    lateinit var phoneAuthCredential : PhoneAuthCredential

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)

        otpConfirm = findViewById(R.id.confirmOTP)
        inp = findViewById(R.id.otpText1)
        Resend = findViewById(R.id.RESENDOTP)

        codeBySystem = intent.getStringExtra("codeSent")

        val uName = intent.getStringExtra("uName")
        val uPhone = intent.getStringExtra("uPhone")
        val area = intent.getStringExtra("area")
        val passwrd = intent.getStringExtra("passwrd")

        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")

        otpConfirm.setOnClickListener {
            val codeByUser:String = inp.text.toString()
            if(codeBySystem!=null) {
                phoneAuthCredential = PhoneAuthProvider.getCredential(
                    codeBySystem!!,codeByUser
                )
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                        override fun onComplete(task: Task<AuthResult>) {
                            if(task.isSuccessful()) {
                                Toast.makeText(this@OTP_Verification, "OTP Verified!", Toast.LENGTH_SHORT).show()
                                val users = User(uName, uPhone, area, passwrd,"-1")
                                database.child(uPhone.toString()).setValue(users).addOnSuccessListener {
                                    Toast.makeText(this@OTP_Verification, "Successfully Signed Up!", Toast.LENGTH_LONG).show()
                                }.addOnFailureListener {
                                    Toast.makeText(this@OTP_Verification, "Sign-Up Failed!", Toast.LENGTH_LONG).show()
                                }
                                finish()
                            } else {
                                Toast.makeText(this@OTP_Verification, "Wrong OTP", Toast.LENGTH_LONG).show()
                            }
                        }
                    }).addOnFailureListener{
                        Toast.makeText(this@OTP_Verification, "Sign-Up Failed!", Toast.LENGTH_LONG).show()
                    }
            }
        }
        Resend.setOnClickListener{
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+88" + uPhone,
                60,
                TimeUnit.SECONDS,
                this@OTP_Verification,
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        TODO("Not yet implemented")
                    }
                    override fun onVerificationFailed(p0: FirebaseException) {
                        TODO("Not yet implemented")
                    }
                    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                        Toast.makeText(this@OTP_Verification, "OTP sent!!", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }
    }
}
