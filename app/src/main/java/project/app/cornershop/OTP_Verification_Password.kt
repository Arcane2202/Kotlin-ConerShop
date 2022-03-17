package project.app.cornershop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class OTP_Verification_Password : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var otpConfirm: Button
    lateinit var inp: EditText
    lateinit var Resend: TextView
    lateinit var auth: FirebaseAuth
    lateinit var resendToken:PhoneAuthProvider.ForceResendingToken
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var codeBySystem: String? = ""
    lateinit var phoneAuthCredential : PhoneAuthCredential

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification_password)
        otpConfirm = findViewById(R.id.confirmOTPp)
        inp = findViewById(R.id.otpText1p)
        Resend = findViewById(R.id.RESENDOTPp)

        codeBySystem = intent.getStringExtra("codeSent")

        val uPhone = intent.getStringExtra("uPhone")

        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")

        otpConfirm.setOnClickListener {
            val codeByUser:String = inp.text.toString()
            if(codeBySystem!=null) {
                phoneAuthCredential = PhoneAuthProvider.getCredential(
                    codeBySystem!!,codeByUser
                )
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                        override fun onComplete(task: Task<AuthResult>) {
                            if(task.isSuccessful()) {
                                val intent = Intent(this@OTP_Verification_Password, ResetPass::class.java)
                                intent.putExtra("uPhone",uPhone)
                                startActivity(intent)
                                finish()
                            }
                        }
                    })
            }
        }
        Resend.setOnClickListener{
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+88" + uPhone,
                60,
                TimeUnit.SECONDS,
                this@OTP_Verification_Password,
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        TODO("Not yet implemented")
                    }
                    override fun onVerificationFailed(p0: FirebaseException) {
                        TODO("Not yet implemented")
                    }
                    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                        Toast.makeText(this@OTP_Verification_Password, "OTP sent!!", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }
    }
}