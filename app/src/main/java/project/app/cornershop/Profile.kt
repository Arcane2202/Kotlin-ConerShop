package project.app.cornershop

import android.content.Intent
import android.content.SharedPreferences
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class Profile : Navigation() {

    private lateinit var database: DatabaseReference
    private  lateinit var ImageUri : Uri
    private lateinit var imgProfile : ShapeableImageView
    private lateinit var userPhone : String

    override fun onCreate(savedInstanceState: Bundle?) {



        var titleName : TextView

        if(getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null)==null) {
            Toast.makeText(this, "Please Login", Toast.LENGTH_LONG).show()
            val intent = Intent(this@Profile, SignIn::class.java)
            startActivity(intent)
        }

        val sharedPreferences: SharedPreferences = getSharedPreferences("Shared_Pref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        super.onCreate(savedInstanceState)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val v: View = inflater.inflate(R.layout.activity_profile,null,false)
        drawerLayout.addView(v,0)

        titleName = findViewById(R.id.titleNav)
        titleName.setText("Profile")

        val name:TextView = findViewById(R.id.title_fullname)
        val phoneNo:TextView = findViewById(R.id.title_phone)
        val location : TextView = findViewById(R.id.locate)
        val editBut : Button = findViewById(R.id.editButton)
        val history:TextView = findViewById(R.id.title_orderHistory)
        val business : LinearLayout = findViewById(R.id.manageBusiness)
        imgProfile = findViewById(R.id.imgProfile)
        val changeProfileImage : ImageView = findViewById(R.id.changeProfileImage)
        val cancelChangeImage : ImageView = findViewById(R.id.cancelChangeImage)
        val confirmChangeImage : ImageView = findViewById(R.id.confirmImageImage)

        val editButton:Button = findViewById(R.id.editButton)
        editBut.setOnClickListener{
            val intent = Intent(this@Profile, ProfileEditing :: class.java)
            startActivity(intent)
        }
        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
        userPhone = getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("Phone",null).toString()

        database.child(userPhone).get().addOnSuccessListener {
            name.setText(name.text.toString()+it.child("name").value.toString())
            phoneNo.setText(phoneNo.text.toString()+it.child("phone").value.toString())
            location.setText(location.text.toString()+it.child("loc").value.toString())
            Picasso.get().load(it.child("img").value.toString()).into(imgProfile)
            editor.apply {
                putString("Name",it.child("name").value.toString())
                putString("Location",it.child("loc").value.toString())
                putString("Business_Id",it.child("business_ID").value.toString())
                putString("userImg",it.child("img").value.toString())
                apply()
            }
        }
        changeProfileImage.setOnClickListener {
            cancelChangeImage.visibility = ImageView.VISIBLE
            confirmChangeImage.visibility = ImageView.VISIBLE
            val intent:Intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent,100)
        }

        confirmChangeImage.setOnClickListener {
            val filename = getSharedPreferences("Shared_Pref",MODE_PRIVATE).getString("Phone","null").toString() + "'s Profile Image"
            val storageReference = FirebaseStorage.getInstance("gs://cornershopmanagement.appspot.com").getReference("userimages/$filename")
            storageReference.putFile(ImageUri).addOnSuccessListener {

                val ref = FirebaseStorage.getInstance("gs://cornershopmanagement.appspot.com").getReference("userimages")

                ref.child(filename).downloadUrl.addOnSuccessListener {
                    database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
                    database.child(userPhone).child("img").setValue(it.toString())
                    Toast.makeText(this@Profile,"Successfully Changed",Toast.LENGTH_LONG).show()
                    confirmChangeImage.isVisible = false
                    cancelChangeImage.isVisible = false
                }.addOnFailureListener{
                    Toast.makeText(this@Profile,"Failed",Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener{
                Toast.makeText(this@Profile,"Failed",Toast.LENGTH_LONG).show()
            }
        }

        cancelChangeImage.setOnClickListener{
            Toast.makeText(this@Profile,"Cancelled",Toast.LENGTH_LONG).show()
            confirmChangeImage.isVisible = false
            cancelChangeImage.isVisible = false
            Picasso.get().load(getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("userImg","null").toString()).into(imgProfile)
        }

        business.setOnClickListener{
            val intent = Intent(this@Profile, ManageBusiness :: class.java)
            startActivity(intent)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == RESULT_OK) {
            ImageUri = data?.data!!
            Picasso.get().load(ImageUri).into(imgProfile)
        }
    }
}

















