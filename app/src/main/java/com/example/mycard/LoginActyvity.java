package com.example.mycard;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Toast;
import com.example.mycard.databinding.ActivityLoginActyvityBinding;
import java.util.concurrent.Executor;

public class LoginActyvity extends AppCompatActivity {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private BiometricManager biometricManager;
    private Toast toast;
    SharedPreferences settings;
    ActivityLoginActyvityBinding binding;
    private static final String PROTECTED_ENTER = "Protect";
    private static final String DARK_MODE = "DarkMode";
    private static final String PREFS_FILE = "Settings";
    private static final String PIN = "Pin";
    private static final String TRY = "TryCount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginActyvityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        settings = getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        biometricManager = BiometricManager.from(this);

        if (settings.getBoolean(DARK_MODE,false)){
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
        }

        if (!settings.getBoolean(PROTECTED_ENTER, false)) {
            switchActivity();
            finish();
        } else {

            if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                    == BiometricManager.BIOMETRIC_SUCCESS) {
                binding.textView2.setText(getString(R.string.check_idendification_good));
                showBiometricPrompt(binding.getRoot());
            } else {
                binding.textView2.setText(getString(R.string.check_idendification_bad));
                binding.fingerBtn.setVisibility(View.GONE);
            }
        }

        binding.btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btn0.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                settext(0);
            }
        });

        binding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btn1.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                settext(1);
            }
        });

        binding.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btn2.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                settext(2);
            }
        });

        binding.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btn3.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                settext(3);
            }
        });

        binding.btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btn4.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                settext(4);
            }
        });

        binding.btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btn5.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                settext(5);
            }
        });

        binding.btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btn6.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                settext(6);
            }
        });

        binding.btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btn7.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                settext(7);
            }
        });

        binding.btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btn8.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                settext(8);
            }
        });

        binding.btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btn9.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                settext(9);
            }
        });

        binding.fingerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.fingerBtn.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                showBiometricPrompt(binding.getRoot());
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.loginBtn.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if (binding.editTextTextPassword.length() >= 4) {
                    if (settings.getString(PIN, "0000").equals(binding.editTextTextPassword.getText().toString())) {
                        switchActivity();
                    } else {
                        if (binding.editTextTextPassword.getText().toString().length() == 0) {
                            toast = Toast.makeText(getApplicationContext(), getString(R.string.pin_donot_enter), Toast.LENGTH_LONG);
                            toast.show();
                            binding.loginBtn.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        } else {
                            checkTryCounts(v);
                        }
                    }
                } else {
                    toast = Toast.makeText(getApplicationContext(), getString(R.string.pin_length_check), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.backBtn.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if (binding.editTextTextPassword.length() !=0) {
                    StringBuffer strBuffer = new StringBuffer(binding.editTextTextPassword.getText());
                    strBuffer.deleteCharAt(binding.editTextTextPassword.length() - 1);
                    binding.editTextTextPassword.setText(strBuffer);
                }
            }
        });
    }

    private void settext(int i) {
        binding.editTextTextPassword.setText(binding.editTextTextPassword.getText() + String.valueOf(i));
    }

    public void showBiometricPrompt(View v) {
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActyvity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                checkTryCounts(v);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                switchActivity();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                checkTryCounts(v);
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.biometricPrompt_title))
                .setSubtitle(getString(R.string.biometricPrompt_subtitle))
                .setNegativeButtonText(getString(R.string.biometricPrompt_cancel))
                //.setDeviceCredentialAllowed(true)
                .build();
        biometricPrompt.authenticate(promptInfo);
    }

    public void switchActivity() {
        SharedPreferences.Editor prefEditor = settings.edit();
        Intent intent = new Intent(LoginActyvity.this, MainActivity.class);
        prefEditor.putInt(TRY, 10);
        prefEditor.apply();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void checkTryCounts(View v) {
        BaseProcessor baseProcessor;
        baseProcessor = new BaseProcessor(v);
        SharedPreferences.Editor prefEditor = settings.edit();
        int tryCount = settings.getInt(TRY, 10);
        if (tryCount==0){
            if (baseProcessor.baseClear()){
                prefEditor.putBoolean(PROTECTED_ENTER, false);
                prefEditor.apply();
                switchActivity();
            }
        } else {
            toast = Toast.makeText(getApplicationContext(), getString(R.string.pin_check_incorrect) + " " +
                    String.valueOf(tryCount), Toast.LENGTH_LONG);
            toast.show();
            tryCount--;
            prefEditor.putInt(TRY, tryCount);
            prefEditor.apply();
        }
    }
}