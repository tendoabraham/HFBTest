package com.elmahousingfinanceug_test.launch.rao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.elmahousingfinanceug_test.R;

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