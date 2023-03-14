package com.example.mycard.ui.settings;

import static android.content.Context.MODE_PRIVATE;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mycard.databinding.FragmentAppSettingsBinding;


public class AppSettings extends Fragment {
    public Callback callBack;
    private FragmentAppSettingsBinding bindingAppsett;
    private SharedPreferences settings;
    private static final String DARK_MODE = "DarkMode";
    private static final String PROTECTED_ENTER = "Protect";
    private static final String PREFS_FILE = "Settings";

    public interface Callback {
        void appSettingsCallbackPinSet();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.callBack = (Callback) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        settings = this.getActivity().getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        bindingAppsett = FragmentAppSettingsBinding.inflate(inflater, container, false);
        View root = bindingAppsett.getRoot();
        SharedPreferences.Editor prefEditor = settings.edit();
        bindingAppsett.settingSwitchNightMode.setChecked(settings.getBoolean(DARK_MODE,false));
        bindingAppsett.settingSwitchNightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bindingAppsett.settingSwitchNightMode.isChecked()){
                    prefEditor.putBoolean(DARK_MODE, true);
                    prefEditor.apply();
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
                }else{
                    prefEditor.putBoolean(DARK_MODE, false);
                    prefEditor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                bindingAppsett.settingSwitchNightMode.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        });
        bindingAppsett.settingSwitchProtectedEnter.setChecked(settings.getBoolean(PROTECTED_ENTER,false));
        bindingAppsett.settingSwitchProtectedEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bindingAppsett.settingSwitchProtectedEnter.isChecked()){
                    callBack.appSettingsCallbackPinSet();
                }else{
                    prefEditor.putBoolean(PROTECTED_ENTER, false);
                }
                prefEditor.apply();
                bindingAppsett.settingSwitchProtectedEnter.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        bindingAppsett.settingSwitchProtectedEnter.setChecked(settings.getBoolean(PROTECTED_ENTER,false));
    }
}
