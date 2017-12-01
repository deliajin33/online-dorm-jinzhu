package com.example.delia.onlinedormselect.dormitory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.delia.onlinedormselect.R;

public class DormInfoActivity extends Activity implements View.OnClickListener
{

    private TextView mName, mId, mGender;
    private TextView mBuilding, mRoom;
    private SharedPreferences sharedPreferences;
    private String name,id,gender,building,room;
    private TextView room_verify;

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
        room_verify = (TextView)findViewById(R.id.room_verify);
        room_verify.setOnClickListener(this);

        name = sharedPreferences.getString("studentName" , "保密");
        id = sharedPreferences.getString("studentId" , "0000000000");
        gender = sharedPreferences.getString("studentGender" , "男");
        building = sharedPreferences.getString("studentBuilding" , "5号楼");
        room = sharedPreferences.getString("studentRoom" , "5001");

        mName.setText(name);
        mId.setText(id);
        mGender.setText(gender);
        mBuilding.setText(building);
        mRoom.setText(room);
    }
    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.room_verify )
        {
            Intent intent = new Intent(DormInfoActivity.this , MainActivity.class);
            startActivity(intent);
        }

    }
}
