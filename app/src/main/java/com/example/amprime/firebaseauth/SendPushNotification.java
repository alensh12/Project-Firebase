package com.example.amprime.firebaseauth;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.amprime.firebaseauth.helper.Token;
import com.example.amprime.firebaseauth.retrofit.APIService;
import com.example.amprime.firebaseauth.model.MyResponse;
import com.example.amprime.firebaseauth.model.Notification;
import com.example.amprime.firebaseauth.model.Sender;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by amprime on 11/17/17.
 */

public class SendPushNotification {
    APIService mService;
    Context mContext;


    public SendPushNotification(Context context) {
        mService = Token.getFCMClient();
        mContext = context;
    }

    public void SendPushNotifiction(final String token, String title, final String message,final String username){
        Log.e("send push notificaiton","here "+ token);



        Notification notification = new Notification(title,message);
        Sender sender = new Sender(token,notification);
        mService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {

               if(response.body().success == 1){
                    Toast.makeText(mContext," Token message sent to "+username,Toast.LENGTH_SHORT).show();


                }
                else{
                    Toast.makeText(mContext, "Device Token Not Found of "+ username, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();

            }


        });

    }
}
