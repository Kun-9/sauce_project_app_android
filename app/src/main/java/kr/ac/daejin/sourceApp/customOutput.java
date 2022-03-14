package kr.ac.daejin.sourceApp;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;




public class customOutput extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_output_page);


        source_class.SourceList sourceList = ((MainActivity) MainActivity.context_main).sourceList;
        OutputStream outputStream = ((MainActivity) MainActivity.context_main).outputStream;

        EditText[] cart = new EditText[6];
        Integer[] cartId = {
                R.id.cart1, R.id.cart2, R.id.cart3, R.id.cart4, R.id.cart5, R.id.cart6
        };

        TextView[] cartName = new TextView[6];
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

                String sendStr = getOutputString(cart, sourceList);

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
                        }

                    }
                }).start();
            }
        });

        saveSourceCompBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
