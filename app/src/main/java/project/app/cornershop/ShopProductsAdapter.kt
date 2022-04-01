package project.app.cornershop

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ShopProductsAdapter(private var itemList: MutableList<ItemList>, private var clickListener: ClickListener, context: Context):
    RecyclerView.Adapter<ShopProductsAdapter.ShopProductsViewHolder>(){

    val sharedPreferences : SharedPreferences = context.getSharedPreferences("Shared_Pref",Context.MODE_PRIVATE)
    val currUser : String = sharedPreferences.getString("Phone",null).toString()
    val shopClick : String = sharedPreferences.getString("Shop_Id",null).toString()
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
    private var reference: DatabaseReference = database.getReference("Cart")
    var cont : Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopProductsViewHolder {
        val layout: View = LayoutInflater.from(parent.context).inflate(R.layout.shop_products_card_view,parent,false)
        return ShopProductsViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ShopProductsViewHolder, position: Int) {
        holder.itemName.text = itemList[position].item
        holder.itemUnit.text = itemList[position].unit
        holder.itemPrice.text = (itemList[position].price + " BDT")
        Picasso.get().load(itemList[position].image).into(holder.itemImage)

        if(holder.itemQuantity.text.toString()=="0") {
            holder.redButton.isEnabled = false
            holder.addInToCart.isEnabled = false
            holder.addInToCart.visibility = Button.INVISIBLE
        }

        holder.addButton.setOnClickListener{
            holder.itemQuantity.text = (holder.itemQuantity.text.toString().toInt()+1).toString()
            if(holder.itemQuantity.text.toString()=="1") {
                holder.redButton.isEnabled = true
                holder.addInToCart.isEnabled = true
                holder.addInToCart.visibility = Button.VISIBLE
            }
        }
        holder.redButton.setOnClickListener {
            if(holder.itemQuantity.text.toString()!="0") {
                holder.itemQuantity.text = (holder.itemQuantity.text.toString().toInt()-1).toString()
            }
            if(holder.itemQuantity.text.toString()=="0") {
                holder.redButton.isEnabled = false
                holder.addInToCart.isEnabled = false
                holder.addInToCart.visibility = Button.INVISIBLE
            }
        }

        holder.addInToCart.setOnClickListener{
            var items = ItemCartData(itemList[position].item, holder.itemQuantity.text.toString(), shopClick, itemList[position].price, itemList[position].item_id, itemList[position].image)
            AddToCart(items)
            holder.itemQuantity.text = "0"
            holder.redButton.isEnabled = false
            holder.addInToCart.isEnabled = false
            holder.addInToCart.visibility = Button.INVISIBLE
        }

        holder.itemView.setOnClickListener{
            clickListener.clickedItem(position)
        }
    }


    private fun AddToCart(items: ItemCartData) {
        reference.child(currUser).get().addOnSuccessListener { user->
            if(user.exists()) {
                reference.child(currUser).child("shop_id").get().addOnSuccessListener() { shopcheck->
                    if(shopcheck.value.toString()==shopClick) {
                        reference.child(currUser).child("cart_items").get().addOnSuccessListener {
                            if(it.child(items.item_id).exists()) {
                                var newcount = (it.child(items.item_id).child("quantity").value.toString().toInt()+items.quantity.toInt()).toString()
                                reference.child(currUser).child("cart_items").child(items.item_id).child("quantity").setValue(newcount).addOnSuccessListener {
                                    Toast.makeText(cont,(items.item+" Was Added To Cart"), Toast.LENGTH_LONG).show()
                                }.addOnFailureListener {
                                    Toast.makeText(cont,(items.item+" Could Not Be Added To Cart"), Toast.LENGTH_LONG).show()
                                }
                            } else {
                                reference.child(currUser).child("cart_items").child(items.item_id).setValue(items).addOnSuccessListener {
                                    Toast.makeText(cont,(items.item+" Was Added To Cart"), Toast.LENGTH_LONG).show()
                                }.addOnFailureListener {
                                    Toast.makeText(cont,(items.item+" Could Not Be Added To Cart"), Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    } else {
                        val confirmView2 = LayoutInflater.from(cont).inflate(R.layout.confirm_replace,null)
                        val builderVar2 = AlertDialog.Builder(cont).setView(confirmView2)
                        val alertDiag2 = builderVar2.show()
                        val confirmAdd : Button = confirmView2.findViewById(R.id.confirmAdd)
                        val cancelAdd : Button = confirmView2.findViewById(R.id.cancelAdd)
                        confirmAdd.setOnClickListener {
                            alertDiag2.dismiss()
                            var ref : DatabaseReference = database.getReference("Cart").child(currUser)
                            ref.child("shop_id").setValue(shopClick)
                            ref.child("cart_items").removeValue()
                            ref.child("cart_items").child(items.item_id).setValue(items).addOnSuccessListener {
                                Toast.makeText(cont,(items.item+" Was Added To Cart"), Toast.LENGTH_LONG).show()
                            }.addOnFailureListener {
                                Toast.makeText(cont,(items.item+" Could Not Be Added To Cart"), Toast.LENGTH_LONG).show()
                            }
                        }
                        cancelAdd.setOnClickListener {
                            alertDiag2.dismiss()
                            Toast.makeText(cont,"Item Was Not Added", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                reference.child(currUser).child("shop_id").setValue(shopClick)
                reference.child(currUser).child("cart_items").child(items.item_id).setValue(items).addOnSuccessListener {
                    Toast.makeText(cont,(items.item+" Was Added To Cart"), Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(cont,(items.item+" Could Not Be Added To Cart"), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    class ShopProductsViewHolder(view: View): RecyclerView.ViewHolder(view){
        var itemImage: ImageView = view.findViewById(R.id.sage)
        var itemName: TextView = view.findViewById(R.id.titleProd)
        var itemUnit: TextView = view.findViewById(R.id.Unit)
        var itemPrice : TextView = view.findViewById(R.id.price)
        var addInToCart : Button = view.findViewById(R.id.AddtoCartButton)
        val itemQuantity: TextView = view.findViewById(R.id.EditItemCount)
        val addButton : ShapeableImageView = view.findViewById(R.id.AddButton)
        val redButton : ShapeableImageView = view.findViewById(R.id.RemoveButton)
    }

    interface ClickListener {
        fun clickedItem(position: Int)
    }
}
