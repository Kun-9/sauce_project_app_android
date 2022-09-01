package kr.ac.daejin.sauceApp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.StringTokenizer;

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

        //리스트 뷰 클릭시 동작
        sourceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int select, long id) {
                position = select;

                Intent intent = new Intent(getActivity(), customOutput.class); startActivity(intent);

                String str = comp_list.get(position);

                StringTokenizer st = new StringTokenizer(str, " ");
                String name = st.nextToken();
                String comp = st.nextToken();
            }
        });


        return rootView;
    }




}