package project.app.cornershop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.*
import com.nex3z.notificationbadge.NotificationBadge
import com.squareup.picasso.Picasso
import java.util.ArrayList

class ShopProducts : Navigation(),ShopProductsAdapter.ClickListener {

    private var layoutManager : RecyclerView.LayoutManager?=null
    private var adapter : RecyclerView.Adapter<ShopProductsAdapter.ShopProductsViewHolder>?=null

    var itemlist = ArrayList<ItemList>()

    private lateinit var recyclerView : RecyclerView
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var cartButton: ImageView
    private lateinit var ocrbutton: Button
    private lateinit var notiButton: ImageView
    private lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val v: View = inflater.inflate(R.layout.activity_shop_products,null,false)
        drawerLayout.addView(v,0)

        val titleName: TextView = findViewById(R.id.titleNav)

        cartButton = findViewById(R.id.cartClick)
        ocrbutton = findViewById(R.id.ocrClick)
        notiButton = findViewById(R.id.notiClick)

        cartButton.setOnClickListener{
            val intent = Intent(this@ShopProducts, Items::class.java)
            startActivity(intent)
        }
        ocrbutton.setOnClickListener{
            val intent = Intent(this@ShopProducts, Items::class.java)
            startActivity(intent)
        }
        notiButton.setOnClickListener{
            val intent = Intent(this@ShopProducts, Notifications::class.java)
            startActivity(intent)
        }

        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")

        val notiBadge : NotificationBadge = findViewById(R.id.notiBadge)
        cartItems(this@ShopProducts,notiBadge)
        val shopClick = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Shop_Id",null).toString()
        titleName.setText("Items")
        reference = database.getReference("items")
        val FirebaseListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemlist.clear()
                val child = snapshot.child(shopClick).children
                child.forEach{
                    if(it.child("stock").value.toString()!="0") {
                        var items = ItemList(
                            it.child("item").value.toString(),
                            it.child("price").value.toString(),
                            it.child("unit").value.toString(),
                            it.child("des").value.toString(),
                            it.child("image").value.toString(),
                            it.child("item_id").value.toString(),
                            it.child("stock").value.toString()
                        )
                        itemlist.add(items)
                    }
                }
                adapter = ShopProductsAdapter(itemlist,this@ShopProducts,this@ShopProducts)
                recyclerView.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        reference.addValueEventListener(FirebaseListener)
        layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager

    }


    override fun clickedItem(position: Int) {
        //startActivity(Intent(this@ShopProducts, ShopProducts::class.java))
        /*val confirmView = LayoutInflater.from(this@ShopProducts).inflate(R.layout.activity_adding_item_in_cart,null)
        val builderVar = AlertDialog.Builder(this@ShopProducts)
            .setView(confirmView)
        val alertDiag = builderVar.show()*/

        val confirmView = BottomSheetDialog(this@ShopProducts)
        confirmView.setContentView(R.layout.activity_adding_item_in_cart)
        confirmView.show()

        val itemCount:EditText = confirmView.findViewById(R.id.EditItemCount)!!
        val addButton : ShapeableImageView = confirmView.findViewById(R.id.AddButton)!!
        val removeButton : ShapeableImageView = confirmView.findViewById(R.id.RemoveButton)!!
        val img : ImageView = confirmView.findViewById(R.id.sage)!!
        val itemName:TextView = confirmView.findViewById(R.id.titleProd)!!
        val unititem : TextView = confirmView.findViewById(R.id.Unit)!!
        val itemPrice : TextView = confirmView.findViewById(R.id.price)!!
        val itemDesc : TextView = confirmView.findViewById(R.id.prodDesc)!!

        removeButton.isEnabled = itemCount.text.toString() != "0"
        addButton.setOnClickListener {
            itemCount.setText((itemCount.text.toString().toInt()+1).toString())
            removeButton.isEnabled = true
        }
        removeButton.setOnClickListener {
            itemCount.setText((itemCount.text.toString().toInt()-1).toString())
            if(itemCount.text.toString()=="0") {
                removeButton.isEnabled = false
            }
        }

        Picasso.get().load(itemlist[position].image).into(img)
        itemName.setText(itemlist[position].item)
        unititem.setText(itemlist[position].unit)
        itemPrice.setText(itemlist[position].price)
        itemDesc.setText(itemlist[position].des)
        val itemId = itemlist[position].item_id

        val shopClick = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Shop_Id",null).toString()
        val currUser = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null).toString()


        confirmView.findViewById<Button>(R.id.AddtoCartButton)!!.setOnClickListener {
            if(itemCount.text.toString() == "0"){
                Toast.makeText(this@ShopProducts,"Select Atleast One Item to Add", Toast.LENGTH_LONG).show()
            } else {
                //alertDiag.dismiss()
                confirmView.dismiss()
                reference = database.getReference("Cart")
                database.getReference("Cart").child(currUser).get().addOnSuccessListener {
                    if(it.exists()) {
                        reference.child(currUser).child("shop_id").get().addOnSuccessListener {
                            if(it.value.toString()==shopClick) {
                                ref = database.getReference("Cart").child(currUser).child("cart_items")
                                ref.child(itemId).get().addOnSuccessListener {
                                    if(it.exists()) {
                                        val prevCount = it.child("quantity").value.toString().toInt()
                                        val newCount = prevCount + itemCount.text.toString().toInt()
                                        ref.child(itemId).child("quantity").setValue(newCount)
                                        Toast.makeText(this@ShopProducts,(itemlist[position].item+" Was Added To Cart"), Toast.LENGTH_LONG).show()
                                    } else {
                                        val itemDet = ItemCartData(itemName.text.toString(),itemCount.text.toString(),shopClick,itemPrice.text.toString(),itemId, itemlist[position].image)
                                        ref.child(itemId).setValue(itemDet).addOnSuccessListener {
                                            Toast.makeText(this@ShopProducts,(itemlist[position].item+" Was Added To Cart"), Toast.LENGTH_LONG).show()
                                        }.addOnFailureListener {
                                            Toast.makeText(this@ShopProducts,(itemlist[position].item+" Could Not Be Added To Cart"), Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                            } else {
                                //Toast.makeText(this@ShopProducts,("Cannot items from different shops"), Toast.LENGTH_LONG).show()
                                //alertDiag.dismiss()
                                confirmView.dismiss()
                                val confirmView2 = LayoutInflater.from(this@ShopProducts).inflate(R.layout.confirm_replace,null)
                                val builderVar2 = AlertDialog.Builder(this@ShopProducts).setView(confirmView2)
                                val alertDiag2 = builderVar2.show()
                                val confirmAdd : Button = confirmView2.findViewById(R.id.confirmAdd)
                                val cancelAdd : Button = confirmView2.findViewById(R.id.cancelAdd)
                                confirmAdd.setOnClickListener {
                                    alertDiag2.dismiss()
                                    ref = database.getReference("Cart").child(currUser)
                                    ref.child("shop_id").setValue(shopClick)
                                    ref.child("cart_items").removeValue()
                                    val itemDet = ItemCartData(itemName.text.toString(),itemCount.text.toString(),shopClick,itemPrice.text.toString(),itemId, itemlist[position].image)
                                    ref.child("cart_items").child(itemId).setValue(itemDet).addOnSuccessListener {
                                        Toast.makeText(this@ShopProducts,(itemlist[position].item+" Was Added To Cart"), Toast.LENGTH_LONG).show()
                                    }.addOnFailureListener {
                                        Toast.makeText(this@ShopProducts,(itemlist[position].item+" Could Not Be Added To Cart"), Toast.LENGTH_LONG).show()
                                    }
                                }
                                cancelAdd.setOnClickListener {
                                    alertDiag2.dismiss()
                                    Toast.makeText(this@ShopProducts,"Item Was Not Added", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    } else {
                        confirmView.dismiss()
                            ref = database.getReference("Cart").child(currUser)
                            ref.child("shop_id").setValue(shopClick)
                            val itemDet = ItemCartData(itemName.text.toString(),itemCount.text.toString(),shopClick,itemPrice.text.toString(),itemId, itemlist[position].image)
                            ref.child("cart_items").child(itemId).setValue(itemDet).addOnSuccessListener {
                                Toast.makeText(this@ShopProducts,(itemlist[position].item+" Was Added To Cart"), Toast.LENGTH_LONG).show()
                            }.addOnFailureListener {
                                Toast.makeText(this@ShopProducts,(itemlist[position].item+" Could Not Be Added To Cart"), Toast.LENGTH_LONG).show()
                            }
                    }
                }

            }
        }
       /* confirmView.findViewById<ImageView>(R.id.closeDiag)!!.setOnClickListener {
            Toast.makeText(this@ShopProducts,"Closed", Toast.LENGTH_LONG).show()
            //alertDiag.dismiss()
            confirmView.dismiss()
        }*/
    }
}

