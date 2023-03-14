package com.example.mycard.ui.cardview;

import static android.content.Context.MODE_PRIVATE;
import static com.example.mycard.DatabaseHelper.COLUMN_ID;
import static com.example.mycard.DatabaseHelper.TABLE;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.mycard.BaseProcessor;
import com.example.mycard.DatabaseHelper;
import com.example.mycard.R;
import com.example.mycard.databinding.FragmentCardViewBinding;
import com.example.mycard.intedrface.MyTextEditMethod;
import com.example.mycard.ui.cardlayout.Cardlayout;


public class CardView extends Fragment implements MyTextEditMethod {
    private FragmentCardViewBinding bindingCView;
    private String cardnumTXT;
    private String cardmonthTXT;
    private String cardyearTXT;
    private String cardcvvTXT;
    private String cardIDTXT;
    private String cardDescrTXT;
    private int cardBankID;
    private int cardColor;
    private int paySysINT;
    private Toast toast;
    private Context contextfr;
    public Callback callBack;
    private BaseProcessor baseProcessor;
    private CardView cardView;

    public void setCardColor(int cardColor) {
        this.cardColor = cardColor;
    }

    public interface Callback {
        void cardViewCallback(FragmentCardViewBinding bindingCView, CardView cardView);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        contextfr = context;
        this.callBack = (Callback) context;
        this.cardView = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bindingCView = FragmentCardViewBinding.inflate(inflater, container, false);
        View root = bindingCView.getRoot();
        this.cardnumTXT = getArguments().getString("cardnum");
        this.cardmonthTXT = getArguments().getString("cardmonth");
        this.cardyearTXT = getArguments().getString("cardyear");
        this.cardcvvTXT = getArguments().getString("cardcvv");
        this.cardIDTXT = getArguments().getString("cardid");
        this.cardDescrTXT = getArguments().getString("description");
        this.paySysINT = getArguments().getInt("paysys");
        this.cardColor = getArguments().getInt("cardcolor");
        this.cardBankID = getArguments().getInt("bankid");
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseProcessor = new BaseProcessor(view);
        bindingCView.cardNumView.setText(cardnumTXT);
        bindingCView.cardMonthView.setText(cardmonthTXT);
        bindingCView.cardYearView.setText(cardyearTXT);
        bindingCView.cardCvvView.setText(cardcvvTXT);
        bindingCView.cardDescriptionView.setText(cardDescrTXT);
        bindingCView.cardColor.setColorFilter(cardColor);
        //слушатель для значка копирования
        bindingCView.cardNumCopyIco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copytobuffer(1);
                bindingCView.cardNumCopyIco.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        });
        //слушатель для значка копирования
        bindingCView.cardMonthCopyIco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copytobuffer(2);
                bindingCView.cardMonthCopyIco.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        });
        //слушатель для значка копирования
        bindingCView.cardYearCopyIco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copytobuffer(3);
                bindingCView.cardYearCopyIco.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        });
        //слушатель для значка копирования
        bindingCView.cardCvvCopyIco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copytobuffer(4);
                bindingCView.cardCvvCopyIco.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        });
        //слушатель для кнопки Назад
        bindingCView.backinviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //откатываем фрагмент в стеке
                bindingCView.backinviewButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                getActivity().onBackPressed();
            }
        });
        //Слушатель для кнопки Изменить
        bindingCView.changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardnumTXT = bindingCView.cardNumView.getText().toString();
                cardmonthTXT = bindingCView.cardMonthView.getText().toString();
                cardyearTXT = bindingCView.cardYearView.getText().toString();
                cardcvvTXT = bindingCView.cardCvvView.getText().toString();
                cardDescrTXT = bindingCView.cardDescriptionView.getText().toString();
                paySysINT = detectPaySystem(cardnumTXT);
                FragmentManager fm = getActivity().getFragmentManager();
                //проверка корректности введенных данных перед их загрузкой в БД
                if (cardDataCheck(fm, cardnumTXT, cardmonthTXT, cardyearTXT, cardcvvTXT)){
                    //запускаем метод изменения данных в выбранной карте
                    baseProcessor.baseCardUpdate(cardnumTXT, cardmonthTXT, cardyearTXT,
                            cardcvvTXT, cardDescrTXT, cardIDTXT, String.valueOf(paySysINT), cardColor, cardBankID);
                    bindingCView.changeButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    getActivity().onBackPressed();
                }
            }
        });
        //слушатель круглишка выбора цвета
        bindingCView.cardColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.cardViewCallback(bindingCView, cardView);
            }
        });
        //ниже создаю бандл для слоя с картой, который подгрузится в
        //виде фрагмента внутри нашего фрагмента
        Bundle itemselected = new Bundle();
        itemselected.putString("cardnum", cardNumFormat(cardnumTXT));
        itemselected.putString("cardmonth", cardmonthTXT);
        itemselected.putString("cardyear", cardyearTXT);
        itemselected.putString("cardcvv", cardcvvTXT);
        itemselected.putString("description", cardDescrTXT);
        itemselected.putInt("paysys", paySysINT);
        itemselected.putInt("cardcolor", cardColor);
        itemselected.putInt("bankid", cardBankID);
        //ниже передаем данные карты в слой, отображающий карту в нашем фрагменте
        //т.е. фрагмент со слоем карты находится внутри нашего фрагмента
        //и после передачи загружаем фрагмент со слоем карты
        Cardlayout cardlayout = new Cardlayout();
        cardlayout.setArguments(itemselected);
        FragmentTransaction ftr = getActivity().getSupportFragmentManager().beginTransaction();
        ftr.replace(R.id.frame_cardview, cardlayout);
        ftr.commit();
    }

    private void copytobuffer(int i) {
        String textFromSource;
        switch (i) {
            case (1):
                textFromSource = bindingCView.cardNumView.getText().toString();
                toast = Toast.makeText(contextfr, getString(R.string.copy_cardnum_ico), Toast.LENGTH_LONG);
                toast.show();
                break;
            case (2):
                textFromSource = bindingCView.cardMonthView.getText().toString();
                toast = Toast.makeText(contextfr, getString(R.string.copy_cardmonth_ico), Toast.LENGTH_LONG);
                toast.show();
                break;
            case (3):
                textFromSource = bindingCView.cardYearView.getText().toString();
                toast = Toast.makeText(contextfr, getString(R.string.copy_cardyear_ico), Toast.LENGTH_LONG);
                toast.show();
                break;
            case (4):
                textFromSource = bindingCView.cardCvvView.getText().toString();
                toast = Toast.makeText(contextfr, getString(R.string.copy_cardcvv_ico), Toast.LENGTH_LONG);
                toast.show();
                break;
            default:
                textFromSource = "";
        }
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) contextfr.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", textFromSource);
        clipboard.setPrimaryClip(clip);
        clipboard.setPrimaryClip(clip);
    }

    //ниже метод адаптации номера карты для просмотра на экране
    //он разделяет номер карты на 4 секции по 4 цифры
    @Override
    public String cardNumFormat(String cardNum) {
        return MyTextEditMethod.super.cardNumFormat(cardNum);
    }

    //ниже метод проверки корректности введенных данных
    @Override
    public Boolean cardDataCheck(FragmentManager fm, String cardNum, String cardMonth, String cardYear, String cardCVV) {
        return MyTextEditMethod.super.cardDataCheck(fm, cardNum, cardMonth, cardYear, cardCVV);
    }
    //определение платежной системы
    @Override
    public int detectPaySystem(String cardNum) {
        return MyTextEditMethod.super.detectPaySystem(cardNum);
    }
    //определение логотипа платежной системы
    @Override
    public int imageRes(int paysysID) {
        return MyTextEditMethod.super.imageRes(paysysID);
    }
}