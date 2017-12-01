package com.example.delia.onlinedormselect.dormitory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.delia.onlinedormselect.R;

public class DormSuccessActivity extends Activity implements View.OnClickListener
{
    private TextView success_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dorm_success_layout);
        initView();
    }
    public void initView()
    {
        success_return = (TextView)findViewById(R.id.success_return);
        success_return.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.success_return )
        {
            Intent intent = new Intent(DormSuccessActivity.this , DormInfoActivity.class);
            startActivity(intent);


        }

    }
}
