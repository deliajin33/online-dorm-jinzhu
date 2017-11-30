package com.example.delia.onlinedormselect.dormitory;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.delia.onlinedormselect.R;

public class PInfoCompletedActivity extends Activity implements View.OnClickListener
{
    private ImageView mBackBtn;

    private TextView mName, mId, mGender, mVeriCode, mRoom, mBuilding, mLocation, mGrade;

    private TextView mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info_completed);

        initView();
    }

    public void initView()
    {
        mBackBtn = (ImageView)findViewById(R.id.pc_back);
        mName = (TextView)findViewById(R.id.pc_stu_name);
        mId = (TextView)findViewById(R.id.pc_stu_id);
        mGender = (TextView)findViewById(R.id.pc_stu_gender);
        mVeriCode = (TextView)findViewById(R.id.pc_stu_veriCode);
        mRoom = (TextView)findViewById(R.id.pc_stu_room);
        mBuilding = (TextView)findViewById(R.id.pc_stu_building);
        mLocation = (TextView)findViewById(R.id.pc_stu_location);
        mGrade = (TextView)findViewById(R.id.pc_stu_grade);
        mLogout = (TextView)findViewById(R.id.pc_logout);

        mBackBtn.setOnClickListener(this);
        mLogout.setOnClickListener(this);

        Intent intent = getIntent();
        String[] s = intent.getStringArrayExtra("pInfo");
        Log.d("pInfo" , s[0]);
        mName.setText(s[0]);
        mId.setText(s[1]);
        mGender.setText(s[2]);
        mVeriCode.setText(s[3]);
        mLocation.setText(s[4]);
        mGrade.setText(s[5]);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.pc_back)
        {
            Intent i = new Intent(PInfoCompletedActivity.this, MainActivity.class);
            startActivity(i);
        }
        if(view.getId() == R.id.pc_logout)
        {
            Intent i = new Intent(PInfoCompletedActivity.this, MainActivity.class);
            startActivity(i);
        }

    }
}
