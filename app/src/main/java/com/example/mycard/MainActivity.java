package com.example.mycard;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import com.example.mycard.databinding.FragmentBankAddFormBinding;
import com.example.mycard.databinding.FragmentBankViewBinding;
import com.example.mycard.databinding.FragmentCardAddFormBinding;
import com.example.mycard.databinding.FragmentCardViewBinding;
import com.example.mycard.ui.bankadd.BankAddForm;
import com.example.mycard.ui.banklist.BankList;
import com.example.mycard.ui.bankview.BankView;
import com.example.mycard.ui.cardadd.CardAddForm;
import com.example.mycard.ui.cardlist.CardList;
import com.example.mycard.ui.cardview.CardView;
import com.example.mycard.ui.settings.AppSettings;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mycard.databinding.ActivityMainBinding;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

public class MainActivity extends AppCompatActivity implements CardList.Callback, BankList.Callback, CardAddForm.Callback,
        CardView.Callback, BankView.Callback, BankAddForm.Callback, AppSettings.Callback, ColorPickerDialogListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavController navController;
    private SharedPreferences settings;
    private FragmentCardAddFormBinding bindingCAdd;
    private FragmentBankAddFormBinding bindingBAdd;
    private FragmentCardViewBinding bindingCView;
    private FragmentBankViewBinding bindingBView;
    private CardView cardView;
    private CardAddForm cardAddForm;
    private BankAddForm bankAddForm;
    private BankView bankView;
    private static final String DARK_MODE = "DarkMode";
    private static final String PREFS_FILE = "Settings";
    private static final String INTRODUCTION = "Introduction";
    private static final String COLOR = "color";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        if (settings.getBoolean(DARK_MODE,false)){
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
        }
        //если приложение открывается впервые после установки, запускается активность с ознакомлением о программе
        if (!settings.getBoolean(INTRODUCTION,false)){
            Intent intent = new Intent(MainActivity.this, Introduction.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        //ниже конфиграция бокового меню. при добавлении новых пунктов в меню их нужно прописать
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.cardlist, R.id.banklist, R.id.settings, R.id.confDescription, R.id.aboutApp)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //реализованные методы из класса обработки выбора цвета в диалоговом окне КолорСелектора
    @Override
    public void onColorSelected(int dialogId, int color) {
        switch (dialogId){
            case 1:
                bindingCAdd.ColorCircle.setColorFilter(color);
                cardAddForm.setCardColor(color);
                break;
            case 2:
                bindingCView.cardColor.setColorFilter(color);
                cardView.setCardColor(color);
                break;
            case 3:
                bindingBAdd.ColorCircle.setColorFilter(color);
                bankAddForm.setBankColor(color);
                break;
            case 4:
                bindingBView.bankColor.setColorFilter(color);
                bankView.setColor(color);
                break;
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {    }

    //метод создающий диалоговое окно выбора цвета
    private void createColorPickerDialog(int id) {
        ColorPickerDialog.newBuilder()
                .setColor(Color.RED)
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setDialogTitle(R.string.color_select_dialog)
                .setSelectedButtonText(R.string.color_select_sel_button)
                .setPresetsButtonText(R.string.color_select_cst_button)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .setColorShape(ColorShape.CIRCLE)
                .setDialogId(id)
                .show(this);
    }

    //обработка нажатия на иконку выбора цвета из фрагмента добавления карты
    @Override
    public void cardAddCallBack(FragmentCardAddFormBinding bindingCAdd, CardAddForm cardAddForm) {
        createColorPickerDialog(1);
        this.bindingCAdd =  bindingCAdd;
        this.cardAddForm = cardAddForm;
    }

    //обработка нажатия на иконку выбора цвета из фрагмента добавления банка
    @Override
    public void bankAddCallBack(FragmentBankAddFormBinding bindingBAdd, BankAddForm bankAddForm) {
        createColorPickerDialog(3);
        this.bindingBAdd =  bindingBAdd;
        this.bankAddForm = bankAddForm;
    }

    //обработка нажатия на иконку выбора цвета из фрагмента редактирования карты
    @Override
    public void cardViewCallback(FragmentCardViewBinding bindingCView, CardView cardView) {
        createColorPickerDialog(2);
        this.bindingCView = bindingCView;
        this.cardView = cardView;
    }

    //обработка нажатия на иконку выбора цвета из фрагмента редактирования карточки банка
    @Override
    public void bankViewCallback(FragmentBankViewBinding bindingBView, BankView bankView) {
        createColorPickerDialog(4);
        this.bindingBView = bindingBView;
        this.bankView = bankView;
    }

    //обработка нажатия на кнопку добавления новой карты
    @Override
    public void cardlistCallbackBtnAdd(Bundle bundle) {
        //navController.navigate(R.id.cardadd, bundle);
        navController.navigate(R.id.action_cardlist_to_cardadd, bundle);
    }

    //обработка нажатия на карту. Появляется диалоговое окно, которое вызывает данный
    // callback на лист редактирования
    @Override
    public void callViewCard(Bundle bundle) {
        //navController.navigate(R.id.cardview, bundle);
        navController.navigate(R.id.action_cardlist_to_cardview, bundle);
    }

    //обработка нажатия на карточку банка. появляется окно редактирование записи банка
    @Override
    public void callViewBank(Bundle bundle) {
        //navController.navigate(R.id.bankview, bundle);
        navController.navigate(R.id.action_banklist_to_bankview, bundle);
    }

    //обработка нажатия на карточку банка. появляются карточки банка
    @Override
    public void banklistCallbackViewBankCard(Bundle bundle) {
        //navController.navigate(R.id.bankCardView, bundle);
        navController.navigate(R.id.action_banklist_to_bankCardView, bundle);
    }
    //обработка выбора карт для банка
    @Override
    public void callCardSelect(Bundle bundle) {
        //navController.navigate(R.id.cardDistrib, bundle);
        navController.navigate(R.id.action_banklist_to_cardDistrib, bundle);
    }

    @Override
    public void appSettingsCallbackPinSet() {
        navController.navigate(R.id.setPin);
        //navController.navigate(R.id.action_settings_to_setPin);
    }

    @Override
    public void banklistCallbackBtnAdd() {
        navController.navigate(R.id.bankadd);
        //navController.navigate(R.id.action_banklist_to_bankadd);
    }

    //запуск активности с ознакомительным текстом при первичной установке программыы
    public static class IntroductionActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_introduction);
        }
    }
}