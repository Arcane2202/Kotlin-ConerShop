package project.app.cornershop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        var nam = intent.getStringExtra("uNam")
        val show =findViewById<TextView>(R.id.welcm)
        nam = show.text.toString()+nam
        show.setText(nam)
    }
}