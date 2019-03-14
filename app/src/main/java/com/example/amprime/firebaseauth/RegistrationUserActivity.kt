package com.example.amprime.firebaseauth

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.LoaderManager.LoaderCallbacks
import android.app.ProgressDialog
import android.content.CursorLoader
import android.content.Intent
import android.content.Loader
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

import com.example.amprime.firebaseauth.model.UserInformation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

import android.Manifest.permission.READ_CONTACTS


/**
 * A login screen that offers login via email/password.
 */
class RegistrationUserActivity : AppCompatActivity(), LoaderCallbacks<Cursor> {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var mAuthTask: UserLoginTask? = null

    // UI references.
    private var mEmailView: AutoCompleteTextView? = null
    private var mPasswordView: EditText? = null
    private var mRePasswordView: EditText? = null
    private var mAddress: EditText? = null
    private var mPersonName: EditText? = null
    private var mAge: EditText? = null
    private var mSpinnerGender: Spinner? = null
    private var mProgressView: View? = null
    private var mLoginFormView: View? = null
    private var firebaseAuthh: FirebaseAuth? = null
    private var dbRefernce: DatabaseReference? = null
    private var mdialog: ProgressDialog? = null
    private var mSpinnerCountry: Spinner? = null
    //    private RadioGroup radioGroup;
    //    private RadioButton radioButton;
    internal var timeStamp: Long? = System.currentTimeMillis()
    internal lateinit var date: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)


        // Set up the login form.
        mEmailView = findViewById(R.id.email)
        populateAutoComplete()
        mdialog = ProgressDialog(this)
        firebaseAuthh = FirebaseAuth.getInstance()
        //        radioGroup = findViewById(R.id.radio_group);
        //        radioButton = findViewById(R.id.checkbox_user);

        mPasswordView = findViewById(R.id.password)
        mPasswordView!!.setOnEditorActionListener(TextView.OnEditorActionListener { textView, id, keyEvent ->
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptRegister()
                return@OnEditorActionListener true
            }
            false
        })

        mRePasswordView = findViewById(R.id.re_password)
        mRePasswordView!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == R.id.login || actionId == EditorInfo.IME_NULL) {
                attemptRegister()
                return@OnEditorActionListener true
            }
            false
        })

        mPersonName = findViewById(R.id.registration_name)
        mPersonName!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_NULL) {

                attemptRegister()
                return@OnEditorActionListener true
            }
            false
        })
        mAddress = findViewById(R.id.registration_address)
        mAddress!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_NULL) {
                attemptRegister()
                return@OnEditorActionListener true
            }
            false
        })
        mAge = findViewById(R.id.registration_age)
        mAge!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_NULL) {
                attemptRegister()

                return@OnEditorActionListener true
            }
            false
        })

        mSpinnerCountry = findViewById(R.id.registration_country)


        val mAdapter = ArrayAdapter.createFromResource(this, R.array.country, android.R.layout.simple_spinner_item)
        mAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)


        mSpinnerCountry!!.adapter = mAdapter

        mSpinnerGender = findViewById(R.id.registration_gender)
        val mmAdapter = ArrayAdapter.createFromResource(this, R.array.Gender, android.R.layout.simple_spinner_item)
        mmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSpinnerGender!!.adapter = mmAdapter

        val mEmailSignInButton = findViewById<Button>(R.id.registration_ok_button)
        mEmailSignInButton.setOnClickListener { attemptRegister() }

        mLoginFormView = findViewById(R.id.login_form)
        mProgressView = findViewById(R.id.login_progress)


    }

    private fun populateAutoComplete() {
        if (!mayRequestContacts()) {
            return
        }

        loaderManager.initLoader(0, null, this)
    }

    private fun mayRequestContacts(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView!!, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) { requestPermissions(arrayOf(READ_CONTACTS), REQUEST_READ_CONTACTS) }
        } else {
            requestPermissions(arrayOf(READ_CONTACTS), REQUEST_READ_CONTACTS)
        }
        return false
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete()
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptRegister() {
        if (mAuthTask != null) {
            return
        }

        // Reset errors.
        mEmailView!!.error = null
        mPasswordView!!.error = null
        mRePasswordView!!.error = null
        mAge!!.error = null
        mAddress!!.error = null
        mPersonName!!.error = null

        // Store values at the time of the login attempt.
        val email = mEmailView!!.text.toString()
        val password = mPasswordView!!.text.toString()
        val rePassword = mRePasswordView!!.text.toString()

        val mage = mAge!!.text.toString()
        val age = if (mage != "") Integer.parseInt(mage) else 0
        val address = mAddress!!.text.toString()
        val name = mPersonName!!.text.toString()
        val stringValueofCountry = mSpinnerCountry!!.selectedItem.toString()
        val stringValueofGender = mSpinnerGender!!.selectedItem.toString()
        val imgurl = "Default"
        try {
            date = getDate(timeStamp!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val time = getTime(timeStamp!!)
        val emailId = email
        val userType = "User"
        val uId = FirebaseDatabase.getInstance().reference.child("User").push().key
        val token = FirebaseInstanceId.getInstance().token
        var cancel = false
        var focusView: View? = null


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView!!.error = getString(R.string.error_invalid_password)
            focusView = mPasswordView
            cancel = true
        }

        if (!isPasswordValid(password)) {
            mPasswordView!!.error = "Password Must be at least 6 Character"
            focusView = mPasswordView
            cancel = true
        }

        //Check if the password matches with re-enter password.

        if (TextUtils.isEmpty(rePassword)) {
            mRePasswordView!!.error = "empty"
            focusView = mRePasswordView
            cancel = true
        }
        if (!sameRePassword(rePassword, password)) {
            mRePasswordView!!.error = "Not Matched"
            focusView = mRePasswordView
            cancel = true
        }

        // Check age
        if (!validAge(age)) {
            mAge!!.error = getString(R.string.validate_age)
            focusView = mAge
            cancel = true
        }
        //Check User Name
        if (TextUtils.isEmpty(name)) {
            mPersonName!!.error = "Empty"
            focusView = mPersonName
            cancel = true
        }
        if (TextUtils.isEmpty(address)) {
            mAddress!!.error = "Empty"
            focusView = mAddress
            cancel = true
        }


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView!!.error = getString(R.string.error_field_required)
            focusView = mEmailView
            cancel = true
        } else if (!isEmailValid(email)) {
            mEmailView!!.error = getString(R.string.error_invalid_email)
            focusView = mEmailView
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mdialog!!.setMessage("Registering......")
            try {
                mdialog!!.show()
            } catch (e: Exception) {

            }

            firebaseAuthh!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showProgress(true)

                            Toast.makeText(this@RegistrationUserActivity, "User Registered", Toast.LENGTH_SHORT).show()
                            FirebaseMessaging.getInstance().subscribeToTopic("me")
                            finish()
                            dbRefernce = FirebaseDatabase.getInstance().reference.child("Users")
                            Log.d("insertUID", uId)

                            val information = UserInformation(name,
                                    address,
                                    age,
                                    stringValueofCountry,
                                    stringValueofGender,
                                    imgurl,
                                    date,
                                    time,
                                    token,
                                    emailId,
                                    userType)
                            val user = firebaseAuthh!!.currentUser
                            if (user != null) {
                                Log.d("user", information.toString())
                                // dbRefernce.child(uId).setValue(information);
                                dbRefernce!!.child(user.uid).setValue(information)
                            }
                            mdialog!!.dismiss()
                            startActivity(Intent(applicationContext, EmailAndPasswordActivity::class.java))


                        }
                        if (!task.isSuccessful) {
                            showProgress(true)
                            Toast.makeText(applicationContext, "Duplicate Username not allowed", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext, RegistrationUserActivity::class.java))
                            mdialog!!.dismiss()

                        } else {
                            mdialog!!.dismiss()

                        }
                    }


            //            mAuthTask = new UserLoginTask(email, password);
            //            mAuthTask.execute((Void) null);
        }
    }

    @Throws(ParseException::class)
    private fun getDate(time: Long): String {
        val formatter = SimpleDateFormat("EEE, MMM d, ''yy")
        val netDate = Date(time)

        val date = formatter.format(netDate)


        Log.d("formated date", date)
        return date
    }

    private fun getTime(watch: Long): String {
        val format = SimpleDateFormat("hh:mm:ss")
        val netTime = Date(watch)

        val time = format.format(netTime)
        return time
    }

    private fun validAge(age: Int): Boolean {
        return age > 18 && age < 50
    }

    private fun sameRePassword(rePassword: String, password: String): Boolean {
        return rePassword == password
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 5
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

            mLoginFormView!!.visibility = if (show) View.GONE else View.VISIBLE
            mLoginFormView!!.animate().setDuration(shortAnimTime.toLong()).alpha(
                    (if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mLoginFormView!!.visibility = if (show) View.GONE else View.VISIBLE
                }
            })

            mProgressView!!.visibility = if (show) View.VISIBLE else View.GONE
            mProgressView!!.animate().setDuration(shortAnimTime.toLong()).alpha(
                    (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mProgressView!!.visibility = if (show) View.VISIBLE else View.GONE
                }
            })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView!!.visibility = if (show) View.VISIBLE else View.GONE
            mLoginFormView!!.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    override fun onCreateLoader(i: Int, bundle: Bundle): Loader<Cursor> {
        return CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", arrayOf(ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE),

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC")
    }

    override fun onLoadFinished(cursorLoader: Loader<Cursor>, cursor: Cursor) {
        val emails = ArrayList<String>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS))
            cursor.moveToNext()
        }

        addEmailsToAutoComplete(emails)
    }

    override fun onLoaderReset(cursorLoader: Loader<Cursor>) {

    }

    private fun addEmailsToAutoComplete(emailAddressCollection: List<String>) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        val adapter = ArrayAdapter(this@RegistrationUserActivity,
                android.R.layout.simple_dropdown_item_1line, emailAddressCollection)

        mEmailView!!.setAdapter(adapter)
    }
    //
    //    @SuppressLint("ShowToast")
    //    public void onRadioButtonClicked(View view) throws Exception {
    //        int radioValue = radioGroup.getCheckedRadioButtonId();
    //        if(radioButton==null) {
    //        Toast.makeText(getApplicationContext(),"null value",Toast.LENGTH_SHORT).show();
    //        }
    //        radioButton = findViewById(radioValue);
    //
    //       String Usertype = radioButton.getText().toString();
    //        Log.d("UserType",Usertype);
    //
    //
    //
    //        Toast.makeText(getApplicationContext(),radioButton.getText().toString(),Toast.LENGTH_LONG).show();
    //    }


    private interface ProfileQuery {
        companion object {
            val PROJECTION = arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.CommonDataKinds.Email.IS_PRIMARY)

            val ADDRESS = 0
            val IS_PRIMARY = 1
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    inner class UserLoginTask internal constructor(private val mEmail: String, private val mPassword: String) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void): Boolean? {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                return false
            }

            for (credential in DUMMY_CREDENTIALS) {
                val pieces = credential.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (pieces[0] == mEmail) {
                    // Account exists, return true if the password matches.
                    return pieces[1] == mPassword
                }
            }


            return true
        }

        override fun onPostExecute(success: Boolean?) {
            mAuthTask = null
            showProgress(false)

            if (success!!) {
                finish()
            } else {
                mPasswordView!!.error = getString(R.string.error_incorrect_password)
                mPasswordView!!.requestFocus()
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@RegistrationUserActivity, EmailAndPasswordActivity::class.java))
        super.onBackPressed()
    }

    companion object {

        /**
         * Id to identity READ_CONTACTS permission request.
         */
        private val REQUEST_READ_CONTACTS = 0

        /**
         * A dummy authentication store containing known user names and passwords.
         * TODO: remove after connecting to a real authentication system.
         */
        private val DUMMY_CREDENTIALS = arrayOf("foo@example.com:hello", "bar@example.com:world")
    }
}

