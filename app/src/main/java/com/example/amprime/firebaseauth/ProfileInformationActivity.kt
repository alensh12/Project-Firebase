package com.example.amprime.firebaseauth

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.URLUtil
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_profile_information.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONObject
import java.util.*

class ProfileInformationActivity : AppCompatActivity() {
    lateinit var name: TextView
    lateinit var email: TextView
    lateinit var address: TextView
    lateinit var userType: TextView
    private var auth: FirebaseAuth? = null
    private var reference: DatabaseReference? = null
    private var mauthListener: FirebaseAuth.AuthStateListener? = null
    internal lateinit var mProcessDialog: ProgressDialog
    private var progressBar:LottieAnimationView? = null
    private var srcUri: Uri? = null
    private var mProfileImage: ImageView? = null
    private var mStorageRefernce: StorageReference? = null
    private var userListItem: MenuItem? = null
    private lateinit var uploadTask: StorageTask<UploadTask.TaskSnapshot>
    private var typeface: Typeface? = null
    lateinit var drawerLayout: DrawerLayout
    lateinit var settingImage: ImageView
    private var mainContainer: RelativeLayout? = null
    private var lottieLayout:RelativeLayout? = null


    override fun onStart() {
        auth!!.addAuthStateListener(mauthListener!!)
        super.onStart()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_information)
        mainContainer = findViewById(R.id.main_content)
        settingImage = findViewById(R.id.setting_imageview)
        progressBar = findViewById(R.id.lottie_view)
        lottieLayout  = findViewById(R.id.lottie_layout)
        lottieLayout?.apply {
            visibility = View.VISIBLE
        }

        setUpDrawer()
        setUPToolbar()
        fetchData()
        typeface = Typeface.createFromAsset(assets, "Raleway-Medium.ttf")
        drawerLayout = findViewById(R.id.drawer_layout)
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


        auth = FirebaseAuth.getInstance()
        Log.d("Tag", "user" + auth!!)


        mauthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (auth!!.currentUser != null) {
                try {
                    mProcessDialog.show()
                } catch (ee: Exception) {

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
                                            mainContainer?.visibility = View.VISIBLE
                                            lottieLayout?.visibility = View.GONE
                                        } else {

                                            mainContainer?.visibility = View.VISIBLE
                                            lottieLayout?.visibility = View.GONE

                                        }

                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        Toast.makeText(applicationContext, "Error in Fetching$databaseError", Toast.LENGTH_LONG).show()
                                        lottieLayout?.visibility = View.GONE
                                    }
                                })


            }
        }
    }

    private fun fetchData() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://allsportsapi.com/api/football/?met=Leagues&APIkey=b22781aea4588f441ff115ea5b79d27ae2d3226304dba8ca9a16ec390d3f1dd2&countryId=134"
        val stringRequest = StringRequest(Request.Method.GET, url, com.android.volley.Response.Listener { response ->
            val strRespone = response.toString()
            val jsonObject = JSONObject(strRespone)
            val jsonArray = jsonObject.getJSONArray("result")

        }, com.android.volley.Response.ErrorListener { })
        queue!!.add(stringRequest)
    }

    private fun setUPToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_search)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(true)
        }


    }

    private fun setUpDrawer() {
        val navDrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener { it ->
            when (it.itemId) {
                R.id.lotte -> {
                   startActivity(Intent(this,AnimationActivity::class.java))
                }
                R.id.edit_user_information -> {

                }
                R.id.change_password -> {
                    startActivity(Intent(this, CryptoCurrencyActivity::class.java))
                }
                R.id.weather_forecast -> {
                    navDrawerLayout.closeDrawers()
                    startActivity(Intent(this@ProfileInformationActivity, ListUserMainActivity::class.java))
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                }
            }

            return@setNavigationItemSelectedListener true
        }
        val actionBarDrawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(this, navDrawerLayout, toolbar, R.string.open, R.string.close) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                val slideX = drawerView.width * slideOffset
                main_content.translationX = -slideX
                super.onDrawerSlide(drawerView, slideOffset)
            }
        }
        navDrawerLayout.addDrawerListener(actionBarDrawerToggle)
        settingImage.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SELECT_PHOTO -> if (resultCode == AppCompatActivity.RESULT_OK) {
                srcUri = data!!.data
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
                    val downloadUri = taskSnapshot.uploadSessionUri
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
                    mainContainer?.visibility = View.VISIBLE
                }


            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
////        val inflater = menuInflater
////        inflater.inflate(R.menu.profile_information_menu, menu)
////        userListItem = menu.findItem(R.id.list_of_users)
////        return true
//
//    }

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
                    if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                        drawerLayout.closeDrawer(GravityCompat.END)
                    } else {
                        drawerLayout.openDrawer(GravityCompat.END)
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



