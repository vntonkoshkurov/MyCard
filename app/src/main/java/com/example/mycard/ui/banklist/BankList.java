package com.example.mycard.ui.banklist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mycard.BankBaseProcessor;
import com.example.mycard.DatabaseHelper;
import com.example.mycard.adapter.BankBaseAdapter;
import com.example.mycard.databinding.FragmentBankListBinding;
import com.example.mycard.dialogs.SelectBankDialog;

public class BankList extends Fragment implements  SelectBankDialog.Callback {
    private FragmentBankListBinding bindingBlist;
    private DatabaseHelper databaseHelper;
    private Cursor cursor;
    private SQLiteDatabase database;
    private BankBaseAdapter adapter;
    private RecyclerView recyclerView;
    private Bundle bundle;
    private BankBaseProcessor baseProcessor;
    public Callback callback;
    private View view;
    private View root;
    private static final int SETTINGS_CLICKED = 1;
    private static final int BANK_CARD_CLICKED = 2;

    //обрабатываем колбэк из диалогового окна. открытие фрагмента редактирования записи банка
    @Override
    public void dialogCallViewBank(Bundle bundle) {
        callback.callViewBank(bundle);
    }

    //обрабатываем колбэк из диалогового окна. обновление адаптера после удаления записи
    @Override
    public void dialogCloseAfterDelBV() {
        //если было удаление записи из БД, то необходимо ее заново инициализировать
        dbInit(root);
        //и передать ее в адаптер для обновления списка
        //о использовании аргумента true, false читай в адаптере
        adapter.updateData(cursor, true);
    }
    //обрабатываем колбэк из диалогового окна. открытие фрагмента выбора карт для банка
    @Override
    public void dialogCallBankCardSelect(Bundle bundle) {
        callback.callCardSelect(bundle);
    }

    //описываем собственные колбэки класса
    public interface Callback {
        void banklistCallbackBtnAdd();
        void banklistCallbackViewBankCard(Bundle bundle);
        void callViewBank(Bundle bundle);
        void callCardSelect(Bundle bundle);
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.callback = (Callback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bundle = new Bundle();
        bindingBlist = FragmentBankListBinding.inflate(inflater, container, false);
        root = bindingBlist.getRoot();
        recyclerView = bindingBlist.bankView;
        //слушатель выбранного элемента в списке банков
        BankBaseAdapter.OnStateClickListener stateClickListener = new BankBaseAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(int i, int type) {
                int bankID = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                int bankName = cursor.getColumnIndex(DatabaseHelper.COLUMN_BANK_NAME);
                int bankColor = cursor.getColumnIndex(DatabaseHelper.COLUMN_BANK_COLOR);
                cursor.moveToFirst();
                //ниже в цикле ищется нужная карта по ее ID в БД в списке перебором всех карт в курсоре
                while (Integer.parseInt(cursor.getString(bankID)) != i) {
                    cursor.moveToNext();
                }
                bundle.putString("bankid", Integer.toString(cursor.getInt(bankID)));
                bundle.putString("bankname", cursor.getString(bankName));
                bundle.putInt("bankcolor", cursor.getInt(bankColor));
                //в зависимости от нажатой области будут открыты разные фрагменты (просмотр и настройки карточки)
                switch (type) {
                    case SETTINGS_CLICKED:
                        showDialog(bundle);
                        break;
                    case BANK_CARD_CLICKED:
                        callback.banklistCallbackViewBankCard(bundle);
                    break;
                }
            }
        };
        dbInit(root);
        adapter = new BankBaseAdapter(cursor, stateClickListener);
        recyclerView.setAdapter(adapter);
        //ниже реализуется возможность перетаскивать элементы списка банков
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();
                //обновляем порядок записей в БД
                baseProcessor.baseItemPos(position_target, position_dragged);
                //передаем новый порядок записей в адаптер
                adapter.notifyItemMoved(position_dragged, position_target);
                //заново инициализируем БД
                dbInit(root);
                //и передаем курсор в адаптер, чтобы было корректное отображение записей в списке
                adapter.updateData(cursor, false);
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }
        });
        helper.attachToRecyclerView(recyclerView);
        return root;
    }
    //инициализация базы данных и курсора
    private void dbInit (View root) {
        if (database != null) {database.close();}
        baseProcessor = new BankBaseProcessor(root);
        databaseHelper = new DatabaseHelper(root.getContext());
        database = databaseHelper.getWritableDatabase();
        cursor = database.query(DatabaseHelper.TABLE_BANKS, null, null,
                null, null, null, DatabaseHelper.COLUMN_ITEMINLIST);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        bindingBlist.bankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.banklistCallbackBtnAdd();
                bindingBlist.bankButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        });
    }
    //инициализация диалогового окна. Передаем в объект необходимые параметры
    //а так же этот фрагмент для того, чтобы окно смогло вызвать здесь свой колбэк
    public void showDialog(Bundle bundle) {
        SelectBankDialog dialog = new SelectBankDialog(database, bundle, this, view);
        dialog.setArguments(bundle);
        dialog.show(getChildFragmentManager(), "bankSelected");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        database.close();
    }
}