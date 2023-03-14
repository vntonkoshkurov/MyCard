package com.example.mycard.ui.ConfDescription;

import static android.text.Html.FROM_HTML_MODE_LEGACY;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mycard.R;
import com.example.mycard.databinding.FragmentConfDescriptionBinding;

public class ConfDescription extends Fragment {

    FragmentConfDescriptionBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentConfDescriptionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.confDescrtxt.setText(Html.fromHtml(getString(R.string.confidencial_description), FROM_HTML_MODE_LEGACY));
        binding.confDescrtxt.setMovementMethod(LinkMovementMethod.getInstance());
        return root;
    }
}