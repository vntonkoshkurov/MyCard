package com.example.mycard;

import static android.text.Html.FROM_HTML_MODE_LEGACY;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import com.example.mycard.databinding.ActivityIntroductionBinding;

public class Introduction extends AppCompatActivity {

    private ActivityIntroductionBinding binding;
    private static final String PREFS_FILE = "Settings";
    private static final String INTRODUCTION = "Introduction";
    SharedPreferences settings;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroductionBinding.inflate(getLayoutInflater());
        settings = getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        setContentView(binding.getRoot());
        binding.introductionTxt.setText(Html.fromHtml(getString(R.string.confidencial_description), FROM_HTML_MODE_LEGACY));
        binding.introductionTxt.setMovementMethod(LinkMovementMethod.getInstance());
        binding.introductionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefEditor.putBoolean(INTRODUCTION, true);
                prefEditor.apply();
                Intent intent = new Intent(Introduction.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}