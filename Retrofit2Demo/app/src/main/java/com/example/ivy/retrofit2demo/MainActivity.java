package com.example.ivy.retrofit2demo;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final String loginUrl = "";
    final String userInfoUrl = "";
    String uploadUrl = "";
    private String weatherInfoUrl = "http://v.juhe.cn/weather/index?cityname=%s&dtype=json&format=1&key=2dddd304db4c8294886f09c112b89119";
    private EditText etUserName;
    private EditText etPwd;
    private Button btLogin;
    private TextView tvResult;
    private Button btOperator;
    private final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C)";
    private Button btUpload;
    private Call<ResponseBody> call;
    private MyServerInterface server;
    private Button btDownload;
    private MyDownloadReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUserName = (EditText) findViewById(R.id.et_username);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        tvResult = (TextView) findViewById(R.id.tv_result);
        btLogin = (Button) findViewById(R.id.bt_login);
        btOperator = (Button) findViewById(R.id.bt_operator);
        btUpload = (Button) findViewById(R.id.bt_upload);
        btDownload = (Button) findViewById(R.id.bt_download);
        Button btNext = (Button) findViewById(R.id.bt_next);

        btLogin.setOnClickListener(this);
        btOperator.setOnClickListener(this);
        btUpload.setOnClickListener(this);
        btDownload.setOnClickListener(this);
        btNext.setOnClickListener(this);



        initRetrofit();

        receiver = new MyDownloadReceiver();

        registerReceiver(receiver,new IntentFilter("com.ivy.retrofit2.downloadapk"));
    }

    private void initRetrofit() {

        OkHttpClient client = OkHttp3Utils.getOkHttpSingletonInstance();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL2)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        server = retrofit.create(MyServerInterface.class);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.bt_next:

                startActivity(new Intent(MainActivity.this,SecondActivity.class));
                break;

            case R.id.bt_login:

//                login();
                login2();

                break;

            case R.id.bt_operator:
//                getInfo();
                //getInfo2();
                getInfo3();
                break;

            case R.id.bt_upload:

//                upload();//failure,guest模式
                upload2();//guest模式
                break;
            case R.id.bt_download:

                downloadApk();
               // download();
                break;

        }

    }

    private void upload2() {

        String path = SDCardHelper.getSDCardPrivateCacheDir(this);
        File file = new File(path, "222.jpg");

        HashMap<String, String> map = new HashMap<>();
        map.put("type","big");
        File[] files  = {file};
        String[] formFileNams  = {"Filedata"};

        MultipartBody mBody =  buildMultipartBody(map,files,formFileNams);

        Call<ResponseBody> uploadFile2 = server.uploadFile2(mBody);

        uploadFile2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    tvResult.setText("upload success"+response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                tvResult.setText("upload onFailure");
            }
        });


    }

    private MultipartBody buildMultipartBody(HashMap<String, String> map, File[] files, String[] formFileNams) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        if(map!=null){
            for(Map.Entry<String,String> entry:map.entrySet()){
                builder.addPart(Headers.of("Content-Disposition","form-data;name=\""+entry.getKey()+"\""),
                        RequestBody.create(null,entry.getValue())
                        );
            }
        }

        if(files!=null && formFileNams!=null){

            for(int i = 0;i<files.length;i++){
                File file = files[i];
                String filename = file.getName();
                String formName = formFileNams[i];

                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                builder.addPart(Headers.of("Content-Disposition",
                        "form-data;name=\""+formName+"\";filename=\""+filename+"\""),requestBody);
            }

        }

        return builder.build();

    }

    private void upload() {
        String path = SDCardHelper.getSDCardPrivateCacheDir(this);
        File file = new File(path, "222.jpg");

        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        Call<ResponseBody> call = server.uploadFile(body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    tvResult.setText("upload success"+response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                tvResult.setText("upload onFailure");
            }
        });


    }

    private void login2() {

        HashMap<String, String> map = new HashMap<>();
        map.put("email","m8");
        map.put("password","111222");
        map.put("ememberme","1");

        Call<ResponseBody> loginmap = server.loginmap(map);
        loginmap.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.d("main","success:"+result);
                    tvResult.setText("success:"+result+"login2");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     *下载大文件apk
     */
    private void downloadApk() {

        Log.d("main","downloadApk...");
        final Call<ResponseBody> loadApk = server.downLoadApk("http://ftp-apk.pconline.com.cn/906e08ac71bb236a3881b6afe497243f/pub/download/201010/pconline1464952259349.apk");
//        final Call<ResponseBody> loadApk = server.downLoadApk("http://img002.21cnimg.com/photos/album/20160711/m600/4DEA0A61901379A64A011EDC78E9F2F5.png");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response<ResponseBody> response = null;
                try {
                    response = loadApk.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                InputStream is = response.body().byteStream();

                boolean flag = SDCardHelper.saveFileToSDCardPrivateCacheDir(is, "ltzj.apk", MainActivity.this);

                if(flag){

                    Intent intent = new Intent();
                    intent.setAction("com.ivy.retrofit2.downloadapk");
                    sendBroadcast(intent);

                }else{
                    Log.d("main","error...:");
                }


            }
        }).start();


//        loadApk.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                try {
//                    byte[] bytes = response.body().bytes();
//                    SDCardHelper.saveFileToSDCardPrivateCacheDir(bytes,"111.apk",MainActivity.this);
//
//                    Log.d("main","success:");
//                    tvResult.setText("success:download");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.d("main","error:");
//                    tvResult.setText("error:");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
    }

    private void download() {
        Call<ResponseBody> downLoad = server.downLoad();
        downLoad.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    SDCardHelper.saveFileToSDCardPrivateCacheDir(response.body().bytes(),"112.jpg",MainActivity.this);

                    Log.d("main","success:");
                    tvResult.setText("success:download");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("main","error:");
                    tvResult.setText("error:");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     * GEt提交多个参数用到QueryMap
     */
    private void getInfo3() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id","1");
        Call<ResponseBody> quesInfo = server.getQuesInfo(map);
        quesInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.d("main","success:");
                    tvResult.setText("success:"+result);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("main","error:");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void login() {

        Call<ResponseBody> login = server.login("m8", "111222", "1");

        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.d("main","success:"+result);
                    tvResult.setText("success:"+result);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


    /**
     * 带参数，用到path和query的请求
     * @path
     * @query
     *
     */
    private void getInfo2() {
        Call<QiushiModel> latest = server.getQiuShiInfo("latest", 1);
        latest.enqueue(new Callback<QiushiModel>() {
            @Override
            public void onResponse(Call<QiushiModel> call, Response<QiushiModel> response) {
                Log.d("main","success:"+response.body().getCount());
                tvResult.setText("success:"+response.body().getCount());
            }

            @Override
            public void onFailure(Call<QiushiModel> call, Throwable t) {
                Log.d("main","error");
            }
        });
    }


    /**
     * 不带参数的Get请求
     */
    private void getInfo() {
        Call<ResponseBody> info = server.getInfo();
        info.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                final String result;
                try {
                    result = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvResult.setText(result);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("main","error");
            }
        });
    }




}
