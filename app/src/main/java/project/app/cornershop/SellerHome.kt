package project.app.cornershop

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView

class SellerHome : Navigation() {

    private lateinit var toggleButton : Button
    private lateinit var packages : CardView
    private lateinit var payment : CardView
    private lateinit var completed : CardView
    private lateinit var stock : CardView

    private lateinit var cartButton: ImageView
    private lateinit var ocrbutton:Button
    private lateinit var notiButton: ImageView
    private lateinit var titleName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val v: View = inflater.inflate(R.layout.activity_seller_home,null,false)
        drawerLayout.addView(v,1)

        titleName = findViewById(R.id.titleNav)
        titleName.setText("Shop Home")

        toggleButton = findViewById(R.id.toggleBuyerProf)
        packages = findViewById(R.id.cardPackages)
        stock = findViewById(R.id.cardStocks)
        completed = findViewById(R.id.cardCompleted)


        toggleButton.setOnClickListener{
            val intent = Intent(this@SellerHome, Home::class.java)
            startActivity(intent)
        }
        bottomClickListeners()
    }
    fun bottomClickListeners() {

        //cartButton = findViewById(R.id.cartClick)
        ocrbutton = findViewById(R.id.ocrClick)
        //notiButton = findViewById(R.id.notiClick)

        /*cartButton.setOnClickListener{
            val intent = Intent(this@SellerHome, Items::class.java)
            startActivity(intent)
        }*/
        ocrbutton.setOnClickListener{
            val intent = Intent(this@SellerHome, Items::class.java)
            startActivity(intent)
        }
       /* notiButton.setOnClickListener{
            val intent = Intent(this@SellerHome, Notifications::class.java)
            startActivity(intent)
        }*/

        stock.setOnClickListener{
            val intent = Intent(this@SellerHome, StockList::class.java)
            startActivity(intent)
        }

        packages.setOnClickListener{
            val intent = Intent(this@SellerHome, PendingPackaging::class.java)
            startActivity(intent)
        }
        completed.setOnClickListener{
            val intent = Intent(this@SellerHome, CompletedOrders::class.java)
            startActivity(intent)
        }
    }
}
