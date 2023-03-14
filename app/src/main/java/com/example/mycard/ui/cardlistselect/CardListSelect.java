package com.example.mycard.ui.cardlistselect;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mycard.databinding.FragmentCardListSelectBinding;

public class CardListSelect extends Fragment {
    FragmentCardListSelectBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCardListSelectBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
}