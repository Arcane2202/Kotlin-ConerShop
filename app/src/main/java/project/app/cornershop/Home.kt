package project.app.cornershop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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

    private lateinit var toggleButton : Button

    private lateinit var cartButton:ImageView
    private lateinit var ocrbutton:Button
    private lateinit var notiButton:ImageView
    private lateinit var titleName:TextView

    private lateinit var database:FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        if(getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null)==null) {
            Toast.makeText(this, "Please Login", Toast.LENGTH_LONG).show()
            val intent = Intent(this@Home, SignIn::class.java)
            startActivity(intent)
        }

        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
        reference = database.getReference("Users")

        val phone : String = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null).toString()

        reference.child(phone).get().addOnSuccessListener{
            if(it.child("buseiness_ID").value.toString()!="-1") {
                toggleButton.isEnabled = true;
            }
        }

        super.onCreate(savedInstanceState)
        val inflater:LayoutInflater = LayoutInflater.from(this)
        val v:View = inflater.inflate(R.layout.activity_home,null,false)
        drawerLayout.addView(v,1)

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



        toggleButton = findViewById(R.id.toggleSellerProf)

        drugs.setOnClickListener{
            val intent = Intent(this@Home, Drugs::class.java)
            startActivity(intent)
        }

        toggleButton.setOnClickListener{
            val intent = Intent(this@Home, SellerHome::class.java)
            startActivity(intent)
        }

        bottomClickListeners()
    }
    fun bottomClickListeners() {

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
    }
}
