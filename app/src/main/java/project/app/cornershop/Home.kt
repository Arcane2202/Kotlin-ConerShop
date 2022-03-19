package project.app.cornershop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView

class Home : Navigation() {

    private lateinit var drugs : CardView
    private lateinit var stationary : CardView
    private lateinit var Groceries : CardView
    private lateinit var Electronics : CardView
    private lateinit var Clothes : CardView
    private lateinit var Cosmetics : CardView
    private lateinit var Health_Hygiene : CardView
    private lateinit var bevarage : CardView
    private lateinit var food_snacks : CardView
    private lateinit var others : CardView

    private lateinit var cartButton:ImageView
    private lateinit var ocrbutton:Button
    private lateinit var notiButton:ImageView
    private lateinit var titleName:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        if(getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null)==null) {
            Toast.makeText(this, "Please Login", Toast.LENGTH_LONG).show()
            val intent = Intent(this@Home, SignIn::class.java)
            startActivity(intent)
        }
        super.onCreate(savedInstanceState)
        val inflater:LayoutInflater = LayoutInflater.from(this)
        val v:View = inflater.inflate(R.layout.activity_home,null,false)
        drawerLayout.addView(v,0)

        drugs = findViewById(R.id.cardDrugs)
        stationary = findViewById(R.id.cardStationary)
        Groceries = findViewById(R.id.cardGroceries)
        Electronics = findViewById(R.id.cardElectronics)
        Clothes = findViewById(R.id.cardClothes)
        Cosmetics = findViewById(R.id.cardCosmetics)
        Health_Hygiene = findViewById(R.id.cardHealth)
        bevarage = findViewById(R.id.cardBevarage)
        food_snacks = findViewById(R.id.cardFood)
        others = findViewById(R.id.cardOthers)

        titleName = findViewById(R.id.titleNav)
        titleName.setText("Home")

        cartButton = findViewById(R.id.cartClick)
        ocrbutton = findViewById(R.id.ocrClick)
        notiButton = findViewById(R.id.notiClick)

        cartButton.setOnClickListener{
            val intent = Intent(this@Home, Items::class.java)
            startActivity(intent)
        }
        ocrbutton.setOnClickListener{
            val intent = Intent(this@Home, Items::class.java)
            startActivity(intent)
        }
        notiButton.setOnClickListener{
            val intent = Intent(this@Home, Notifications::class.java)
            startActivity(intent)
        }

        drugs.setOnClickListener{
            val intent = Intent(this@Home, Drugs::class.java)
            startActivity(intent)
        }
    }
}
