package ua.kh.tremtyachiy.mylist.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.kh.tremtyachiy.mylist.R;

/**
 * Created by User on 22.06.2015.
 */
public class FragmentAdd extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentadd, container, false);
        return view;
    }
}
