package com.example.mycard.ui.bankview;

import static android.content.Context.MODE_PRIVATE;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.mycard.BankBaseProcessor;
import com.example.mycard.R;
import com.example.mycard.databinding.FragmentBankViewBinding;
import com.example.mycard.ui.banklayout.BankLayout;

public class BankView extends Fragment {
    private String bankName;
    private String bankID;
    private int color;
    private FragmentBankViewBinding bindingBView;
    private Callback callback;
    private BankBaseProcessor baseProcessor;
    private BankView bankView;

    public void setColor(int color) {
        this.color = color;
    }

    public interface Callback {
        void bankViewCallback (FragmentBankViewBinding bindingBView, BankView bankView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.callback = (BankView.Callback) context;
        this.bankView = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bindingBView = FragmentBankViewBinding.inflate(inflater, container, false);
        View root = bindingBView.getRoot();
        this.bankName = getArguments().getString("bankname");
        this.color = getArguments().getInt("bankcolor");
        this.bankID = getArguments().getString("bankid");
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseProcessor = new BankBaseProcessor(view);
        bindingBView.bankNameView.setText(bankName);
        bindingBView.bankColor.setColorFilter(color);
        //слушатель для кнопки Назад
        bindingBView.backinviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //откатываем фрагмент в стеке
                bindingBView.backinviewButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                getActivity().onBackPressed();
            }
        });
        //Слушатель для кнопки Изменить
        bindingBView.changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankName = bindingBView.bankNameView.getText().toString();
                FragmentManager fm = getActivity().getFragmentManager();
                baseProcessor.baseBankUpdate(bankID, bankName, color);
                    bindingBView.changeButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    getActivity().onBackPressed();
            }
        });
        //слушатель круглишка выбора цвета
        bindingBView.bankColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.bankViewCallback(bindingBView, bankView);
            }
        });
        //ниже передаем данные банка в слой, отображающий карточку банка в нашем фрагменте
        //т.е. фрагмент со слоем карты находится внутри нашего фрагмента
        //и после передачи загружаем фрагмент со слоем карты
        Bundle itemselected = new Bundle();
        itemselected.putString("bankname", bankName);
        itemselected.putInt("bankcolor", color);
        BankLayout banklayout = new BankLayout();
        banklayout.setArguments(itemselected);
        FragmentTransaction ftr = getActivity().getSupportFragmentManager().beginTransaction();
        ftr.replace(R.id.frame_bankdview, banklayout);
        ftr.commit();
    }
}