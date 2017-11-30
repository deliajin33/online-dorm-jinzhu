package com.example.delia.onlinedormselect.dormitory;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.delia.onlinedormselect.R;
import com.example.delia.onlinedormselect.bean.RoomInfo;
import com.example.delia.onlinedormselect.bean.StudentInfo;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class OneModeActivity extends Activity implements View.OnClickListener
{
    private static final int UPDATE_ROOM_INFO = 1;

    private SharedPreferences sharedPreferences;

    private TextView mName, mId, mGender;

    private TextView mBuilding1Name , mBuilding2Name, mBuilding3Name, mBuilding4Name, mBuilding5Name;

    private TextView mBuilding1,mBuilding2,mBuilding3,mBuilding4,mBuilding5;

    private TextView mVerify;

    private int errcode = 1;

    private RoomInfo roomInfo = null;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case UPDATE_ROOM_INFO:

                    updateRoomInfo((RoomInfo) msg.obj );

                    break;

                default:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.one_mode_layout);

        sharedPreferences = getSharedPreferences("config" , MODE_PRIVATE);

        roomInfo = new RoomInfo();

        initView();
    }

    public void initView()
    {
        mBuilding1Name = (TextView)findViewById(R.id.one_building1_name);
        mBuilding2Name = (TextView)findViewById(R.id.one_building2_name);
        mBuilding3Name = (TextView)findViewById(R.id.one_building3_name);
        mBuilding4Name = (TextView)findViewById(R.id.one_building4_name);
        mBuilding5Name = (TextView)findViewById(R.id.one_building5_name);

        mBuilding1 = (TextView)findViewById(R.id.one_building1);
        mBuilding2 = (TextView)findViewById(R.id.one_building2);
        mBuilding3 = (TextView)findViewById(R.id.one_building3);
        mBuilding4 = (TextView)findViewById(R.id.one_building4);
        mBuilding5 = (TextView)findViewById(R.id.one_building5);

        mName = (TextView)findViewById(R.id.one_name);
        mId = (TextView)findViewById(R.id.one_id);
        mGender = (TextView)findViewById(R.id.one_gender);

        mVerify = (TextView)findViewById(R.id.one_verify);
        mVerify.setOnClickListener(this);

        String name = sharedPreferences.getString("studentName" , "保密");
        String id = sharedPreferences.getString("studentId" , "0000000000");
        String gender = sharedPreferences.getString("studentGender" , "男");

        mName.setText(name);
        mId.setText(id);
        mGender.setText(gender);

        String genderCode = "0";
        if(gender == "男")
        {
            genderCode = "0";
        }
        else
        {
            genderCode = "1";
        }

        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getRoom?gender="+genderCode;
        queryInternet(address);
    }

    public void queryInternet(final String address)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run() {
                //API接口
                HttpURLConnection con = null;
                try
                {
                    SSLContext sc = SSLContext.getInstance("TLS");
                    sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                    HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());

                    URL url = new URL(address);
                    con = (HttpsURLConnection) url.openConnection();

                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);

                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();

                    String str;
                    while ( (str = reader.readLine()) != null )
                    {
                        response.append(str);
                    }
                    String jsonData = response.toString();

                    Log.d("dormSelect" , jsonData);
                    //调用解析方法
                    parseJSON(jsonData);

                    if(errcode == 0 && roomInfo.getFive() != null)
                    {

                        Log.d("dormSelect" , "test");
                        //子线程与主线程的通信机制
                        Message msg = new Message();
                        msg.what = UPDATE_ROOM_INFO;
                        msg.obj = roomInfo;
                        mHandler.sendMessage(msg);
                    }

                }
                catch (Exception e)
                {
                    Log.d("dormSelect", "error");
                    e.printStackTrace();
                }
                finally
                {
                    if (con != null)
                    {
                        con.disconnect();
                    }
                }

            }


        }).start();//将该线程加入资源等待队列

    }


    private void parseJSON(String jsonData)
    {
        try
        {
            JSONTokener jsonParser = new JSONTokener(jsonData);
            JSONObject loginfos = (JSONObject) jsonParser.nextValue();
            errcode=Integer.parseInt(loginfos.getString("errcode"));

            String datas=loginfos.getString("data");
            JSONTokener jsonParser_data = new JSONTokener(datas);
            JSONObject room_data = (JSONObject) jsonParser_data.nextValue();

            if(room_data.has("5")==true)
            {
                roomInfo.setFive(room_data.getString("5"));
                roomInfo.setThirteen(room_data.getString("13"));
                roomInfo.setFourteen(room_data.getString("14"));
                roomInfo.setEight((room_data.getString("8")));
                roomInfo.setNine(room_data.getString("9"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void updateRoomInfo(RoomInfo roomInfo)
    {

        if(!roomInfo.getFive().equals("0"))
        {
            mBuilding1Name.setText("5号楼");
            mBuilding1.setText(roomInfo.getFive());
        }
        if(!roomInfo.getThirteen().equals("0"))
        {
            mBuilding2Name.setText("13号楼");
            mBuilding2.setText(roomInfo.getThirteen());
        }
        if(!roomInfo.getFourteen().equals("0"))
        {
            mBuilding3Name.setText("14号楼");
            mBuilding3.setText(roomInfo.getFourteen());
        }
        if(!roomInfo.getEight().equals("0"))
        {
            mBuilding4Name.setText("8号楼");
            mBuilding4.setText(roomInfo.getEight());
        }
        if(!roomInfo.getNine().equals("0"))
        {
            mBuilding5Name.setText("9号楼");
            mBuilding5.setText(roomInfo.getNine());
        }
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.one_verify )
        {

        }
    }
}

