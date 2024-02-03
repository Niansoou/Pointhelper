package com.example.pointhelper.ui.theme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pointhelper.R;

import java.time.Instant;

import androidx.annotation.Nullable;
import androidx.compose.foundation.interaction.DragInteraction;

public class WellcomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wellcome_page);
        Button bt =(Button)findViewById(R.id.button_send);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(WellcomeActivity.this,SelectActivity.class);
                startActivity(intent);
            }
        });

    }

}
