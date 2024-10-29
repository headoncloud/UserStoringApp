package com.headoncloud.personalproject.room_database_ver1

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.headoncloud.personalproject.room_database_ver1.database.UserDatabase
import com.headoncloud.personalproject.room_database_ver1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var listUsers : List<User> = emptyList()
    val sam = object : UserAdapter.IClickItemUser{
        override fun updateUser(user: User) {
            clickToUpdateUser(user)
        }

        override fun deleteUser(user: User) {
            clickToDeleteUser(user)
        }

    }

    private val updateActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == RESULT_OK){
            loadData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapter = UserAdapter(listUsers, sam)
        binding.rcvUser.adapter = adapter
        binding.rcvUser.layoutManager = LinearLayoutManager(this)

        binding.btnSubmit.setOnClickListener {
            addUser()
        }

        binding.btnDeleteAll.setOnClickListener {
            clickToDeleteAll()
        }
        
        binding.edtFindUser.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                clickToFindUser()
            }
            true
        }

//        binding.edtUsername.doOnTextChanged { text, _, _, _ ->
//            clickToFindUser(text.toString().trim())
//        }

        loadData()
    }

    private fun clickToFindUser() {
        val strFind = binding.edtFindUser.text.toString().trim()

        listUsers = UserDatabase.getInstance(this).userDao().findUser(strFind)
        val adapter = UserAdapter(listUsers, sam)
        binding.rcvUser.adapter = adapter
        hideSoftKeyboard()
    }

    private fun clickToDeleteAll() {
        AlertDialog.Builder(this)
            .setTitle("Confirm to delete all user")
            .setMessage("Are you sure to delete all?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                UserDatabase.getInstance(this).userDao().deleteAll()
                Toast.makeText(this, "Delete successfully", Toast.LENGTH_SHORT).show()

                loadData()
            })
            .setNegativeButton("No", null)
            .show()
    }

    private fun clickToUpdateUser(user: User) {
        val intent = ActivityUpdate.newIntentToActivityResult(this, user)
        updateActivityLauncher.launch(intent)
    }

    private fun clickToDeleteUser(user: User) {
        AlertDialog.Builder(this)
            .setTitle("Confirm to delete user")
            .setMessage("Are you sure?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                UserDatabase.getInstance(this).userDao().deleteUser(user)
                Toast.makeText(this, "Delete successfully", Toast.LENGTH_SHORT).show()

                loadData()
            })
            .setNegativeButton("No", null)
            .show()
    }

    private fun addUser() {
        val strUserName = binding.edtUsername.text.toString().trim()
        val strPhoneNumber = binding.edtPhonenumber.text.toString().trim()
        val strBirth = binding.edtBirth.text.toString().trim()

        if(TextUtils.isEmpty(strUserName) || TextUtils.isEmpty(strPhoneNumber) || TextUtils.isEmpty(strBirth)){
            return
        }

        val user: User = User(userName = strUserName, userPhoneNumber = strPhoneNumber, birth = strBirth)
        if(isUserExist(user)){
            Toast.makeText(
                this,
                "User is already exist!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        UserDatabase.getInstance(this).userDao().insertUser(user)
        Toast.makeText(
            this,
            "Add user successfully",
            Toast.LENGTH_SHORT
        ).show()


        binding.edtUsername.setText("")
        binding.edtPhonenumber.setText("")
        hideSoftKeyboard()

        loadData()
    }

    fun hideSoftKeyboard(){
        try {
            val inputMethodManager: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }catch (ex: NullPointerException){
            ex.printStackTrace()
        }
    }

    fun loadData(){
        listUsers = UserDatabase.getInstance(this).userDao().getListUser()
        val adapter = UserAdapter(listUsers, sam)
        binding.rcvUser.adapter = adapter
        Log.d("MainActivity_ROOM", "list = $listUsers")
    }

    fun isUserExist(user: User): Boolean{
        val list: List<User> = UserDatabase.getInstance(this).userDao().checkUser(user.userName)
        return list != null && !list.isEmpty()
    }
}