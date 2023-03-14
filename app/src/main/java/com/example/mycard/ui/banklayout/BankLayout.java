package com.example.mycard.ui.banklayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mycard.databinding.BanklayoutBinding;

public class BankLayout extends Fragment {
    private BanklayoutBinding bindingBLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindingBLayout = BanklayoutBinding.inflate(inflater, container, false);
        View root = bindingBLayout.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!getArguments().isEmpty()){
            bindingBLayout.bankName.setText(getArguments().getString("bankname"));
            bindingBLayout.bankImage.setColorFilter(getArguments().getInt("bankcolor"));
            bindingBLayout.bankCardSettings.setVisibility(View.INVISIBLE);
        }
    }
}
