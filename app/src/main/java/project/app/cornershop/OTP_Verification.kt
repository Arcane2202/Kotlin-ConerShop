package project.app.cornershop

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.*
import java.util.concurrent.TimeUnit

class OTP_Verification : Registration() {

    private lateinit var otpConfirm:Button
    lateinit var inp1:EditText
    lateinit var inp2:EditText
    lateinit var inp3:EditText
    lateinit var inp4:EditText
    lateinit var inp5:EditText
    lateinit var inp6:EditText
    lateinit var codeBySystem: String
    lateinit var phoneAuthCredential : PhoneAuthCredential

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)
        val phone:String = uPhone

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(uPhone)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        otpConfirm = findViewById(R.id.confirmOTP)
        inp1 = findViewById(R.id.otpText1)
        inp2 = findViewById(R.id.otpText2)
        inp3 = findViewById(R.id.otpText3)
        inp4 = findViewById(R.id.otpText4)
        inp5 = findViewById(R.id.otpText5)
        inp6 = findViewById(R.id.otpText6)

        otpConfirm.setOnClickListener {
            val codeByUser:String = inp1.text.toString()+inp2.text.toString()+inp3.text.toString()+inp4.text.toString()+inp5.text.toString()+inp6.text.toString()
            phoneAuthCredential = PhoneAuthProvider.getCredential(codeBySystem,codeByUser)
            FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(
                OnCompleteListener {
                    
                })
        }
    }
    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            codeBySystem = p0
        }

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            val code: String? = credential.smsCode

        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@OTP_Verification,"Failed",Toast.LENGTH_SHORT)
        }
    }
}
