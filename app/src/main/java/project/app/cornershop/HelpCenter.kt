package project.app.cornershop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast

class HelpCenter : Navigation() {
    override fun onCreate(savedInstanceState: Bundle?) {

        if(getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null)==null) {
            Toast.makeText(this, "Please Login", Toast.LENGTH_LONG).show()
            val intent = Intent(this@HelpCenter, SignIn::class.java)
            startActivity(intent)
        }

        super.onCreate(savedInstanceState)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val v: View = inflater.inflate(R.layout.activity_help_center,null,false)
        drawerLayout.addView(v,0)
        val titleName: TextView = findViewById(R.id.titleNav)
        titleName.setText("Help Center")
    }
}
