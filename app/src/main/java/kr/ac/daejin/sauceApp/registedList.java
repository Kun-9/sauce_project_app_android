package kr.ac.daejin.sauceApp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class registedList extends Fragment {

    Dialog customdialog, customdialog_input;
    TextView dialog_msg;
    EditText cartNumDialogInput;
    source_class.SourceList sourceList;
    ArrayAdapter Adapter;
    ArrayList<String> LIST_MENU;
    ListView sourceListView;
    int position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.source_registed_list_page, container, false);

        sourceList = ((MainActivity) MainActivity.context_main).sourceList;
        Adapter = ((MainActivity) MainActivity.context_main).Adapter;
        LIST_MENU = ((MainActivity) MainActivity.context_main).LIST_MENU;



        sourceListView = (ListView) rootView.findViewById(R.id.registedSourceList);
        sourceListView.setAdapter(Adapter);


        //리스트 뷰 클릭시 동작하도록
        sourceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int select, long id) {
                position = select;
                selectOptionDialog();
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

    public void selectOptionDialog() {
        //다이얼로그 설정
        customdialog = new Dialog(getActivity());
        customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customdialog.setContentView(R.layout.custom_dialog);
        dialog_msg = customdialog.findViewById(R.id.dialog_msg);

        ViewGroup.LayoutParams param = dialog_msg.getLayoutParams();
        param.width = 900;
        dialog_msg.setLayoutParams(param);

        dialog_msg.setText("동작 선택");
        Button noBtn = customdialog.findViewById(R.id.noBtn);
        Button yesBtn = customdialog.findViewById(R.id.yesBtn);
        noBtn.setText("카트리지 등록");
        yesBtn.setText("삭제");

        customdialog.show();
        customdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경


        // 카트리지 등록버튼
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                customdialog.dismiss(); // 다이얼로그 닫기
                cartridgeInput();
            }
        });
        // 삭제 버튼
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현

                LIST_MENU.remove(position);
                sourceList.getSourceList().remove(position);
                sourceListView.setAdapter(Adapter);

                // 토스트
                Toast.makeText( getActivity().getApplicationContext(), "삭제 완료", Toast.LENGTH_SHORT).show();

                customdialog.dismiss(); // 다이얼로그 닫기
            }
        });
    }

    public void cartridgeInput() {
        customdialog = new Dialog(getActivity());
        customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customdialog.setContentView(R.layout.custom_dialog_textinput);
        cartNumDialogInput = customdialog.findViewById(R.id.cartNumDialogInput);

        customdialog.show();
        customdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경


        // 아니오 버튼
        Button noBtn = customdialog.findViewById(R.id.noBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현

                customdialog.dismiss(); // 다이얼로그 닫기
            }
        });
        // 네 버튼
        customdialog.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                customdialog.dismiss(); // 다이얼로그 닫기

                int num = Integer.parseInt(cartNumDialogInput.getText().toString()) - 1;

                // 이미 등록된 소스가 있을 때 (중복 검사)
                if (sourceList.getCurrentSourceExist()[num] == 1) {
                    // 키보드 내리기
                    InputMethodManager mInputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    mInputMethodManager.hideSoftInputFromWindow(cartNumDialogInput.getWindowToken(), 0);

                    // 다음 다이얼로그로 이동
                    duplicatetionDialog(num);

                } else {
                    // 등록된 소스가 없을 때
                    sourceList.regist_current_source(num, position);
                    String str = num + 1 + "번 카트리지에 등록되었습니다.";
                    Toast.makeText(getActivity().getApplicationContext(), str, Toast.LENGTH_SHORT).show();

                    // 키보드 내리기
                    InputMethodManager mInputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    mInputMethodManager.hideSoftInputFromWindow(cartNumDialogInput.getWindowToken(), 0);

                    // 토스트
                    Toast.makeText( getActivity().getApplicationContext(), "등록 완료", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void duplicatetionDialog(int num) {
        customdialog = new Dialog(getActivity());
        customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customdialog.setContentView(R.layout.custom_dialog);
        dialog_msg = customdialog.findViewById(R.id.dialog_msg);
        dialog_msg.setText("등록된 소스를 삭제하고 진행하시겠습니까?");
        customdialog.show();
        customdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경


        // 아니오 버튼
        Button noBtn = customdialog.findViewById(R.id.noBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                customdialog.dismiss(); // 다이얼로그 닫기
            }
        });
        // 네 버튼
        customdialog.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                sourceList.regist_current_source(num, position);
                String str = num + 1 + "번 카트리지에 등록되었습니다.";
                Toast.makeText(getActivity().getApplicationContext(), str, Toast.LENGTH_SHORT).show();

                // 토스트
                Toast.makeText( getActivity().getApplicationContext(), "등록 완료", Toast.LENGTH_SHORT).show();

                customdialog.dismiss(); // 다이얼로그 닫기
            }
        });
    }


}