package com.ats.samarthajuice.constant;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ats.samarthajuice.interfaces.MyInterface;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constants {

    // public static  String serverURL="http://192.168.2.3:8099/";
   //  public static  String serverURL="http://192.168.2.13:8097/";
     public static  String serverURL="http://192.168.2.201:8080/SamarthApi/";

   //  public static String serverURL = "http://132.148.148.215:8080/happyfeast/";

//    public static String categoryImagePath = "http://132.148.143.124:8080/happyfeast/uploads/category/";
//    public static String itemImagePath = "http://132.148.143.124:8080/happyfeast/uploads/item/";


    public static String categoryImagePath = "http://132.148.148.215:8080/happyfeastuploads/category/";
    public static String itemImagePath = "http://132.148.148.215:8080/happyfeastuploads/item/";

    public static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .method(original.method(), original.body())
                            .build();

                    Response response = chain.proceed(request);
                    return response;
                }
            })
            .readTimeout(10000, TimeUnit.SECONDS)
            .connectTimeout(10000, TimeUnit.SECONDS)

            .writeTimeout(10000, TimeUnit.SECONDS)
            .build();


    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(serverURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build();

    public static MyInterface myInterface = retrofit.create(MyInterface.class);

    public static boolean isOnline(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            // Toast.makeText(context, "No Internet Connection ! ", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


}
