package com.example.amprime.firebaseauth;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.amprime.firebaseauth.Retrofit.APIService;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by amprime on 10/27/17.
 */

public class ActivityPushNotificationConsole extends AppCompatActivity {
   private TextView txt;
   private EditText TitleText;
   private EditText Body;
   APIService mService;

   private final String TAG=ActivityPushNotificationConsole.class.getSimpleName();
    String app_server_Url = "http://10.42.0.1/Fcm/fcm_insert.php";
    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification);
        txt = findViewById(R.id.push_notification_text);
        TitleText = findViewById(R.id.title);
        Body = findViewById(R.id.body);
        Token.UserToken= FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG,Token.UserToken);

        mService = Token.getFCMClient();



}

    public void sendTokenToAppServer(View view) {
       SharedPreferences preferences = getApplicationContext().getSharedPreferences("Shared Pref", Context.MODE_PRIVATE);
        final String key =  preferences.getString("UserToken","");
        txt.setText(key);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, app_server_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ErrorListener", String.valueOf(error));
            }

        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("fcm_token",key);
                return params;
            }
        };

        Singleton.getmInstance(getApplicationContext()).addRequestQueue(stringRequest);
    }

    public void sendPushNotificationMessage(View view) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Shared Pref", Context.MODE_PRIVATE);
        final String key =  preferences.getString("UserToken","");
        txt.setText(key);


        String textTitle = TitleText.getText().toString().trim();
        String textBody = Body.getText().toString().trim();

        SendPushNotification notification = new SendPushNotification(this);
        notification.SendPushNotifiction(key,textTitle,textBody,"alensh");

    }
}