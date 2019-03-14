package com.example.amprime.firebaseauth;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import androidx.appcompat.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.amprime.firebaseauth.adapter.UserAdapter;
import com.example.amprime.firebaseauth.helper.SimpleDividerItemDecoration;
import com.example.amprime.firebaseauth.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListUser extends AppCompatActivity implements UserAdapter.MessageAdapterlistner, DialogClick {

    private RecyclerView recyclerView;
    private FirebaseAuth.AuthStateListener mListener;
    private FirebaseAuth mAuth;
    private Query mDatabaseReference;
    private UserAdapter mAdapter;
    private static final String TAG = ListUser.class.getSimpleName();
    private static ArrayList entries = new ArrayList<>();

    private ActionMode actionmode;
    private ActionModeCallBack actionModeCallBack;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        Log.d("tag","here 1");
        recyclerView = findViewById(R.id.list_user_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionModeCallBack = new ActionModeCallBack();
        mAdapter = new UserAdapter(this,entries,this);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference =FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("time");
        Log.d("Tag",""+mDatabaseReference);
        Log.d("Tag","User"+mAuth.getCurrentUser());
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(getApplicationContext(), EmailAndPasswordActivity.class);
                    intent.putExtra("goToListUser", true);
                    startActivity(intent);
                   // finish();
                } else {
                    getUser();


                }

            }
        };


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search_action,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId()==R.id.action_search){
//
//        }
//        return true;
//
//    }

    private void getUser() {

             mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                        Log.d("Tag",""+dataSnapshot);
                        entries.clear();
                        while(items.hasNext()){

                            DataSnapshot item= items.next();
                            String username;
                            String address;
                            final String date;
                            final String time;
                            final String token;
                            final String designation;

                            String Uid =item.getKey();
                            username = item.child("fullname").getValue().toString();
                            address = item.child("address").getValue().toString();
                            date=item.child("date").getValue().toString();
                            time=item.child("time").getValue().toString();
                            token = item.child("token").getValue().toString();
                            designation = item.child("userType").getValue().toString();
                            Log.d("token", String.valueOf(item.hasChild("token")));
                            Log.d("values",item.getValue().toString());
                            Log.d("timeStamp", String.valueOf(item.hasChild("timestampCreated")));
                            entries.add(new User(Uid,username,address,date,time,token,designation));


                        }

                        Collections.reverse(entries);

//                        recyclerView.setItemAnimator(new DefaultItemAnimator());
//                        recyclerView.addItemDecoration(new DividerItem(getApplicationContext(), LinearLayoutManager.VERTICAL));
                        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
                        recyclerView.setAdapter(mAdapter);
                        recyclerView.getAdapter().notifyDataSetChanged();

                        }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(ListUser.this, "Error"+databaseError, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.addAuthStateListener(mListener);
    }



    @Override
    public void MessageRowClicked(int position) {
        mAdapter.getSelectedItem().size();
        User user = (User) entries.get(position);
        Log.d("click_tag","iconClick "+position );
        changeUserDesignation(position);

    }





    @Override
    public void iconClick(int position) {
      mAdapter.getSelectedItem().size();

        User user = (User) entries.get(position);

        Log.d("click_tag","iconClick "+position );

        Log.d("clicked_token",String.valueOf(user.getToken()));
        Log.d("username",String.valueOf(user.getFullname()));
            if(actionmode==null){
            actionmode =startSupportActionMode(actionModeCallBack);


            }
            toggleSelection(position);

    }

    @Override
    public void onRowLongClicked(int position) {


        enableActionMode(position);
        Log.d("click_tag","RowLongClick"+ position);
    }
    private void enableActionMode(int position) {
        if(actionmode==null){
            actionmode =startSupportActionMode(actionModeCallBack);
            Log.d(TAG,"Here");
        }
        toggleSelection(position);
    }
    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();
        if(count==0){
            actionmode.setTitle(String.valueOf(count)+" selected");
        }
        else{
            actionmode.setTitle(String.valueOf(count)+" selected");
            actionmode.invalidate();
        }

    }

    @Override
    public void click(String title, String message) {
        List<Integer> selectedItemPositions = mAdapter.getSelectedItem();
        Log.d("selected item", String.valueOf(selectedItemPositions.size()));
        for(int i= selectedItemPositions.size()-1;i>=0;i--) {

            User user = (User) entries.get(selectedItemPositions.get(i));

            String token = String.valueOf(user.getToken());
            String username = user.getFullname();


            Log.d("sending","username: " +user.getFullname()+" token : "+token);

            SendPushNotification notification = new SendPushNotification(this);
            notification.SendPushNotifiction(token,title,message,username);

        }
        mAdapter.clearSelection();

        actionmode.finish();
    }

    @Override
    public void clickCancel() {
        mAdapter.clearSelection();
        actionmode.finish();
    }

    private class ActionModeCallBack implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.list_user_action_mode,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch(item.getItemId()){
//                case R.id.delete_action:
//                    Toast.makeText(ListUser.this, "deleted", Toast.LENGTH_SHORT).show();
//                    deleteUser();
//                  mode.finish();
//                  return true;


                case R.id.send_push_notification:
                    show_push_notification_dialog();
                    Toast.makeText(ListUser.this, "push notification", Toast.LENGTH_SHORT).show();
                    mode.setTitle("");

                    default:
                        return false;
            }



        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
           mAdapter.clearSelection();
            actionmode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.resetIndex();
                     Log.d(TAG,"Runnable()");
                }
            });

        }

    }

    private void show_push_notification_dialog() {

            NotificationDialog dialog = NotificationDialog.newInstance(this);

            dialog.show(getSupportFragmentManager(), "Notificaiton Dialog");

        }


        private void changeUserDesignation(final int postion){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            LayoutInflater inflater = getLayoutInflater();
            final View view = inflater.inflate(R.layout.edit_information,null);
            dialogBuilder.setView(view);

            final Spinner spinnerDesignation = view.findViewById(R.id.designation_spinner);


            Button saveButton = view.findViewById(R.id.save_edited_user_data);

            Button cancelButton  = view.findViewById(R.id.cancel_edited_user_data);


            final AlertDialog dialog =dialogBuilder.create();
            dialog.setTitle("Change User Designation");
            dialog.show();
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userDesignation = spinnerDesignation.getSelectedItem().toString().trim();

                        updateUser(postion,userDesignation);
                        dialog.dismiss();

                    }

                }
            );
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });




        }

    private void updateUser(int position, String userDesignation) {

        User user = (User) entries.get(position);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        Log.d("TAG", String.valueOf(reference));
        Map<String, Object> users = new HashMap<>();
        users.put("userType",userDesignation);

        reference.updateChildren(users);

    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(ListUser.this,ProfileInformationActivity.class);
        backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(backIntent);
        super.onBackPressed();
    }

    //    private void deleteUser() {
//
//        List<Integer> selectedItemPositions =
//                mAdapter.getSelectedItem();
//        for (int i = selectedItemPositions.size()-1; i >= 0; i--) {
//            mAdapter.removeUser(selectedItemPositions.get(i));
//        }
//
//        mAdapter.notifyDataSetChanged();
//        mAdapter.resetIndex();
//    }

}
