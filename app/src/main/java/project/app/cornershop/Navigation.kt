package project.app.cornershop

import android.app.ActivityOptions
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.MenuItem
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

open class Navigation : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    private lateinit var imgMenu:ImageView
    private lateinit var navView:NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        drawerLayout = findViewById(R.id.drawerLayout)
        imgMenu = findViewById(R.id.imgMenu)
        navView = findViewById(R.id.navDrawer)
        navView.itemIconTintList=null
        imgMenu.setOnClickListener{
            drawerLayout.openDrawer(GravityCompat.START)
        }
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        if(item.itemId==R.id.profile){
            val intent = Intent(this@Navigation, Profile::class.java)
            overridePendingTransition(0,0)
            startActivity(intent)
        }
        else if(item.itemId==R.id.home) {
            val intent = Intent(this@Navigation, Home::class.java)
            overridePendingTransition(0,0)
            startActivity(intent)
        }
        else if(item.itemId==R.id.settings) {
            val intent = Intent(this@Navigation, Settings::class.java)
            overridePendingTransition(0,0)
            startActivity(intent)
        }
        else if(item.itemId==R.id.Orders) {
            val intent = Intent(this@Navigation, OrderHistory::class.java)
            overridePendingTransition(0,0)
            startActivity(intent)
        }
        else if(item.itemId==R.id.Terms) {
            val intent = Intent(this@Navigation, TermsAndConditions::class.java)
            overridePendingTransition(0,0)
            startActivity(intent)
        }
        else if(item.itemId==R.id.helpCenter) {
            val intent = Intent(this@Navigation, HelpCenter::class.java)
            overridePendingTransition(0,0)
            startActivity(intent)
        }
        else if(item.itemId==R.id.logOutNav) {
            val sharedPreferences: SharedPreferences = getSharedPreferences("Shared_Pref", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply {
                putString("Phone",null)
                apply()
            }
            val intent = Intent(this@Navigation, SignIn::class.java)
            overridePendingTransition(0,0)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        return false
    }
}