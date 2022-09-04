package kr.ac.daejin.sauceApp;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.suke.widget.SwitchButton;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class customOutput extends AppCompatActivity {

    public Context context_customOutput;
    Boolean sauceExist;
    SwitchButton editMode;
    Dialog customdialog, outputValueDialog, editNameDialog, editLiquidDialog, submitDialog;
    public TextView dialog_msg;
    public TextView[] cart, cartName, g;

    Gson gson = new Gson();
    CardView[] cartCard;
    source_class.SourceList sourceList;
    SauceListManager.SauceList sauceList;
    SauceListManager.SauceComp sauceComp;
    SauceListManager.SauceCompManager sauceCompManager;
    ArrayList<String> comp_list;
    CardView card1, card2, card3, card4, card5, card6;

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

        context_customOutput = this;
        comp_list = ((MainActivity) MainActivity.context_main).comp_list;
        checkConnecting = ((MainActivity) MainActivity.context_main).checkConnecting;
        sourceList = ((MainActivity) MainActivity.context_main).sourceList;
        sauceList = ((MainActivity) MainActivity.context_main).sauceList;
        sauceComp = ((MainActivity) MainActivity.context_main).sauceComp;
        sauceCompManager = ((MainActivity) MainActivity.context_main).sauceCompManager;
        outputStream = ((MainActivity) MainActivity.context_main).outputStream;

        cart = new TextView[6];
        Integer[] cartId = {
                R.id.cart1, R.id.cart2, R.id.cart3, R.id.cart4, R.id.cart5, R.id.cart6
        };

        cartName = new TextView[6];
        Integer[] cartNameId = {
                R.id.cart1Name, R.id.cart2Name, R.id.cart3Name, R.id.cart4Name, R.id.cart5Name, R.id.cart6Name
        };

        cartCard = new CardView[6];
        Integer[] cartCardId = {
                R.id.cartridge1, R.id.cartridge2, R.id.cartridge3, R.id.cartridge4, R.id.cartridge5, R.id.cartridge6
        };

        g = new TextView[6];
        Integer[] gId = {
                R.id.g1, R.id.g2, R.id.g3, R.id.g4, R.id.g5, R.id.g6
        };


        for (int i = 0; i < 6; i++) {
            cart[i] = findViewById(cartId[i]);
            cartName[i] = findViewById(cartNameId[i]);
            cartCard[i] = findViewById(cartCardId[i]);
            g[i] = findViewById(gId[i]);
        }

        Button customOutputSendBtn, saveSourceCompBtn;
        customOutputSendBtn = (Button) findViewById(R.id.customOutputSendBtn);
        saveSourceCompBtn = (Button) findViewById(R.id.saveSourceCompBtn);
        editMode = (SwitchButton) findViewById(R.id.editMode);

//        preSavedSauce preSavedSauce = new preSavedSauce();
        refreshSauceInfo();


        editMode.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                for (int i = 0; i < 6; i++) {
                    YoYo.with(Techniques.Swing).duration(900).repeat(0).playOn(cartCard[i]);
                }

                if (editMode.isChecked()) {

                    for (int i = 0; i < 6; i++) {
                        if (!sauceList.getSauceList().get(i).getId().equals("null")) {
                            cartName[i].setTextColor(getResources().getColor(R.color.white));
                            cart[i].setTextColor(getResources().getColor(R.color.white));
                            g[i].setTextColor(getResources().getColor(R.color.white));
                            cartCard[i].setCardBackgroundColor(getResources().getColor(R.color.LightDarkNavy));
                        }
                    }

                } else {
                    for (int i = 0; i <6; i++) {
                        if (!sauceList.getSauceList().get(i).getId().equals("null")) {
                            cartName[i].setTextColor(getResources().getColor(R.color.black));
                            cart[i].setTextColor(getResources().getColor(R.color.black));
                            g[i].setTextColor(getResources().getColor(R.color.black));
                            cartCard[i].setCardBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }
                }
            }
        });


        cartCard[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCartridge(0);
            }
        });

        cartCard[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCartridge(1);
            }
        });

        cartCard[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCartridge(2);
            }
        });

        cartCard[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCartridge(3);
            }
        });

        cartCard[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCartridge(4);
            }
        });

        cartCard[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCartridge(5);
            }
        });

        //////////////////////////////////////////////////////////


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
//                registCompDialog();
//                saveCurrentComp();
//                sauceCompManager.loadSavedSauceCompString("hello2");
//                sauceCompManager.saveSauceComp("hello2", 0);


//                saveCurrentComp("temp1");

                saveCurrentComp("temp2");
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

    public void showInputValueDialog(TextView tv) {

//        outputValueDialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
//        outputValueDialog = requestWindowFeature(Window.FEATURE_NO_TITLE);

        outputValueDialog = new Dialog(this);
        outputValueDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        outputValueDialog.setContentView(R.layout.outputvalue_page);

        outputValueDialog.show();

        EditText value = outputValueDialog.findViewById(R.id.edittextValue);
        Button confirm = outputValueDialog.findViewById(R.id.btnConfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str;
                Editable text = value.getText();

                if (TextUtils.isEmpty(text)) {
                    str = "0";
                } else if (Integer.parseInt(text.toString()) > 50) {
                    str = "50";
                    Toast.makeText(getApplicationContext(), "50g 아래의 수를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    str = text.toString();
                    Toast.makeText(getApplicationContext(), "입력되었습니다.", Toast.LENGTH_SHORT).show();
                }
                tv.setText(str);
                outputValueDialog.dismiss();
            }
        });
    }

    @NonNull
    private String getOutputString(TextView[] cart, source_class.SourceList sourceList) {
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

    void editNameDialog(int num) {

        editNameDialog = new Dialog(this);
        editNameDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editNameDialog.setContentView(R.layout.edit_sauce_name_page);

        editNameDialog.show();

        EditText valueName = (EditText) editNameDialog.findViewById(R.id.editName);
        Button btnName = (Button) editNameDialog.findViewById(R.id.btnName);

        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable text = valueName.getText();
                String name = "지정되지 않은 이름";

                System.out.println("edit.onClick 1");
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(getApplicationContext(), "이름이 입력되지 않았습니다", Toast.LENGTH_SHORT).show();
                } else {

                    name = text.toString();
//                     액체 여부 묻고 라즈베리파이에 수정 요청
                    editLiquidDialog(editNameDialog, name, num);

                    editNameDialog.dismiss();
                }
            }
        });
    }

    void editLiquidDialog(Dialog dialog, String name, int num) {

        editLiquidDialog = new Dialog(this);
        editLiquidDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editLiquidDialog.setContentView(R.layout.edit_sauce_liquid_page);

        editLiquidDialog.show();

        SwitchCompat liquidSwitch = (SwitchCompat) editLiquidDialog.findViewById(R.id.selectIsLiquid);
        Button btnEditLiquid = (Button) editLiquidDialog.findViewById(R.id.btnLiquidEditConfirm);

        btnEditLiquid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String isLiquid = "0";
                if (liquidSwitch.isChecked()) {
                    isLiquid = "1";
                }

                sendEditSauceMethod(num, name, isLiquid, sauceList.getSauceList().get(num).getId());
                submitDialog();
                if (checkConnecting == 1) {
                    sauceList.getSauceList().get(num).setName(name);
                    sauceList.getSauceList().get(num).setIsLiquid(isLiquid);

                    SauceParse sauceParse = new SauceParse();
                    sauceParse.saveSauceList(gson.toJson(sauceList.getSauceList()));
                    refreshSauceInfo();
                }
                editLiquidDialog.dismiss();
                editNameDialog.dismiss();
            }
        });
    }

    void submitDialog() {
        submitDialog = new Dialog(this);
        submitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        submitDialog.setContentView(R.layout.submit);
        submitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LottieAnimationView submit = (LottieAnimationView) submitDialog.findViewById(R.id.submitAnimation);

        if(checkConnecting == 0) {
            submit.setAnimation(R.raw.reject);
        }

        submitDialog.show();

        submit.playAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                submitDialog.dismiss();
            }
        },2500);
    }


    public void sendEditSauceMethod(int num, String name, String isLiquid, String id) {
        if (checkConnecting == 1) {
            // 새로운 쓰레드 생성
            new Thread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder sb = new StringBuilder();
                    byte[] buffer = new byte[1024];
                    try {
                        // 입력값 전송
                        SauceListManager.Sauce sauce = new SauceListManager.Sauce(name, isLiquid, id);
                        Gson sendMsg = new Gson();
                        String s = sb.append("7").append(sendMsg.toJson(sauce)).toString();

                        buffer = s.getBytes(StandardCharsets.UTF_8);
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

    void checkCartridge(int num) {
        if (!sauceList.getSauceList().get(num).getId().equals("null")) {
            if (editMode.isChecked()) {
                editNameDialog(num);
            } else {
                showInputValueDialog(cart[num]);
            }
        }
    }

    void refreshSauceInfo() {
        for (int i = 0; i < 6; i++) {
            if (sauceList.getSauceList().get(i).getId().equals("null")) {
                cartName[i].setText("없음");
                cartName[i].setTextColor(getResources().getColor(R.color.lightgray));
                cart[i].setTextColor(getResources().getColor(R.color.lightgray));
                g[i].setTextColor(getResources().getColor(R.color.lightgray));

            } else {
                cartName[i].setText(sauceList.getSauceList().get(i).getName());
                cartName[i].setTextColor(getResources().getColor(R.color.black));
                cart[i].setTextColor(getResources().getColor(R.color.black));
                g[i].setTextColor(getResources().getColor(R.color.black));
            }
        }
    }

    void saveCurrentComp(String compName) {

        ArrayList<SauceListManager.Sauce> comp = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String weight = cart[i].getText().toString();
            if(weight.equals("0")) {
               continue;
            }
            String name = sauceList.getSauceList().get(i).getName();
            String isLiquid = sauceList.getSauceList().get(i).getIsLiquid();


            comp.add(new SauceListManager.Sauce(name, isLiquid, Integer.parseInt(weight)));
        }
        SauceListManager.SauceComp compData = new SauceListManager.SauceComp(comp);
        sauceCompManager.saveSauceComp(compName, compData);
//        sauceComp.saveSauceComp("hello2");
    }
}
