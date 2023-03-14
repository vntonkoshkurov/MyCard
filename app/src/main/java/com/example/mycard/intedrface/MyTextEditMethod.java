package com.example.mycard.intedrface;

import android.app.FragmentManager;
import android.os.Bundle;
import com.example.mycard.R;
import com.example.mycard.dialogs.CheckCardDataDialog;

//метод добавляет пробелы через 4 символа в номер карты для ее лучшей читабельности
public interface MyTextEditMethod {

    //метод ставит пробелы в номер карты через каждые 4 символа для читаемости номера карты
    public default String cardNumFormat(String cardNum){
        StringBuffer strBuffer = new StringBuffer(cardNum);
        int pos = 1;
        for (int i = 0; i < strBuffer.length(); i++){
            if (pos ==5){
                strBuffer.insert(i, " ");
                pos = 0;
            }
            pos++;
        }
        return strBuffer.toString();
    };

    //метод возвращает последние 4 символа карты
    public default String cardNumShort (String cardNum){
        StringBuffer strBuffer = new StringBuffer(cardNum);
        strBuffer.reverse();
        strBuffer.delete(4,strBuffer.length());
        return strBuffer.reverse().toString();
    };

    //метод проверяет корректность введенных данных по карте
    public default Boolean cardDataCheck(FragmentManager ft, String cardNum, String cardMonth,
                                             String cardYear, String cardCVV) {
        CheckCardDataDialog checkCardDataDialog = new CheckCardDataDialog();
        Bundle nameOfBadString;
        nameOfBadString = new Bundle();
        Boolean result = true;
        if (!(cardNum.length() >= 12 && cardNum.length() <= 19)) {
            nameOfBadString.putInt("name", R.string.check_card_num_result);
            checkCardDataDialog.setArguments(nameOfBadString);
            checkCardDataDialog.show(ft, "custom" );
            result = false;
            return result;
        }
        if (cardMonth.length() != 2) {
            nameOfBadString.putInt("name", R.string.check_card_month1_result);
            checkCardDataDialog.setArguments(nameOfBadString);
            checkCardDataDialog.show(ft, "custom" );
            result = false;
            return result;
        }
        if (Integer.parseInt(cardMonth) > 12) {
            nameOfBadString.putInt("name", R.string.check_card_month2_result);
            checkCardDataDialog.setArguments(nameOfBadString);
            checkCardDataDialog.show(ft, "custom" );
            result = false;
            return result;
        }
        if (cardYear.length() != 2) {
            nameOfBadString.putInt("name", R.string.check_card_year_result);
            checkCardDataDialog.setArguments(nameOfBadString);
            checkCardDataDialog.show(ft, "custom" );
            result = false;
            return result;
        }
        if (cardCVV.length() != 3) {
            nameOfBadString.putInt("name", R.string.check_card_cvv_result);
            checkCardDataDialog.setArguments(nameOfBadString);
            checkCardDataDialog.show(ft, "custom" );
            result = false;
            return result;
        }
        return result;
    };

    //метод определяет платежную систему по номеру карты
    public default int detectPaySystem(String cardNum){

            int val;
            int payType = 8;

            if (cardNum.length()!=0){
               val = Integer.parseInt(cardNum.substring(0,2));
            } else{
                return payType;
            }
            if (val >=20 & val <30){
                payType = 1;
            }
            if (val >= 40 & val < 50){
                payType = 2;
            }
            if (val >= 51 & val <= 55){
                payType = 3;
            }
            if (val == 34 || val == 37){
                payType = 4;
            }
            if (val == 50 || val == 56 || val == 57 || val == 58 || val == 63
                    || val == 67){
                payType = 5;
            }
            if (val == 62){
                payType = 6;
            }
            if (val == 31 || val ==35){
                payType = 7;
            }
            return payType;
        };

    //метод определяет ресурс логотипа для платежной системы
    public default int imageRes(int paysysID) {

        int paysys [] = {R.drawable.mir, R.drawable.visa,
                R.drawable.mastercard, R.drawable.amexpress,
                R.drawable.maestro, R.drawable.unionpay, R.drawable.jcb, R.drawable.unknown};

        int payType = 0;

        if (paysysID == 1) {
            payType = paysys[0]; //mir system
        }
        if (paysysID == 2){
            payType = paysys[1]; //visa system
        }
        if (paysysID == 3){
            payType = paysys[2]; //mastercard system
        }
        if (paysysID == 4){
            payType = paysys[3]; //americanexpress system
        }
        if (paysysID == 5){
            payType = paysys[4]; //maestro system
        }
        if (paysysID == 6){
            payType = paysys[5]; //unionpay system
        }
        if (paysysID == 7){
            payType = paysys[6]; //jcb system
        }

        if (paysysID == 8){
            payType = paysys[7]; //jcb system
        }

        return payType;
    };

}