package com.example.mycard.ui.AboutApp;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycard.BuildConfig;
import com.example.mycard.R;
import com.example.mycard.databinding.FragmentAboutAppBinding;

public class AboutApp extends Fragment {

    private FragmentAboutAppBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAboutAppBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.aboutTxt.setText(Html.fromHtml(getString(R.string.aboutApp), FROM_HTML_MODE_LEGACY));
        binding.aboutTxt.setMovementMethod(LinkMovementMethod.getInstance());
        binding.versionTxt.setText(getString(R.string.app_version) + BuildConfig.VERSION_NAME);
        return root;
    }
}