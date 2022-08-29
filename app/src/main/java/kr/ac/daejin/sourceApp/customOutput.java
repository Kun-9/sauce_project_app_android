package kr.ac.daejin.sourceApp;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class customOutput extends AppCompatActivity {

    Dialog customdialog;
    TextView dialog_msg;
    EditText[] cart;
    TextView[] cartName;
    source_class.SourceList sourceList;
    SauceListManager.SauceList sauceList;
    ArrayList<String> comp_list;

    OutputStream outputStream;
    int checkConnecting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_output_page);

        customdialog = new Dialog(this);
        customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customdialog.setContentView(R.layout.custom_dialog);
        dialog_msg = customdialog.findViewById(R.id.dialog_msg);


        comp_list = ((MainActivity) MainActivity.context_main).comp_list;
        checkConnecting = ((MainActivity) MainActivity.context_main).checkConnecting;
        sourceList = ((MainActivity) MainActivity.context_main).sourceList;
        sauceList = ((MainActivity) MainActivity.context_main).sauceList;
        outputStream = ((MainActivity) MainActivity.context_main).outputStream;

        cart = new EditText[6];
        Integer[] cartId = {
                R.id.cart1, R.id.cart2, R.id.cart3, R.id.cart4, R.id.cart5, R.id.cart6
        };

        cartName = new TextView[6];
        Integer[] cartNameId = {
                R.id.cart1Name, R.id.cart2Name, R.id.cart3Name, R.id.cart4Name, R.id.cart5Name, R.id.cart6Name
        };


        for (int i = 0; i < 6; i++) {
            cart[i] = findViewById(cartId[i]);
            cartName[i] = findViewById(cartNameId[i]);
        }

        Button customOutputSendBtn, saveSourceCompBtn;
        customOutputSendBtn = (Button) findViewById(R.id.customOutputSendBtn);
        saveSourceCompBtn = (Button) findViewById(R.id.saveSourceCompBtn);

        for (int i = 0; i < 6; i++) {
            if (sauceList.getSauceList().get(i).getId().equals("null")) {
                cartName[i].setText("X");
            } else {
                cartName[i].setText(sauceList.getSauceList().get(i).getName());
//                cartName[i].setText("exist");
            }
        }

//        for (int i = 0; i < 6; i++) {
//            String name = sauceList.getSauceList().get(0).getName();
//            cartName[i].setText(name);
//
//        }


        customOutputSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_msg.setText("소스를 출력합니다.");
                showDialog();
            }
        });

        // 조합 저장 버튼 클릭
        saveSourceCompBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registCompDialog();
            }
        });



    }

    public void showDialog() {
//        dialog_msg.setText("소스를 출력 하시겠습니까?");
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
                String sendStr = getOutputString(cart, sourceList);

                if (checkConnecting == 1) {
                    // 새로운 쓰레드 생성
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            byte[] buffer = new byte[1024];
                            try {
                                // 입력값 전송
                                buffer = sendStr.getBytes(StandardCharsets.UTF_8);
                                outputStream.write(buffer);
                                outputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                customdialog.dismiss(); // 다이얼로그 닫기
                            }
                        }
                    }).start();
                    Toast.makeText(getApplicationContext(), "전송 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "기기와 연결중이 아닙니다.", Toast.LENGTH_SHORT).show();
                }

                customdialog.dismiss(); // 다이얼로그 닫기
            }
        });
    }

    @NonNull
    private String getOutputString(EditText[] cart, source_class.SourceList sourceList) {
        int num = 0;
        StringBuilder cartNum = new StringBuilder();
        StringBuilder weight = new StringBuilder();
        StringBuilder isLiquid = new StringBuilder();
        StringBuilder sendStr = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            SauceListManager.Sauce sauce = sauceList.getSauceList().get(i);
            if (!sauce.getId().equals("null")) {
                // 카트리지 번호
                cartNum.append(i + 1).append(" ");

                // 출력량
                weight.append(cart[i].getText().toString()).append(" ");

                // 액체 구분
                isLiquid.append(sauce.getIsLiquid()).append(" ");

                // 출력 소스 갯수
                num++;
            }
        }
        sendStr.append(num).append(",").append(cartNum).append(",").append(weight).append(",").append(isLiquid);
        return sendStr.toString();
    }

    public void registCompDialog() {
        customdialog = new Dialog(this);
        customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customdialog.setContentView(R.layout.custom_dialog_textinput);
        EditText cartNumDialogInput = customdialog.findViewById(R.id.cartNumDialogInput);
        customdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경

        dialog_msg = customdialog.findViewById(R.id.dialog_msg);
        dialog_msg.setText("이름 설정");

        customdialog.show();


        // 아니오 버튼
        Button noBtn = customdialog.findViewById(R.id.noBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customdialog.dismiss(); // 다이얼로그 닫기
            }
        });
        // 네 버튼
        customdialog.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customdialog.dismiss(); // 다이얼로그 닫기

                String name = cartNumDialogInput.getText().toString();

                String sendStr = getOutputString(cart, sourceList);
                String nameInfo = name + " " + sendStr;
                comp_list.add(nameInfo);

                Toast.makeText(getApplicationContext(), "등록 완료", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editSauce(String name, String isLiquid, String id) {
        if (checkConnecting == 1) {
            // 새로운 쓰레드 생성
            new Thread(new Runnable() {
                @Override
                public void run() {

                    byte[] buffer = new byte[1024];
                    try {
                        // 입력값 전송
                        buffer = "7{\"name\" : \"간장\", \"isLiquid\" : \"1\", \"id\" : \"2277814929\"}".getBytes(StandardCharsets.UTF_8);
                        outputStream.write(buffer);
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Toast.makeText(getApplicationContext(), "전송 성공", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "기기와 연결중이 아닙니다.", Toast.LENGTH_SHORT).show();
        }
    }

}
