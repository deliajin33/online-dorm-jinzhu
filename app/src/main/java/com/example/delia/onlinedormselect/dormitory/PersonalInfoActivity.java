package com.example.delia.onlinedormselect.dormitory;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delia.onlinedormselect.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PersonalInfoActivity extends Activity implements View.OnClickListener
{
    private ImageView mBackBtn;

    private TextView mName, mId , mGender, mVeriCode;

    private TextView mProcedureBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);

        initView();
    }

    public void initView()
    {
        mBackBtn = (ImageView)findViewById(R.id.back_login);
        mName = (TextView)findViewById(R.id.pstu_name);
        mId = (TextView)findViewById(R.id.pstu_id);
        mGender = (TextView)findViewById(R.id.pstu_gender);
        mVeriCode = (TextView)findViewById(R.id.pstu_veriCode);
        mProcedureBtn = (TextView)findViewById(R.id.start_procedure);

        mBackBtn.setOnClickListener(this);
        mProcedureBtn.setOnClickListener(this);

        Intent intent = getIntent();
        String[] s = intent.getStringArrayExtra("pInfo");
        Log.d("pInfo" , s[0]);
        mName.setText(s[0]);
        mId.setText(s[1]);
        mGender.setText(s[2]);
        mVeriCode.setText(s[3]);

    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.back_login)
        {
            Intent i = new Intent(PersonalInfoActivity.this, MainActivity.class);
            startActivity(i);
        }
        if(view.getId() == R.id.start_procedure)
        {
            Intent i = new Intent(PersonalInfoActivity.this, ModeActivity.class);
            startActivity(i);
        }

    }



}
