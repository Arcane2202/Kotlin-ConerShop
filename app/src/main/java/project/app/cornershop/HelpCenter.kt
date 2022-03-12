package project.app.cornershop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View

class HelpCenter : Navigation() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val v: View = inflater.inflate(R.layout.activity_help_center,null,false)
        drawerLayout.addView(v,0)
    }
}