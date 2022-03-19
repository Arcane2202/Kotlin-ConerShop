package project.app.cornershop

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Pair
import android.view.View
import android.os.Handler as Handler

class MainActivity : AppCompatActivity() {

    private lateinit var img_logo:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        img_logo = findViewById(R.id.img_logo)

        Handler(Looper.getMainLooper()).postDelayed({
            run{
                val sharedPreferences:SharedPreferences = getSharedPreferences("Shared_Pref", MODE_PRIVATE)

                if(sharedPreferences.getString("Phone",null)!=null) {
                    val intent = Intent(this@MainActivity, Home::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                else {
                    val options = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, Pair.create(img_logo,"logo"))
                    val intent = Intent(this@MainActivity, SignIn::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent, options.toBundle())
                }
            }
        }, 700)
    }
}
