package com.example.delia.onlinedormselect.dormitory;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.delia.onlinedormselect.R;

public class DormInfoActivity extends Activity
{

    private TextView mName, mId, mGender;
    private TextView mBuilding, mRoom;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dorm_info_layout);
        sharedPreferences = getSharedPreferences("config" , MODE_PRIVATE);
        initView();
    }
    public void initView()
    {
        mName = (TextView)findViewById(R.id.room_name);
        mId = (TextView)findViewById(R.id.room_id);
        mGender = (TextView)findViewById(R.id.room_gender);
        mBuilding = (TextView)findViewById(R.id.room_building);
        mRoom = (TextView)findViewById(R.id.room_room);
    }
}
