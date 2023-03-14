package com.example.mycard.ui.cardlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mycard.databinding.CardlayoutBinding;

public class Cardlayout extends Fragment {
    private CardlayoutBinding bindingCLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bindingCLayout = CardlayoutBinding.inflate(inflater, container, false);
        View root = bindingCLayout.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!getArguments().isEmpty()){
            int i;
            if (getArguments().getString("cardnum").length()<=19){
                bindingCLayout.cardNum.setTextSize(21);
            }else{
                bindingCLayout.cardNum.setTextSize(18);
            }
            bindingCLayout.cardNum.setText(getArguments().getString("cardnum"));
            bindingCLayout.cardMonth.setText(getArguments().getString("cardmonth"));
            bindingCLayout.cardYear.setText(getArguments().getString("cardyear"));
            bindingCLayout.cardCvv.setText(getArguments().getString("cardcvv"));
            bindingCLayout.cardDescr.setText(getArguments().getString("description"));
            bindingCLayout.paysysImg.setImageResource(getArguments().getInt("paysys"));
            bindingCLayout.cardImage.setColorFilter(getArguments().getInt("cardcolor"));
        }
    }

}