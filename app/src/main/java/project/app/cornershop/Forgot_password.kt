package project.app.cornershop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class Forgot_password : AppCompatActivity() {

    private lateinit var PhonNo: EditText
    private lateinit var changeBut: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        PhonNo = findViewById(R.id.PhoneNoF)
        changeBut = findViewById(R.id.SendButton)

        val uPhone = PhonNo.text.toString()
        changeBut.setOnClickListener{
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+88" + uPhone,
                60,
                TimeUnit.SECONDS,
                this@Forgot_password,
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        TODO("Not yet implemented")
                    }
                    override fun onVerificationFailed(p0: FirebaseException) {
                        TODO("Not yet implemented")
                    }
                    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                        val intent = Intent(this@Forgot_password, OTP_Verification_Password::class.java)
                        intent.putExtra("codeSent",p0)
                        intent.putExtra("uPhone",uPhone)
                        startActivity(intent)
                        finish()
                    }
                }
            )
        }
    }
}