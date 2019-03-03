package com.example.amprime.firebaseauth

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.drm.DrmStore
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import android.webkit.URLUtil
import android.widget.*

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile_information.*

import java.util.HashMap

class ProfileInformationActivity : AppCompatActivity() {
    lateinit var name: TextView
    lateinit var email: TextView
    lateinit var address: TextView
    lateinit var userType: TextView
    lateinit var usernameField: TextView
    lateinit var emailIdField: TextView
    lateinit var addressField: TextView
    lateinit var usertypeField: TextView
    private var infoContainer: RelativeLayout? = null
    private var auth: FirebaseAuth? = null
    private var reference: DatabaseReference? = null
    private var mauthListener: FirebaseAuth.AuthStateListener? = null
    internal lateinit var mProcessDialog: ProgressDialog
    private var srcUri: Uri? = null
    private var mProfileImage: ImageView? = null
    private var mStorageRefernce: StorageReference? = null
    private var userListItem: MenuItem? = null
    private lateinit var uploadTask: StorageTask<UploadTask.TaskSnapshot>
    private var typeface: Typeface? = null
    lateinit var drawerLayout: DrawerLayout


    override fun onStart() {
        auth!!.addAuthStateListener(mauthListener!!)
        super.onStart()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_information)
        val mainLayout: LinearLayout = findViewById(R.id.main_content)
        setUpDrawer()
        setUPToolbar()
//        val imageView = ImageView(actionBar!!.themedContext)
//        imageView.apply {
//            scaleType.apply { ImageView.ScaleType.CENTER }
//            setImageResource(R.mipmap.fb_main_launcher)
//
//        }
//        val layoutParams = ActionBar.LayoutParams(
//                ActionBar.LayoutParams.WRAP_CONTENT,
//                ActionBar.LayoutParams.WRAP_CONTENT, (Gravity.END or Gravity.CENTER_VERTICAL))
//        layoutParams.rightMargin = 40
//        imageView.layoutParams = layoutParams
//        actionBar.customView = imageView

        usernameField = findViewById(R.id.username_field)
        emailIdField = findViewById(R.id.email_id_field)
        addressField = findViewById(R.id.address_field)
        usertypeField = findViewById(R.id.designation_field)
        typeface = Typeface.createFromAsset(assets, "Raleway-Medium.ttf")
        drawerLayout = findViewById(R.id.drawer_layout)
        usernameField.typeface = typeface
        emailIdField.typeface = typeface
        addressField.typeface = typeface
        usertypeField.typeface = typeface

        name = findViewById(R.id.PI_name)
        email = findViewById(R.id.PI_email)
        email.movementMethod = ScrollingMovementMethod()
        address = findViewById(R.id.PI_address)
        userType = findViewById(R.id.PI_Designation)
        mProfileImage = findViewById(R.id.PI_profile_image)
        mProfileImage!!.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, SELECT_PHOTO)
        }
        infoContainer = findViewById(R.id.data_container)
        mProcessDialog = ProgressDialog(this)
        mProcessDialog.setMessage("Loading User Info......")
        mProcessDialog.setCancelable(false)

        auth = FirebaseAuth.getInstance()
        Log.d("Tag", "user" + auth!!)


        mauthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (auth!!.currentUser != null) {
                try {
                    mProcessDialog.show()
                } catch (e: Exception) {

                }

                mStorageRefernce = FirebaseStorage.getInstance().reference
                reference = FirebaseDatabase.getInstance().reference.child("Users")
                reference!!.child(firebaseAuth
                        .currentUser!!
                        .uid)
                        .addValueEventListener(
                                object : ValueEventListener {

                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            name.text = dataSnapshot.child("fullname").value.toString()
                                            address.text = dataSnapshot.child("address").value.toString()
                                            email.text = dataSnapshot.child("emailId").value.toString()
                                            userType.text = dataSnapshot.child("userType").value.toString()
                                            val UrlImg = dataSnapshot.child("imgUrl").value.toString()
                                            Log.d("TAG", "UrlImage: $UrlImg")
                                            if (URLUtil.isValidUrl(UrlImg)) {
                                                Glide.with(applicationContext)
                                                        .load(UrlImg)
                                                        .apply(RequestOptions.circleCropTransform())
                                                        .into(mProfileImage!!)
                                            }
                                            val UserType = dataSnapshot.child("userType").value.toString()
                                            Log.d("UserType: ", UserType)
                                            val preferences = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
                                            val editor = preferences.edit()
                                            editor.putString("role", UserType)
                                            editor.apply()
                                            mProcessDialog.dismiss()
                                        } else {
                                            mProcessDialog.dismiss()

                                        }

                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        Toast.makeText(applicationContext, "Error in Fetching$databaseError", Toast.LENGTH_LONG).show()
                                        mProcessDialog.dismiss()
                                    }
                                })


            }
        }
    }

    private fun setUPToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_search)
            setDisplayHomeAsUpEnabled(true)
        }


    }

    private fun setUpDrawer() {
        val navDrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener {
            navDrawerLayout.closeDrawers()
            return@setNavigationItemSelectedListener true
        }
        val actionBarDrawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(this, navDrawerLayout, R.string.open, R.string.close) {
            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
                val slideX = drawerView!!.width * slideOffset
                main_content.translationX = - slideX
                super.onDrawerSlide(drawerView, slideOffset)
            }
        }
        navDrawerLayout.addDrawerListener(actionBarDrawerToggle)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SELECT_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                srcUri = data.data
                Log.d("tag", "here" + srcUri!!)
                Toast.makeText(this, "File Path :" + srcUri!!.path!!, Toast.LENGTH_LONG).show()
                val filepath = mStorageRefernce!!.child("Photos").child(srcUri!!.lastPathSegment!!)
                mProcessDialog = ProgressDialog(this)
                mProcessDialog.max = 100
                mProcessDialog.setMessage("Loading....")
                mProcessDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                mProcessDialog.show()
                mProcessDialog.setCancelable(false)

                uploadTask = filepath.putFile(srcUri!!)
                uploadTask.addOnProgressListener { taskSnapshot ->
                    val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    mProcessDialog.incrementProgressBy(progress.toInt())
                }
                uploadTask.addOnFailureListener { Toast.makeText(this@ProfileInformationActivity, "Failure", Toast.LENGTH_SHORT).show() }.addOnSuccessListener { taskSnapshot ->
                    val downloadUri = taskSnapshot.downloadUrl
                    Toast.makeText(applicationContext, "Successfully Saved in Storage", Toast.LENGTH_SHORT).show()
                    Glide.with(this@ProfileInformationActivity)
                            .load(downloadUri)
                            .apply(RequestOptions.circleCropTransform())
                            .into(mProfileImage!!)


                    val reference = FirebaseDatabase.getInstance().getReference("Users").child(auth!!.currentUser!!.uid)

                    val userImage = HashMap<String, Any>()
                    userImage["imgUrl"] = downloadUri!!.toString()
                    reference.updateChildren(userImage)

                    mProcessDialog.dismiss()
                }


            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.profile_information_menu, menu)
        userListItem = menu.findItem(R.id.list_of_users)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val preferences = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val role = preferences.getString("role", "")

        if (role!!.matches(mUser.toRegex())) {

            when (item.itemId) {
                R.id.log_out_profile -> {
                    auth!!.signOut()

                    Log.d("item ID: ", item.itemId.toString())

                    val preferences1 = getSharedPreferences("login", Context.MODE_PRIVATE)
                    val editor = preferences1.edit()
                    editor.putBoolean("islogin", false)
                    editor.commit()
                    val intent = Intent(this@ProfileInformationActivity, EmailAndPasswordActivity::class.java)

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }

                R.id.change_password -> {
                    Log.d("item ID: ", item.itemId.toString())
                    changePassword()
                    return true
                }

                R.id.list_of_users -> {
                    Toast.makeText(applicationContext, "Not allowed to User", Toast.LENGTH_SHORT).show()
                    return true
                }
                R.id.edit_user_information -> {
                    showUpdateDialog()
                    return true
                }
                else -> return true
            }
        } else
            when (item.itemId) {
                android.R.id.home -> {
                    if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                        drawerLayout.closeDrawer(Gravity.RIGHT)
                    } else {
                        drawerLayout.openDrawer(Gravity.RIGHT)
                    }
                    return true

                }
                R.id.log_out_profile -> {


                    val preferences2 = getSharedPreferences("login", Context.MODE_PRIVATE)
                    val editor = preferences2.edit()
                    editor.putBoolean("islogin", false)
                    editor.commit()
                    val intent = Intent(this@ProfileInformationActivity, EmailAndPasswordActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
                R.id.change_password -> changePassword()
                R.id.list_of_users -> {
                    userListItem!!.isVisible = true
                    startActivity(Intent(this@ProfileInformationActivity, ListUser::class.java))
                    finish()
                    return true
                }
                R.id.edit_user_information -> {


                    showUpdateDialog()
                    return true
                }

                else -> return true
            }//                    finish();

        return super.onOptionsItemSelected(item)
    }

    private fun updateUser(userDesignation: String, userAddress: String): Boolean {

        val reference = FirebaseDatabase.getInstance().getReference("Users")
        val users = HashMap<String, Any>()
        users[auth!!.currentUser!!.uid + "/fullname"] = userDesignation
        users[auth!!.currentUser!!.uid + "/address"] = userAddress
        reference.updateChildren(users)
        return true
    }

    private fun showUpdateDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater

        val view = inflater.inflate(R.layout.edit_profile_information, null)
        builder.setView(view)

        val editTextUsername = view.findViewById<EditText>(R.id.edit_user_name)
        val editTextAddress = view.findViewById<EditText>(R.id.edit_user_address)

        val saveButton = view.findViewById<Button>(R.id.save_edited_user_data)

        val cancelButton = view.findViewById<Button>(R.id.cancel_edited_user_data)

        builder.setTitle("Update User Information")

        val dialog = builder.create()
        dialog.show()

        saveButton.setOnClickListener {
            val userName = editTextUsername.text.toString().trim { it <= ' ' }
            val userAddress = editTextAddress.text.toString().trim { it <= ' ' }
            Log.d("Tag", "userAddress: $userAddress")
            if (TextUtils.isEmpty(userAddress)) {
                editTextAddress.error = "Required"
                Log.d("Tag", "here")
            } else {
                updateUser(userName, userAddress)
                dialog.dismiss()

            }
        }
        cancelButton.setOnClickListener { dialog.dismiss() }

    }


    private fun changePassword() {
        val intent = Intent(this@ProfileInformationActivity, ChangePassword::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
        super.onBackPressed()
    }

    companion object {
        private val SELECT_PHOTO = 100
        private val mAdmin = "Admin"
        private val mUser = "User"
    }

}



