package project.app.cornershop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View

class TermsAndConditions : Navigation() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val v: View = inflater.inflate(R.layout.activity_terms_and_conditions,null,false)
        drawerLayout.addView(v,0)
    }
}