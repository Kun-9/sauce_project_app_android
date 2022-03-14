package kr.ac.daejin.sourceApp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class registSource extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.source_regist_page, container, false);
        ArrayList<String> LIST_MENU = ((MainActivity) MainActivity.context_main).LIST_MENU;

        EditText sourceName = (EditText) rootView.findViewById(R.id.sourceName);
        CheckBox isLiquidCheck = (CheckBox) rootView.findViewById(R.id.isLiquidCheck);
//        FragmentTransaction ft = getFragmentManager().beginTransaction();

        source_class.SourceList sourceList = ((MainActivity) MainActivity.context_main).sourceList;
        Button registBtn = (Button) rootView.findViewById(R.id.registBtn);


        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = sourceName.getText().toString();
                int isLiquid = 0;
                if (name.equals("")) {
                    Toast.makeText( getActivity().getApplicationContext(), "소스를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
//                    if(isLiquidCheck.isChecked()) isLiquid = 1;
                    OnClickHandler(view, "소스를 등록하시겠습니까?", sourceList, sourceName, isLiquidCheck, LIST_MENU);
                }
            }
        });

        return rootView;
    }


    // 소스 등록 확인창
    public void OnClickHandler(View view, String msg, source_class.SourceList sourceList, EditText name, CheckBox checkBox, ArrayList<String> arr)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("알림").setMessage(msg);

        builder.setPositiveButton("등록", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                String liquidStr = "가루형";
                int liquidInt = 0;
                if (checkBox.isChecked()) {
                    liquidStr = "액체형";
                    liquidInt = 1;
                }
                sourceList.addSource(name.getText().toString(), liquidInt);
                arr.add(name.getText().toString() + ", " + liquidStr);
                name.setText(null);
                checkBox.setChecked(false);

                // 키보드 내리기
                InputMethodManager mInputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(name.getWindowToken(), 0);

                // 토스트
                Toast.makeText( getActivity().getApplicationContext(), "등록 성공", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {

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
}