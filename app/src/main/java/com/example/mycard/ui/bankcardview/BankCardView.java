package com.example.mycard.ui.bankcardview;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mycard.R;
import com.example.mycard.databinding.FragmentBankCardViewBinding;
import com.example.mycard.ui.cardlist.CardList;


public class BankCardView extends Fragment {
    private String bankName;
    private String bankID;
    private FragmentBankCardViewBinding bindingBCView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bindingBCView = FragmentBankCardViewBinding.inflate(inflater, container, false);
        View root = bindingBCView.getRoot();
        this.bankName = getArguments().getString("bankname");
        this.bankID = getArguments().getString("bankid");
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingBCView.bankName.setText(bankName);
        Bundle itemForCardListView = new Bundle();
        itemForCardListView.putInt("bankid", Integer.parseInt(bankID));
        CardList cardListLayout = new CardList();
        cardListLayout.setArguments(itemForCardListView);
        FragmentTransaction ftr2 = getActivity().getSupportFragmentManager().beginTransaction();
        ftr2.replace(R.id.frame_bankcard, cardListLayout);
        ftr2.commit();
    }
}