package com.headoncloud.personalproject.room_database_ver1

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.headoncloud.personalproject.room_database_ver1.database.UserDatabase
import com.headoncloud.personalproject.room_database_ver1.databinding.ActivityUpdateBinding

class ActivityUpdate : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private var bundleReceived: Bundle? = null
    private var userReceived: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bundleReceived = intent.extras
        if(bundleReceived != null){
            userReceived = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                bundleReceived?.getSerializable("object_user", User::class.java) as User
            } else{
                User(userName = "none", userPhoneNumber = "none")
            }
            binding.edtUsername.setText(userReceived?.userName)
            binding.edtPhonenumber.setText(userReceived?.userPhoneNumber)
        }

        binding.btnUpdate.setOnClickListener {
            updateSelectedUserInfo(userReceived!!)
        }
    }

    private fun updateSelectedUserInfo(user: User) {
        //Check if input info is empty
        val userName = binding.edtUsername.text.toString().trim()
        val userPhone = binding.edtPhonenumber.text.toString().trim()
        if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPhone)){
            Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show()
            return
        }

        //Update user info
        userReceived?.userName = userName
        userReceived?.userPhoneNumber = userPhone
        UserDatabase.getInstance(this).userDao().updateUser(userReceived!!)
        Toast.makeText(this, "Update user successfully!", Toast.LENGTH_SHORT).show()

        //Send back to MainActivity
        val intentResult = Intent()
        setResult(RESULT_OK, intentResult)
        finish()
    }

    private fun sendBackDataResult() {

    }

    companion object{
        fun newIntentToActivityResult(packageContext: Context, user: User) : Intent{
            val intent = Intent(packageContext, ActivityUpdate::class.java)
            val bundle = Bundle()
            bundle.putSerializable("object_user", user)
            intent.putExtras(bundle)
            return intent
        }
    }
}