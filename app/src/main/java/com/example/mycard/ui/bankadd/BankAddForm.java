package com.example.mycard.ui.bankadd;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mycard.BankBaseProcessor;
import com.example.mycard.BaseProcessor;
import com.example.mycard.R;
import com.example.mycard.databinding.FragmentBankAddFormBinding;
import com.example.mycard.ui.banklist.BankList;

import javax.security.auth.callback.Callback;


public class BankAddForm extends Fragment {
    private String banknameTXT;
    private int bankColor;
    private FragmentBankAddFormBinding bindingBAdd;
    private Callback callback;
    private BankBaseProcessor baseProcessor;
    private Toast toast;
    private Context contextfr;
    private BankAddForm bankAddForm;

    public void setBankColor(int bankColor) {
        this.bankColor = bankColor;
    }

    public interface Callback {
        void bankAddCallBack (FragmentBankAddFormBinding bindingBAdd, BankAddForm bankAddForm);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.callback = (Callback) context;
        this.contextfr = context;
        this.bankAddForm = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bindingBAdd = FragmentBankAddFormBinding.inflate(inflater, container, false);
        View root = bindingBAdd.getRoot();
        return root;
    }
    //слушатель кнопки добавления записи в БД
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bankColor = getResources().getColor(R.color.card_color);
        baseProcessor = new BankBaseProcessor(view);
        //слушатель кнопки добавления
        bindingBAdd.bankButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                banknameTXT = bindingBAdd.bankName.getText().toString();
                if (banknameTXT.length() != 0) {
                    baseProcessor.baseBankAdd(banknameTXT, bankColor);
                    bindingBAdd.bankButtonAdd.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    getActivity().onBackPressed();
                } else {
                    toast = Toast.makeText(contextfr, getString(R.string.bank_add_msg), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        //слушатель кнопки возврата
        bindingBAdd.bankButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindingBAdd.bankButtonBack.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                getActivity().onBackPressed();
            }
        });
        //слушатель круглишка выбора цвета
        bindingBAdd.ColorCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.bankAddCallBack(bindingBAdd, bankAddForm);
            }
        });
    }
}