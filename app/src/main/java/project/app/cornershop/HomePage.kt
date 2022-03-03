package project.app.cornershop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class HomePage : AppCompatActivity() {

    private lateinit var tog : ActionBarDrawerToggle
    private lateinit var drawlay : DrawerLayout
    private lateinit var navview : NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        var nam = intent.getStringExtra("uNam")
        val show =findViewById<TextView>(R.id.welcm)
        nam = show.text.toString()+nam+"!"
        show.text = nam

        drawlay = findViewById(R.id.drawerlayout)
        navview = findViewById(R.id.navView)
        tog = ActionBarDrawerToggle(this,drawlay,R.string.open,R.string.close)
        drawlay.addDrawerListener(tog)
        tog.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navview.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.prof -> Toast.makeText(applicationContext,"Clicked Profile",Toast.LENGTH_SHORT).show()
            }
            true
        }

    }
    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        if(tog.onOptionsItemSelected(item)) {
            return true
        }
        return  super.onOptionsItemSelected(item)
    }
}
