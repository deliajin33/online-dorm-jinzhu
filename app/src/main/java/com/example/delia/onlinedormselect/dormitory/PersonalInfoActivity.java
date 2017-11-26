package com.example.delia.onlinedormselect.dormitory;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.delia.onlinedormselect.R;

public class PersonalInfoActivity extends Activity implements View.OnClickListener
{
    private ImageView mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    public void initView()
    {
        mBackBtn = (ImageView)findViewById(R.id.back_login);
        mBackBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.back_login)
        {
            Intent i = new Intent(PersonalInfoActivity.this, MainActivity.class);
            startActivity(i);
        }

    }
}
