package com.example.delia.onlinedormselect.dormitory;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delia.onlinedormselect.bean.StudentInfo;
import com.example.delia.onlinedormselect.interpolator.JellyInterpolator;
import com.example.delia.onlinedormselect.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends Activity implements View.OnClickListener
{
    private static final int UPDATE_ERRCODE = 1;
    private static final int UPDATE_PERSONAL_INFO = 2;
    private SharedPreferences sharedPreferences;

    //http://showdoc.zhangqx.com/index.php?s=9&page_id=9

    private TextView mBtnLogin;

    private View progress;

    private View mInputLayout;

    private float mWidth, mHeight;

    private LinearLayout mName, mPsw;

    private EditText editText1, editText2;

    private String stu_id, stu_psw;

    private int errcode = 1;

    private StudentInfo studentInfo = null;

    //Handler来根据接收的消息，处理UI更新。子Thread线程发出Handler消息，通知更新UI
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case UPDATE_ERRCODE:

                    updateErrCode((int)msg.obj );

                    break;

                case UPDATE_PERSONAL_INFO:

                    updatePersonalInfo((StudentInfo)msg.obj );

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("config" , MODE_PRIVATE);

        initView();
    }

    private void initView()
    {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        editText1 = (EditText) findViewById(R.id.stu_id);
        editText2 = (EditText) findViewById(R.id.stu_psw);
        studentInfo=new StudentInfo();
        Log.d("dormSelect" , "test8"+studentInfo.getId());
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                stu_id = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });


        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                stu_psw = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        // 计算出控件的高与宽
        mWidth = mBtnLogin.getMeasuredWidth();
        mHeight = mBtnLogin.getMeasuredHeight();
        // 隐藏输入框
        mName.setVisibility(View.INVISIBLE);
        mPsw.setVisibility(View.INVISIBLE);
        inputAnimator(mInputLayout, mWidth, mHeight);

    }

    /**
     * 输入框的动画效果
     *
     * @param view
     *            控件
     * @param w
     *            宽
     * @param h
     *            高
     */
    private void inputAnimator(final View view, float w, float h)
    {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

                Log.d("dormSelect" , stu_id);
                Log.d("dormSelect" , stu_psw);

                //API接口
                final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/Login?username="+stu_id+"&password="+stu_psw;
                Log.d("dormSelect" , address);
                queryInternet(address);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

    }

    public void updateErrCode(int errcode)
    {
        Log.d("dormSelect" , "test4"+String.valueOf(errcode));
        if(errcode == 0)
        {
            final String address_personalInfo = "https://api.mysspku.com/index.php/V1/MobileCourse/getDetail?stuid="+stu_id;
            Log.d("dormSelect" , address_personalInfo);
            queryInternet(address_personalInfo);
        }
        else
        {
            progress.setVisibility(View.INVISIBLE);
            mName.setVisibility(View.VISIBLE);
            mPsw.setVisibility(View.VISIBLE);
            mInputLayout.setVisibility(View.VISIBLE);
        }
    }

    public void updatePersonalInfo(StudentInfo studentInfo)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("studentName" , studentInfo.getName());
        editor.putString("studentId" , studentInfo.getId());
        editor.putString("studentGender" , studentInfo.getGender());
        editor.commit();

        Log.d("dormSelect" , "test3");
        if(studentInfo.getBuilding() != null && studentInfo.getRoom() != null)
        {
            String[] pInfo = {studentInfo.getName(),studentInfo.getId(),studentInfo.getGender(),studentInfo.getVeriCode(),studentInfo.getLocation(),studentInfo.getGrade()};

            Intent i = new Intent(MainActivity.this, PInfoCompletedActivity.class);

            i.putExtra("pInfo" , pInfo);

            Log.d("pInfo1" , pInfo[0]);
            Log.d("pInfo1" , pInfo[1]);
            Log.d("pInfo1" , pInfo[2]);

            startActivity(i);
        }
        else
        {
            String[] pInfo = {studentInfo.getName(),studentInfo.getId(),studentInfo.getGender(),studentInfo.getVeriCode(),studentInfo.getLocation(),studentInfo.getGrade()};

            Intent i = new Intent(MainActivity.this,PersonalInfoActivity.class);

            i.putExtra("pInfo" , pInfo);

            Log.d("pInfo2" , pInfo[0]);
            Log.d("pInfo2" , pInfo[1]);
            Log.d("pInfo2" , pInfo[2]);

            startActivity(i);

        }
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

                    Log.d("dormSelect", String.valueOf(errcode)+" "+studentInfo.getId());
                    if(studentInfo.getId()!=null&&errcode==0)
                    {

                        Log.d("dormSelect" , "test");
                        //子线程与主线程的通信机制
                        Message msg = new Message();
                        msg.what = UPDATE_PERSONAL_INFO;
                        msg.obj = studentInfo;
                        mHandler.sendMessage(msg);
                    }
                    else {
                        Log.d("dormSelect", String.valueOf(errcode));

                        //子线程与主线程的通信机制
                        Message msg = new Message();
                        msg.what = UPDATE_ERRCODE;
                        msg.obj = errcode;
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
            // 此时还未读取任何json文本，直接读取就是一个JSONObject对象。
            JSONObject loginfos = (JSONObject) jsonParser.nextValue();
            // 接下来的就是JSON对象的操作了
            errcode=Integer.parseInt(loginfos.getString("errcode"));
            Log.d("dormSelect" , "test5"+loginfos.getString("data"));

            String datas=loginfos.getString("data");
            JSONTokener jsonParser_data = new JSONTokener(datas);
            JSONObject stu_data = (JSONObject) jsonParser_data.nextValue();
            if(stu_data.has("studentid")==true)
            {
                Log.d("dormSelect" , "stuid"+stu_data.getString("studentid"));
                Log.d("dormSelect" , "stuid"+stu_data.getString("name"));
                Log.d("dormSelect" , "stuid"+stu_data.getString("gender"));
                Log.d("dormSelect" , "stuid"+stu_data.getString("vcode"));
                Log.d("dormSelect" , "stuid"+stu_data.getString("location"));
                Log.d("dormSelect" , "stuid"+stu_data.getString("grade"));
                studentInfo.setName(stu_data.getString("name"));
                Log.d("json" , stu_data.getString("name"));
                studentInfo.setId(stu_data.getString("studentid"));
                studentInfo.setGender(stu_data.getString("gender"));
                Log.d("json" , stu_data.getString("gender"));
                studentInfo.setVeriCode(stu_data.getString("vcode"));
                if(stu_data.has("room")==true)
                    studentInfo.setRoom(stu_data.getString("room"));
                if(stu_data.has("building")==true)
                    studentInfo.setBuilding(stu_data.getString("building"));
                studentInfo.setLocation(stu_data.getString("location"));
                studentInfo.setGrade(stu_data.getString("grade"));

            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    /**
     * 出现进度动画
     *
     * @param view
     */
    private void progressAnimator(final View view)
    {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
    }

}
class MyTrustManager implements X509TrustManager
{
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // TODO Auto-generated method stub
    }
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // TODO Auto-generated method stub
    }
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        // TODO Auto-generated method stub
        return null;
    }

}
class MyHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        // TODO Auto-generated method stub
        return true;
    }

}
