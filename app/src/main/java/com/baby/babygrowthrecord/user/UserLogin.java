package com.baby.babygrowthrecord.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.baby.babygrowthrecord.R;

/**
 * Created by think on 2016/11/22.
 */
public class UserLogin extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
    }

    public void backOnClick(View view){
        finish();
    }
}