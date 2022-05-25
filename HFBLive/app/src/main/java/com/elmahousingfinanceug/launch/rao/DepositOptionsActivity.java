package com.elmahousingfinanceug.launch.rao;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.elmahousingfinanceug.R;


public class DepositOptionsActivity extends AppCompatActivity {
     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_options);
        TextView done = findViewById(R.id.done);
        done.setOnClickListener(v -> {
            finish();
        });
    }
    
}