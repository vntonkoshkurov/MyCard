package com.example.mycard.ui.cardlist;

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
import com.example.mycard.BaseProcessor;
import com.example.mycard.DatabaseHelper;
import com.example.mycard.adapter.CardBaseAdapter;
import com.example.mycard.databinding.FragmentCardListBinding;
import com.example.mycard.dialogs.SelectCardDialog;
import com.example.mycard.intedrface.MyTextEditMethod;

public class CardList extends Fragment implements MyTextEditMethod, SelectCardDialog.Callback {

    private FragmentCardListBinding bindingCList;
    private DatabaseHelper databaseHelper;
    private Cursor cursor;
    private SQLiteDatabase database;
    private CardBaseAdapter adapter;
    private RecyclerView recyclerView;
    private Bundle bundle;
    private Bundle getArguments;
    public Callback callBack;
    private BaseProcessor baseProcessor;
    private View view;
    private View root;

    //описываем собственные колбэки класса
    public interface Callback {
        void cardlistCallbackBtnAdd(Bundle bundle);
        void callViewCard(Bundle bundle);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.callBack = (Callback) context;
    }

    //обрабатываем колбэк из диалогового окна
    @Override
    public void dialogCallViewCard(Bundle bundle) {
        callBack.callViewCard(bundle);
    }

    //обрабатываем колбэк из диалогового окна
    @Override
    public void dialogCloseAfterDelCV() {
        //если было удаление записи из БД, то необходимо ее заново инициализировать
        dbInit(root);
        //и передать ее в адаптер для обновления списка
        //о использовании аргумента true, false читай в адаптере
        adapter.updateData(cursor, true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        bindingCList = FragmentCardListBinding.inflate(inflater, container, false);
        root = bindingCList.getRoot();
        CardBaseAdapter.OnStateClickListener stateClickListener  = null;
        getArguments = getArguments();
        bundle = new Bundle();
        recyclerView = bindingCList.cardView;
        //слушатель выбранного элемента в списке карт
        stateClickListener  = new CardBaseAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(int i) {
                int cardNumID = cursor.getColumnIndex(DatabaseHelper.COLUMN_CARDNUM);
                int cardMonthID = cursor.getColumnIndex(DatabaseHelper.COLUMN_CARDMONTH);
                int cardYearID = cursor.getColumnIndex(DatabaseHelper.COLUMN_CARDYEAR);
                int cardCVVID = cursor.getColumnIndex(DatabaseHelper.COLUMN_CARDCVV);
                int cardIDID = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                int paysysID = cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYSYS);
                int cardDescrID = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION);
                int cardColorID = cursor.getColumnIndex(DatabaseHelper.COLUMN_CARD_COLOR);
                int cardBankID = cursor.getColumnIndex(DatabaseHelper.COLUMN_BANK_ID);
                cursor.moveToFirst();
                //ниже в цикле ищется нужная карта по ее ID в БД в списке перебором всех карт в курсоре
                while (Integer.parseInt(cursor.getString(cardIDID)) != i) {
                    cursor.moveToNext();
                }
                bundle.putString("itemfordel", String.valueOf(i));
                bundle.putString("cardnum", cursor.getString(cardNumID));
                bundle.putString("cardmonth", cursor.getString(cardMonthID));
                bundle.putString("cardyear", cursor.getString(cardYearID));
                bundle.putString("cardcvv", cursor.getString(cardCVVID));
                bundle.putString("cardid", cursor.getString(cardIDID));
                bundle.putString("description", cursor.getString(cardDescrID));
                bundle.putInt("paysys", imageRes(cursor.getInt(paysysID)));
                bundle.putInt("cardcolor", cursor.getInt(cardColorID));
                if (getArguments != null) {
                    bundle.putInt("bankid", getArguments().getInt("bankid"));
                } else {
                    bundle.putInt("bankid", cursor.getInt(cardBankID));
                }
                showDialog(bundle);
            }
        };
        dbInit(root);
        adapter = new CardBaseAdapter(cursor, stateClickListener);
        recyclerView.setAdapter(adapter);
        //ниже реализуется возможность перетаскивать элементы списка карт.
        //перетаскиваение возможно только при прямом открытии данного фрагмента.
        //при открытии фрагмента из под карточки банка перетаскивание карточек не возможно.
        if (getArguments == null) {
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
        }
        return root;
    }
    //инициализация базы данных и курсора
    private void dbInit (View root) {
        if (database != null) {database.close();}
        baseProcessor = new BaseProcessor(root);
        databaseHelper = new DatabaseHelper(root.getContext());
        database = databaseHelper.getWritableDatabase();
        //выборка из БД в зависимости от того какой фгармент вызывает метод
        //выбираются сортируются либо все карты, либо карты конкретного банка
        if (getArguments != null) {
            cursor = database.query(DatabaseHelper.TABLE, null, DatabaseHelper.COLUMN_BANK_ID + "= ?",
                    new  String[] {Integer.toString(getArguments.getInt("bankid"))},
                    null, null, DatabaseHelper.COLUMN_ITEMINLIST);
        } else {
            cursor = database.query(DatabaseHelper.TABLE, null, null, null, null,
                    null, DatabaseHelper.COLUMN_ITEMINLIST);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        bindingCList.cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments != null) {
                    callBack.cardlistCallbackBtnAdd(getArguments);
                } else {
                    callBack.cardlistCallbackBtnAdd(null);
                }
                bindingCList.cardButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        database.close();
    }

    public void showDialog(Bundle bundle) {
        //инициализация диалогового окна. Передаем в объект необходимые параметры
        //а так же этот фрагмент для того, чтобы окно смогло вызвать здесь свой колбэк
        SelectCardDialog dialog = new SelectCardDialog(database, bundle, this, getView());
        dialog.setArguments(bundle);
        dialog.show(getChildFragmentManager(), "cardSelected");
    }

    @Override
    public int imageRes(int paysysID) {
        return MyTextEditMethod.super.imageRes(paysysID);
    }
}