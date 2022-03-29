package project.app.cornershop

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.nex3z.notificationbadge.NotificationBadge

fun cartItems(context:Context,notiBadge : NotificationBadge) {
    var currUser = context.getSharedPreferences("Shared_Pref", AppCompatActivity.MODE_PRIVATE).getString("Phone",null).toString()
    var childCount : Int = 0
    var database : FirebaseDatabase = FirebaseDatabase.getInstance("https://cornershopmanagement-default-rtdb.asia-southeast1.firebasedatabase.app")
    var reference : DatabaseReference = database.getReference("Cart")
    val firebaseListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            childCount = snapshot.child(currUser).childrenCount.toInt()
            notiBadge.setNumber(childCount)
        }
        override fun onCancelled(error: DatabaseError) {

        }
    }
    reference.addValueEventListener(firebaseListener)
}