package com.example.mycard.ui.setPIN;

import static android.content.Context.MODE_PRIVATE;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.fragment.app.Fragment;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.mycard.R;
import com.example.mycard.databinding.FragmentSetPinBinding;
import com.example.mycard.dialogs.CheckCardDataDialog;

public class setPin extends Fragment {

    private FragmentSetPinBinding binding;
    private SharedPreferences settings;
    private Toast toast;
    private Context contextfr;
    private BiometricManager biometricManager;
    private static final String PREFS_FILE = "Settings";
    private static final String PIN = "Pin";
    private static final String TRY = "TryCount";
    private static final String PROTECTED_ENTER = "Protect";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.contextfr = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSetPinBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        settings = this.getActivity().getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        biometricManager = BiometricManager.from(getContext());

        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                == BiometricManager.BIOMETRIC_SUCCESS){
            binding.txtStatus.setText(getString(R.string.check_idendification_good));
        } else {
            binding.txtStatus.setText(getString(R.string.check_idendification_bad));
        }

        binding.pinsetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckCardDataDialog checkCardDataDialog = new CheckCardDataDialog();
                FragmentManager fm = getActivity().getFragmentManager();
                Bundle nameOfBadString;
                if (binding.pin1.getText().toString().length()>=4) {
                    if (binding.pin1.getText().toString().equals(binding.pin2.getText().toString())
                            && binding.pin2.getText().toString().length() != 0) {
                        prefEditor.putString(PIN, binding.pin1.getText().toString());
                        prefEditor.putBoolean(PROTECTED_ENTER, true);
                        prefEditor.putInt(TRY, 10);
                        prefEditor.apply();
                        toast = Toast.makeText(contextfr, getString(R.string.pin_set_ok), Toast.LENGTH_LONG);
                        binding.pinsetButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        toast.show();
                        getActivity().onBackPressed();
                    } else {
                            nameOfBadString = new Bundle();
                            binding.pinsetButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                            nameOfBadString.putInt("name", R.string.pin_set_incorrect);
                            checkCardDataDialog.setArguments(nameOfBadString);
                            checkCardDataDialog.show(fm, "custom" );
                    }
                } else {
                    nameOfBadString = new Bundle();
                    binding.pinsetButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    nameOfBadString.putInt("name", R.string.pin_length_check);
                    checkCardDataDialog.setArguments(nameOfBadString);
                    checkCardDataDialog.show(fm, "custom" );
                }
            }
        });
        return root;
    }
}