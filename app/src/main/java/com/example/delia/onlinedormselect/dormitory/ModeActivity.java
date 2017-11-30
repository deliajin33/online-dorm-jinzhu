package com.example.delia.onlinedormselect.dormitory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.delia.onlinedormselect.R;

public class ModeActivity extends Activity implements View.OnClickListener
{

    private TextView modeOne,modeTwo,modeThree,modeFour;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_layout);
        initView();
    }
    public void initView()
    {
        modeOne = (TextView)findViewById(R.id.one_mode);
        modeTwo = (TextView)findViewById(R.id.two_mode);
        modeThree = (TextView)findViewById(R.id.three_mode);
        modeFour = (TextView)findViewById(R.id.four_mode);

        modeOne.setOnClickListener(this);
        modeTwo.setOnClickListener(this);
        modeThree.setOnClickListener(this);
        modeFour.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.one_mode)
        {
            Intent i = new Intent(ModeActivity.this, OneModeActivity.class);
            startActivity(i);
        }
        if(view.getId() == R.id.two_mode)
        {
            Intent i = new Intent(ModeActivity.this, TwoModeActivity.class);
            startActivity(i);
        }
        if(view.getId() == R.id.three_mode)
        {
            Intent i = new Intent(ModeActivity.this, ThreeModeActivity.class);
            startActivity(i);
        }
        if(view.getId() == R.id.four_mode)
        {
            Intent i = new Intent(ModeActivity.this, FourModeActivity.class);
            startActivity(i);
        }

    }




}
