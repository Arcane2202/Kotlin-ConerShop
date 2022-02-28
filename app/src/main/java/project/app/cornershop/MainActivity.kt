package project.app.cornershop

import android.app.ActivityOptions
import android.content.Intent
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
                val options = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, Pair.create(img_logo,"logo"))
                val intent = Intent(this@MainActivity, SignIn::class.java)
                startActivity(intent, options.toBundle())
            }
        }, 700)
    }
}
