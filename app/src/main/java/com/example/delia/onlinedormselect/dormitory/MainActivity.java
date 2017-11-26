package com.example.delia.onlinedormselect.dormitory;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delia.onlinedormselect.interpolator.JellyInterpolator;
import com.example.delia.onlinedormselect.R;

import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener
{
    //http://showdoc.zhangqx.com/index.php?s=9&page_id=9

    private TextView mBtnLogin;

    private View progress;

    private View mInputLayout;

    private float mWidth, mHeight;

    private LinearLayout mName, mPsw;

    private EditText editText1, editText2;

    private String stu_id, stu_psw;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

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
        stu_id = editText1.getText().toString();
        stu_psw = editText2.getText().toString();

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


                //API接口
                final String address = "http://api.mysspku.com/index.php/V1/MobileCourse/Login?username="+stu_id+"password="+stu_psw;
                new Thread(new Runnable()
                {
                    @Override
                    public void run() {
                        HttpURLConnection con = null;
                        try
                        {
                            URL url = new URL(address);
                            con = (HttpURLConnection) url.openConnection();

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

                            int errcode = parseJSON(jsonData);

                            if(errcode == 0)
                            {
                                Intent i = new Intent(MainActivity.this,PersonalInfoActivity.class);

                                /**在启动另外一个Activity的时候，有两种方法，
                                 * 一种是直接使用startActivity，
                                 * 另外一种就是使用startActivityForResult**/
                                startActivity(i);

                                //startActivityForResult( i , 1 );
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this , "学号或密码错误" , Toast.LENGTH_LONG).show();
                            }








                            /**
                             //跳转界面或者更新界面
                             if(todayWeather != null)
                             {
                             Log.d("myWeather" , todayWeather.toString());

                             //子线程与主线程的通信机制
                             Message msg = new Message();
                             msg.what = UPDATE_TODAY_WEATHER;
                             msg.obj = todayWeather;
                             mHandler.sendMessage(msg);
                             }**/

                        }
                        catch (Exception e)
                        {
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

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

    }



    private int parseJSON(String jsonData)
    {
        int errcode = 1;
        //String login_info = null;

        try {
            //将json字符串jsonData装入JSON数组，即JSONArray
            //jsonData可以是从文件中读取，也可以从服务器端获得
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i< jsonArray.length(); i++)
            {
                //循环遍历，依次取出JSONObject对象
                //用getInt和getString方法取出对应键值
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                errcode = jsonObject.getInt("errocode");
                //login_info = jsonObject.getString("stu_name");
                Log.d("MainActivity","errocode: " + errcode);

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return errcode;
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
