package com.example.mycard.ui.carddistrib;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.mycard.BaseProcessor;
import com.example.mycard.DatabaseHelper;
import com.example.mycard.R;
import com.example.mycard.adapter.CardSelectBaseAdapter;
import com.example.mycard.databinding.FragmentCardDistribBinding;
import java.util.ArrayList;
import java.util.function.Consumer;

public class CardDistrib extends Fragment implements CardSelectBaseAdapter.CallBack{
    private FragmentCardDistribBinding binding;
    private DatabaseHelper databaseHelper;
    private Cursor cursor;
    private SQLiteDatabase database;
    private RecyclerView recyclerView;
    private ArrayList <String> selectList = new ArrayList<>();
    private BaseProcessor baseProcessor;
    private Toast toast;
    private Context contextfr;

    //немного о логике работы данного фрагмента
    //в фрагменте имеется reciclerview. В нем реализован мультивыбор позиций. при каждом нажатии
    //reciclerview возвращает массив выделенных элементов.
    //далее при команде добавления идендификатора банка в запись карты перебирается данный массив для
    //выявления карт, которые были выбраны

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.contextfr = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentCardDistribBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        databaseHelper = new DatabaseHelper(root.getContext());
        database = databaseHelper.getWritableDatabase();
        cursor = database.query(DatabaseHelper.TABLE, null, null, null, null, null, DatabaseHelper.COLUMN_ITEMINLIST);
        recyclerView = binding.cardListSelect;
        CardSelectBaseAdapter
        adapter = new CardSelectBaseAdapter(cursor, this);
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseProcessor = new BaseProcessor(view);
        //слушатель кнопки добавления
        binding.bankButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectList.isEmpty()) {
                    toast = Toast.makeText(contextfr, getString(R.string.card_select_msg), Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        selectList.forEach(new Consumer<String>() {
                            @Override
                            public void accept(String s) {
                                baseProcessor.baseBankIdChange(s, Integer.parseInt(getArguments().getString("bankid")));
                            }
                        });
                    }
                    toast = Toast.makeText(contextfr, getString(R.string.card_selcted_msg), Toast.LENGTH_LONG);
                    toast.show();
                    binding.bankButtonAdd.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    getActivity().onBackPressed();
                }
            }
        });
        //слушатель кнопки возврата
        binding.bankButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.bankButtonBack.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void selectedList(ArrayList<String> selectList) {
        this.selectList =(ArrayList<String>) selectList.clone();
    }
}