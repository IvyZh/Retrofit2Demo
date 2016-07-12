package com.example.ivy.retrofit2demo;


import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Ivy on 2016/7/11.
 */
public interface MyServerInterface {

    @GET("article/list/latest?page=1")
    Call<ResponseBody> getInfo();

    @GET("article/list/{type}?")
    Call<QiushiModel> getQiuShiInfo(@Path("type") String type, @Query("page") int page);


    @GET("article/list/{type}?")
    Call<QiushiModel> getInfoList(@Path("type") String type, @Query("page") int page);

    @POST("index/login?p=1")
    @FormUrlEncoded
    Call<ResponseBody> login(@Field("email") String username,@Field("password") String pwd,@Field("ememberme") String ememberme);


    @POST("index/login?p=1")
    @FormUrlEncoded
    Call<ResponseBody> loginmap(@FieldMap Map<String,String> options);




    @GET("app/question")
    Call<ResponseBody> getQuesInfo(@QueryMap Map<String ,String> map);


    @Streaming
    @GET("http://img002.21cnimg.com/photos/album/20160711/m600/4DEA0A61901379A64A011EDC78E9F2F5.png")
    Call<ResponseBody> downLoad();



    @Streaming
    @GET
    Call<ResponseBody> downLoadApk(@Url String url);



    //Post上传文件

    @Multipart
    @POST("utility/uploadhead")
    Call<ResponseBody> uploadFile(@Part("Filedata\";filename=\"uploadpic.png") RequestBody requestBody);


    @POST("utility/uploadhead")
    Call<ResponseBody> uploadFile2(@Body MultipartBody multipartBody);




}
