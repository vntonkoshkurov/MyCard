package com.example.mycard.ui.cardadd;


import static android.content.Context.MODE_PRIVATE;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mycard.BaseProcessor;
import com.example.mycard.R;
import com.example.mycard.databinding.FragmentCardAddFormBinding;
import com.example.mycard.intedrface.MyTextEditMethod;

public class CardAddForm extends Fragment implements MyTextEditMethod {
    private FragmentCardAddFormBinding bindingCAdd;
    private String cardnumTXT;
    private String cardmonthTXT;
    private String cardyearTXT;
    private String cardcvvTXT;
    private String carddescrTXT;
    private int cardColor;
    private int paySystemINT;
    private Callback callback;
    private BaseProcessor baseProcessor;
    private Bundle bundle;
    private CardAddForm cardAddForm;

    public void setCardColor(int cardColor) {
        this.cardColor = cardColor;
    }

    public interface Callback {
        void cardAddCallBack(FragmentCardAddFormBinding bindingCAdd, CardAddForm cardAddForm);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.callback = (Callback) context;
        this.cardAddForm = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bindingCAdd = FragmentCardAddFormBinding.inflate(inflater, container, false);
        View root = bindingCAdd.getRoot();
        bundle = getArguments();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseProcessor = new BaseProcessor(view);
        cardColor = getResources().getColor(R.color.card_color);
        //слушатель для кнопки добавления
        bindingCAdd.cardButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cardnumTXT = bindingCAdd.cardNum.getText().toString();
                cardmonthTXT = bindingCAdd.cardMonth.getText().toString();
                cardyearTXT = bindingCAdd.cardYear.getText().toString();
                cardcvvTXT = bindingCAdd.cardCvv.getText().toString();
                carddescrTXT = bindingCAdd.cardDescription.getText().toString();
                paySystemINT = detectPaySystem(cardnumTXT);
                FragmentManager fm = getActivity().getFragmentManager();
                //проверка корректности введенных данных перез из загрузкой в БД
                if (cardDataCheck(fm, cardnumTXT, cardmonthTXT, cardyearTXT, cardcvvTXT)) {
                    //запускаем метод изменения данных в выбранной карте
                    //в зависимости от того, откуда был вызван фрагмент, производится разная обработка записи
                    //к примеру, если фрагмент был открыт из карточки банка, то в базу запишется ID банка, в противном случае будет нулевое значение
                    if (bundle != null) {
                        baseProcessor.baseCardAdd(cardnumTXT, cardmonthTXT, cardyearTXT,
                                cardcvvTXT, carddescrTXT, String.valueOf(paySystemINT),cardColor, bundle.getInt("bankid"));
                    } else {
                        baseProcessor.baseCardAdd(cardnumTXT, cardmonthTXT, cardyearTXT,
                                cardcvvTXT, carddescrTXT, String.valueOf(paySystemINT),cardColor, 0);
                    }
                    bindingCAdd.cardButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    getActivity().onBackPressed();
                }
            }
        });
        //слушатель для кнопки закрытия окна создания новой карты
        bindingCAdd.backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bindingCAdd.backButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                getActivity().onBackPressed();
            }
        });
        //слушатель круглишка выбора цвета
        bindingCAdd.ColorCircle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.cardAddCallBack(bindingCAdd, cardAddForm);
            }
        });
    }
    //разделение символов в номере карты
    @Override
    public String cardNumFormat(String cardNum) {
        return MyTextEditMethod.super.cardNumFormat(cardNum);
    }
    //проверка введенных данных по карте
    @Override
    public Boolean cardDataCheck(FragmentManager fm, String cardNum, String cardMonth, String cardYear, String cardCVV) {
        return MyTextEditMethod.super.cardDataCheck(fm, cardNum, cardMonth, cardYear, cardCVV);
    }
    //определение платежной системы
    @Override
    public int detectPaySystem(String cardNum) {
        return MyTextEditMethod.super.detectPaySystem(cardNum);
    }

}