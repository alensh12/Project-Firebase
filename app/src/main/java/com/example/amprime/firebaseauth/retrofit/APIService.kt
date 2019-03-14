package com.example.amprime.firebaseauth.retrofit


import com.example.amprime.firebaseauth.model.MyResponse
import com.example.amprime.firebaseauth.model.Sender

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by amprime on 11/14/17.
 */

interface APIService {
    @Headers("Content_Type:application/json", "Authorization:key=AAAAbveL9Nc:APA91bHwwa5uBuD2e1fGlm2M4GvaGEYTPpzdpwDdLlvt-Jt4UBrgMOAWdldnP6lD1oCzWShFpIPnZtcAm3ZGrgf9nbmV29Bzarb_H20TX8R8qLpng2SOB8Yioz2J3GPcbTisvQ8jI_yA")
    @POST("fcm/send")
    fun sendNotification(@Body body: Sender): retrofit2.Call<MyResponse>
}
