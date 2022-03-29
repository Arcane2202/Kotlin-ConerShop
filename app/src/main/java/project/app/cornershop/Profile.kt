package project.app.cornershop

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
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
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.util.ArrayList

class Profile : Navigation() {

    private lateinit var database: DatabaseReference
    private  lateinit var ImageUri : Uri
    private lateinit var imgProfile : ShapeableImageView
    private lateinit var userPhone : String

    private lateinit var changeProfileImage : ImageView
    private lateinit var cancelChangeImage : ImageView
    private lateinit var confirmChangeImage : ImageView

    private var flag : Boolean = false
    private var locationlist = ArrayList<String>()

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

        var name:EditText = findViewById(R.id.title_fullname)
        var phoneNo:EditText = findViewById(R.id.title_phone)
        val location : EditText = findViewById(R.id.locate)
        val editBut : Button = findViewById(R.id.editButton)
        val history:TextView = findViewById(R.id.title_orderHistory)
        val business : LinearLayout = findViewById(R.id.manageBusiness)
        val confirm : Button = findViewById(R.id.confirmEditButton)
        val cancel : Button = findViewById(R.id.cancelEditButton)

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

        val dropdownSelect : Spinner = findViewById(R.id.locEditBox)
        database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Locations")
        locationlist.add("Select Area")
        val FirebaseListenerLoc = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val child = snapshot.children
                child.forEach{
                    locationlist.add(it.value.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        database.addValueEventListener(FirebaseListenerLoc)
        val adapterloc = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locationlist)
        dropdownSelect.adapter = adapterloc
        dropdownSelect.setSelection(0)

        imgProfile = findViewById(R.id.imgProfile)
        changeProfileImage = findViewById(R.id.changeProfileImage)
        cancelChangeImage = findViewById(R.id.cancelChangeImage)
        confirmChangeImage = findViewById(R.id.confirmImageImage)

        val editButton:Button = findViewById(R.id.editButton)
        editBut.setOnClickListener{
            /*val intent = Intent(this@Profile, ProfileEditing :: class.java)
            startActivity(intent)*/
            name.isEnabled = true
            dropdownSelect.visibility = Spinner.VISIBLE
            location.visibility = EditText.INVISIBLE
            confirm.visibility = Button.VISIBLE
            cancel.visibility = Button.VISIBLE
            editButton.visibility = Button.INVISIBLE
            changeProfileImage.visibility = ImageView.VISIBLE
        }

        changeProfileImage.setOnClickListener {
            val intent:Intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent,100)
        }
        confirm.setOnClickListener {

            database = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")

            if(flag) {
                val filename = getSharedPreferences("Shared_Pref",MODE_PRIVATE).getString("Phone","null").toString() + "_profile_image"
                val storageReference = FirebaseStorage.getInstance("gs://cornershopmanagement.appspot.com").getReference("userimages/$filename")
                storageReference.putFile(ImageUri).addOnSuccessListener {
                    val ref = FirebaseStorage.getInstance("gs://cornershopmanagement.appspot.com").getReference("userimages")
                    ref.child(filename).downloadUrl.addOnSuccessListener {
                        database.child(userPhone).child("img").setValue(it.toString())
                        Toast.makeText(this@Profile,"Image Uploaded",Toast.LENGTH_LONG).show()
                        editor.apply {
                            putString("userImg",it.toString())
                            apply()
                        }
                    }.addOnFailureListener{
                        Toast.makeText(this@Profile,"Image Upload Failed",Toast.LENGTH_LONG).show()
                        Picasso.get().load(getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("userImg","null").toString()).into(imgProfile)
                    }
                }.addOnFailureListener{
                    Toast.makeText(this@Profile,"Image Upload Failed",Toast.LENGTH_LONG).show()
                    Picasso.get().load(getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("userImg","null").toString()).into(imgProfile)
                }
            }
                database.child(userPhone).child("name").setValue(name.text.toString())
                if(dropdownSelect.selectedItem.toString()!="Select Area") {
                    database.child(userPhone).child("loc").setValue(dropdownSelect.selectedItem.toString())
                    location.setText(dropdownSelect.selectedItem.toString())
                }
                Toast.makeText(this@Profile,"Profile Information Changed Successfully",Toast.LENGTH_LONG).show()
                editor.apply {
                    putString("Name",name.text.toString())
                    putString("Location",dropdownSelect.selectedItem.toString())
                    apply()
                }
                name.isEnabled = false
                dropdownSelect.visibility = Spinner.INVISIBLE
                location.visibility = EditText.VISIBLE
                confirm.visibility = Button.INVISIBLE
                cancel.visibility = Button.INVISIBLE
                editButton.visibility = Button.VISIBLE
                location.isEnabled = false
                changeProfileImage.visibility = ImageView.INVISIBLE
        }
        cancel.setOnClickListener{
            Toast.makeText(this@Profile,"Cancelled",Toast.LENGTH_LONG).show()
            Picasso.get().load(getSharedPreferences("Shared_Pref", MODE_PRIVATE).getString("userImg","null").toString()).into(imgProfile)
            name.setText(getSharedPreferences("Shared_Pref",MODE_PRIVATE).getString("Name","null").toString())
            location.setText(getSharedPreferences("Shared_Pref",MODE_PRIVATE).getString("Location","null").toString())
            name.isEnabled = false
            dropdownSelect.visibility = Spinner.INVISIBLE
            location.visibility = EditText.VISIBLE
            location.isEnabled = false
            confirm.visibility = Button.INVISIBLE
            cancel.visibility = Button.INVISIBLE
            editButton.visibility = Button.VISIBLE
            changeProfileImage.visibility = ImageView.INVISIBLE
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
            flag = true
        }
    }
}

















