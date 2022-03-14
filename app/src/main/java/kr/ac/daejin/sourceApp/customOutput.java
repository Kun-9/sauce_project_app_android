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


public class customOutput extends AppCompatActivity {

    Dialog customdialog;
    TextView dialog_msg;
    EditText[] cart;
    TextView[] cartName;
    source_class.SourceList sourceList;
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


        checkConnecting = ((MainActivity) MainActivity.context_main).checkConnecting;
        sourceList = ((MainActivity) MainActivity.context_main).sourceList;
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
            if (sourceList.getCurrentSourceExist()[i] != 0) {
                cartName[i].setText(sourceList.getCurrentSourceList()[i].getName());
            } else {
                cartName[i].setText("X");
            }
        }

        customOutputSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_msg.setText("소스를 출력합니다.");
                showDialog();
            }
        });

        saveSourceCompBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void showDialog() {
//        dialog_msg.setText("소스를 출력 하시겠습니까?");
        customdialog.show();
        customdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경
        /* 이 함수 안에 원하는 디자인과 기능을 구현하면 된다. */

        // 위젯 연결 방식은 각자 취향대로~
        // '아래 아니오 버튼'처럼 일반적인 방법대로 연결하면 재사용에 용이하고,
        // '아래 네 버튼'처럼 바로 연결하면 일회성으로 사용하기 편함.
        // *주의할 점: findViewById()를 쓸 때는 -> 앞에 반드시 다이얼로그 이름을 붙여야 한다.

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
            if (cart[i] != null && sourceList.getCurrentSourceExist()[i] == 1) {
                // 카트리지 번호
                cartNum.append(i + 1).append(" ");

                // 출력량
                weight.append(cart[i].getText().toString()).append(" ");

                // 액체 구분
                isLiquid.append(sourceList.getCurrentSourceList()[i].getIsLiquid()).append(" ");

                // 출력 소스 갯수
                num++;
            }
        }
        sendStr.append(num).append(",").append(cartNum).append(",").append(weight).append(",").append(isLiquid);
        return sendStr.toString();
    }

}
