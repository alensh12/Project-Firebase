package com.example.amprime.firebaseauth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileInformationActivity extends AppCompatActivity {


    public TextView name, email, address, userType, usernameField, emailIdField, addressField, usertypeField;
    private RelativeLayout infoContainer;
    private FirebaseAuth auth;
    private static final int SELECT_PHOTO = 100;
    private DatabaseReference reference;
    private FirebaseAuth.AuthStateListener mauthListener;
    ProgressDialog mProcessDialog;
    private Uri srcUri;
    private ImageView mProfileImage;
    private ListUser user;
    private StorageReference mStorageRefernce;
    private static final String mAdmin = "Admin";
    private static final String mUser = "User";
    private MenuItem userListItem;
    StorageTask<UploadTask.TaskSnapshot> uploadTask;
    private Typeface typeface;


    @Override
    protected void onStart() {
        auth.addAuthStateListener(mauthListener);
        super.onStart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_information);


        usernameField = findViewById(R.id.username_field);
        emailIdField = findViewById(R.id.email_id_field);
        addressField = findViewById(R.id.address_field);
        usertypeField = findViewById(R.id.designation_field);
        typeface = Typeface.createFromAsset(getAssets(), "Raleway-Medium.ttf");
        usernameField.setTypeface(typeface);
        emailIdField.setTypeface(typeface);
        addressField.setTypeface(typeface);
        usertypeField.setTypeface(typeface);

        name = findViewById(R.id.PI_name);
        email = findViewById(R.id.PI_email);
        address = findViewById(R.id.PI_address);
        userType = findViewById(R.id.PI_Designation);
        mProfileImage = findViewById(R.id.PI_profile_image);
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);


            }
        });
        infoContainer = findViewById(R.id.data_container);
        mProcessDialog = new ProgressDialog(this);
        mProcessDialog.setMessage("Loading User Info......");
        mProcessDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        Log.d("Tag", "user" + auth);


        mauthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {



                Log.d("TAg", "CurrentUser: " + auth.getCurrentUser());
                if (auth.getCurrentUser() != null) {
        try {
        mProcessDialog.show();
        }catch (Exception e){

        }
                    mStorageRefernce = FirebaseStorage.getInstance().getReference();
                    reference = FirebaseDatabase.getInstance().getReference().child("Users");
                    reference.child(firebaseAuth
                            .getCurrentUser()
                            .getUid())
                            .addValueEventListener(
                                    new ValueEventListener() {

                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()) {
                                                name.setText(String.valueOf(dataSnapshot.child("fullname").getValue()));
                                                address.setText(String.valueOf(dataSnapshot.child("address").getValue()));
                                                email.setText(String.valueOf(dataSnapshot.child("emailId").getValue()));
                                                userType.setText(String.valueOf(dataSnapshot.child("userType").getValue()));
                                                String UrlImg = String.valueOf(dataSnapshot.child("imgUrl").getValue());
                                                Log.d("TAG", "UrlImage: " + UrlImg);
                                                if (URLUtil.isValidUrl(UrlImg)) {
                                                    Glide.with(getApplicationContext())
                                                            .load(UrlImg)
                                                            .apply(RequestOptions.circleCropTransform())
                                                            .into(mProfileImage);
                                                }
                                                String UserType = String.valueOf(dataSnapshot.child("userType").getValue());
                                                Log.d("UserType: ", UserType);
                                                SharedPreferences preferences = getSharedPreferences("my_pref", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("role", UserType);
                                                editor.apply();
                                                mProcessDialog.dismiss();
                                            }
                                            else{
                                                mProcessDialog.dismiss();
                                                Toast.makeText(user, "empty", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(getApplicationContext(), "Error in Fetching" + databaseError, Toast.LENGTH_LONG).show();
                                            mProcessDialog.dismiss();
                                        }
                                    });


                }

            }
        };
}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    srcUri = data.getData();
                    Log.d("tag", "here" + srcUri);
                    Toast.makeText(this,"File Path :"+srcUri.getPath(), Toast.LENGTH_LONG).show();
                    final StorageReference filepath = mStorageRefernce.child("Photos").child(srcUri.getLastPathSegment());
                    mProcessDialog = new ProgressDialog(this);
                    mProcessDialog.setMax(100);
                    mProcessDialog.setMessage("Loading....");
                    mProcessDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProcessDialog.show();
                    mProcessDialog.setCancelable(false);

                    uploadTask = filepath.putFile(srcUri);
                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            mProcessDialog.incrementProgressBy((int) progress);
                        }
                    });
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileInformationActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUri = taskSnapshot.getDownloadUrl();
                            Toast.makeText(getApplicationContext(), "Successfully Saved in Storage", Toast.LENGTH_SHORT).show();
                            Glide.with(ProfileInformationActivity.this)
                                    .load(downloadUri)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(mProfileImage);


                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid());

                    Map<String, Object> userImage = new HashMap<>();
                    userImage.put("imgUrl",downloadUri.toString());
                    reference.updateChildren(userImage);

                            mProcessDialog.dismiss();
                        }
                    });



                }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_information_menu, menu);
        userListItem = menu.findItem(R.id.list_of_users);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences preferences = getSharedPreferences("my_pref", MODE_PRIVATE);
        final String role = preferences.getString("role", "");

        if (role.matches(mUser)) {

            switch (item.getItemId()) {
                case R.id.log_out_profile:
                    auth.signOut();

                    Log.d("item ID: ", String.valueOf(item.getItemId()));

                    SharedPreferences preferences1 = getSharedPreferences("login" ,MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences1.edit();
                    editor.putBoolean("islogin",false);
                    editor.commit();
                    Intent intent = new Intent(ProfileInformationActivity.this, EmailAndPasswordActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);


                    break;

                case R.id.change_password:
                    Log.d("item ID: ", String.valueOf(item.getItemId()));
                    changePassword();
                    return true;

                case R.id.list_of_users:
                    Toast.makeText(getApplicationContext(),"Not allowed to User",Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.edit_user_information:
                    showUpdateDialog();
                default:
                    return true;

            }
        }
        else
            switch (item.getItemId()){
                case R.id.log_out_profile:


                    SharedPreferences preferences2 = getSharedPreferences("login" ,MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences2.edit();
                    editor.putBoolean("islogin",false);
                    editor.commit();
                    Intent intent = new Intent(ProfileInformationActivity.this, EmailAndPasswordActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

//                    finish();
                    break;
                case R.id.change_password:
                    changePassword();

                    break;
                case R.id.list_of_users:
                    userListItem.setVisible(true);
                    startActivity(new Intent(ProfileInformationActivity.this,ListUser.class));
                    finish();
                    return true;
                case R.id.edit_user_information:


                    showUpdateDialog();

                default:
                    return true;
            }

        return super.onOptionsItemSelected(item);
    }

    private boolean updateUser(String userDesignation,String userAddress){

      DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Map<String, Object> users = new HashMap<>();
        users.put(auth.getCurrentUser().getUid()+"/fullname",userDesignation);
        users.put(auth.getCurrentUser().getUid()+"/address",userAddress);
        reference.updateChildren(users);
        return  true;
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater= getLayoutInflater();

        final View view = inflater.inflate(R.layout.edit_profile_information,null);
        builder.setView(view);

        final EditText editTextUsername = view.findViewById(R.id.edit_user_name);
        final EditText editTextAddress = view.findViewById(R.id.edit_user_address);

        Button saveButton = view.findViewById(R.id.save_edited_user_data);

        Button cancelButton  = view.findViewById(R.id.cancel_edited_user_data);

        builder.setTitle("Update User Information");

        final AlertDialog dialog = builder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName= editTextUsername.getText().toString().trim();
                String userAddress = editTextAddress.getText().toString().trim();
                Log.d("Tag","userAddress: "+ userAddress);
                if(TextUtils.isEmpty(userAddress)){
                    editTextAddress.setError("Required");
                    Log.d("Tag","here");
               }
                else{
                    updateUser(userName,userAddress);
                    dialog.dismiss();

                }

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    private void changePassword()  {
        Intent intent = new Intent(ProfileInformationActivity.this,ChangePassword.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }

}



