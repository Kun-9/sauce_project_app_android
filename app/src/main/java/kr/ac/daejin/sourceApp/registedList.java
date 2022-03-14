package kr.ac.daejin.sourceApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class registedList extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.source_registed_list_page, container, false);

        source_class.SourceList sourceList = ((MainActivity) MainActivity.context_main).sourceList;
        ArrayAdapter Adapter = ((MainActivity) MainActivity.context_main).Adapter;
        ArrayList<String> LIST_MENU = ((MainActivity) MainActivity.context_main).LIST_MENU;
/*
        ArrayList<String> LIST_MENU = new ArrayList<String>();



        ArrayAdapter Adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU) ;
*/

        ListView sourceListView;


        sourceListView = (ListView) rootView.findViewById(R.id.registedSourceList);
        sourceListView.setAdapter(Adapter);


        

        //리스트 뷰 클릭시 동작하도록
        sourceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("알림").setMessage("");

                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        LIST_MENU.remove(position);
                        sourceList.getSourceList().remove(position);
                        sourceListView.setAdapter(Adapter);

                        // 토스트
                        Toast.makeText( getActivity().getApplicationContext(), "삭제 성공", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("카트리지에 등록", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // 순서대로 카트리지에 등록
                        for (int i = 0; i < 6; i++) {
                            if (sourceList.getCurrentSourceExist()[i] == 0) {

                                sourceList.regist_current_source(i, position);
                                break;
                            }
                        }

                    }
                });

//        builder.setNeutralButton("Neutral", new DialogInterface.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface dialog, int id)
//            {
//                Toast.makeText( getActivity().getApplicationContext(), "Neutral Click", Toast.LENGTH_SHORT).show();
//            }
//        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                ArrayList<String> str = new ArrayList<>();
//                for (int i = 0; i < sourceList.getSourceList().size(); i++) {
//                    LIST_MENU.add(sourceList.getSourceList().get(i).getInfo());
//                }
//
//                sourceListView.setAdapter(Adapter);

            }
        });


        return rootView;
    }

}