package project.app.cornershop

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Home : Navigation() {

    private lateinit var drugs : CardView
    private lateinit var stationary : CardView
    private lateinit var groceries : CardView
    private lateinit var electronics : CardView
    private lateinit var clothes : CardView
    private lateinit var cosmetics : CardView
    private lateinit var health_hygiene : CardView
    private lateinit var beverage : CardView
    private lateinit var food : CardView
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
        groceries = findViewById(R.id.cardGroceries)
        electronics = findViewById(R.id.cardElectronics)
        clothes = findViewById(R.id.cardClothes)
        cosmetics = findViewById(R.id.cardCosmetics)
        health_hygiene = findViewById(R.id.cardHealth)
        beverage = findViewById(R.id.cardBevarage)
        food = findViewById(R.id.cardFood)
        others = findViewById(R.id.cardOthers)

        titleName = findViewById(R.id.titleNav)
        titleName.setText("Home")


        val sharedPreferences: SharedPreferences = getSharedPreferences("Shared_Pref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        reference = database.getReference("Users")
        val userPhone = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null).toString()

        reference.child(userPhone).get().addOnSuccessListener {

            editor.apply {
                putString("Name",it.child("name").value.toString())
                putString("Location",it.child("loc").value.toString())
                putString("Business_Id",it.child("business_ID").value.toString())
                apply()
            }
        }

        toggleButton = findViewById(R.id.toggleSellerProf)
        toggleButton.setOnClickListener{
            val intent = Intent(this@Home, ManageBusiness::class.java)
            startActivity(intent)
        }

        ClickListeners()
    }
    fun ClickListeners() {

        val sharedPreferences: SharedPreferences = getSharedPreferences("Shared_Pref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

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
            val intent = Intent(this@Home, ShopListerClass::class.java)
            editor.apply{
                putString("category_Id","1")
                apply()
            }
            startActivity(intent)
        }
        stationary.setOnClickListener{
            val intent = Intent(this@Home, ShopListerClass::class.java)
            editor.apply{
                putString("category_Id","2")
                apply()
            }
            startActivity(intent)
        }
        groceries.setOnClickListener{
            val intent = Intent(this@Home, ShopListerClass::class.java)
            editor.apply{
                putString("category_Id","3")
                apply()
            }
            startActivity(intent)
        }
        electronics.setOnClickListener{
            val intent = Intent(this@Home, ShopListerClass::class.java)
            editor.apply{
                putString("category_Id","4")
                apply()
            }
            startActivity(intent)
        }
        clothes.setOnClickListener{
            val intent = Intent(this@Home, ShopListerClass::class.java)
            editor.apply{
                putString("category_Id","5")
                apply()
            }
            startActivity(intent)
        }
        cosmetics.setOnClickListener {
            val intent = Intent(this@Home, ShopListerClass::class.java)
            editor.apply{
                putString("category_Id","6")
                apply()
            }
            startActivity(intent)
        }
        health_hygiene.setOnClickListener {
            val intent = Intent(this@Home, ShopListerClass::class.java)
            editor.apply{
                putString("category_Id","7")
                apply()
            }
            startActivity(intent)
        }
        beverage.setOnClickListener {
            val intent = Intent(this@Home, ShopListerClass::class.java)
            editor.apply{
                putString("category_Id","8")
                apply()
            }
            startActivity(intent)
        }
        food.setOnClickListener {
            val intent = Intent(this@Home, ShopListerClass::class.java)
            editor.apply{
                putString("category_Id","9")
                apply()
            }
            startActivity(intent)
        }
        others.setOnClickListener {
            val intent = Intent(this@Home, ShopListerClass::class.java)
            editor.apply{
                putString("category_Id","10")
                apply()
            }
            startActivity(intent)
        }
    }
}
