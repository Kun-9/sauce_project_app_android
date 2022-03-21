package kr.ac.daejin.sourceApp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class source_comp_list extends Fragment {

    Dialog customdialog, customdialog_input;
    TextView dialog_msg;
    EditText cartNumDialogInput;
    source_class.SourceList sourceList;
    ArrayAdapter Adapter;
    ArrayList<String> comp_list;
    ListView sourceListView;
    int position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.source_comp_list, container, false);

        sourceList = ((MainActivity) MainActivity.context_main).sourceList;
        comp_list = ((MainActivity) MainActivity.context_main).comp_list;
        Adapter = ((MainActivity) MainActivity.context_main).Adapter2;

        sourceListView = (ListView) rootView.findViewById(R.id.comp_listView);
        sourceListView.setAdapter(Adapter);




        return rootView;
    }
}