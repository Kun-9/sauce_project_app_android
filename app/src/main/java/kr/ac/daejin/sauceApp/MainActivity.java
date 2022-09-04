package kr.ac.daejin.sauceApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static Context context_main;



    LinearLayout customOutputBtn, registSoruceBtn, tempBtn;
    Button sendBtn, showSourceListBtn, showCurrentSourceListBtn, showCurrentSourceExistBtn;
    TextView chatLog, isConnected, existCartridgeNum;
    EditText sendMsgBox;
    EditText nameBox;
    Socket socket;
    StringBuilder chattingStringBuilder;
    TextView[] cartName;
    CardView startBtn;
    LottieAnimationView dotAnimation;


    public source_class.SourceList sourceList;
    public SauceListManager.SauceList sauceList;
    public ArrayAdapter Adapter, Adapter2;
    public ArrayList<String> LIST_MENU;
    public ArrayList<String> comp_list;


    public InputStream inputStream;
    public OutputStream outputStream;


    // 채팅 중 상태
    public int checkConnecting = 0;
    // Log 태그
    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context_main = this;
        sourceList = new source_class.SourceList();
        sauceList = new SauceListManager.SauceList();

/////////////////////////////////////////////////////////////////////////////////////

        LIST_MENU = new ArrayList<String>();
        comp_list = new ArrayList<String>();

        // Adapter로 registedList에 전달, ListView 설정
        Adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU) ;
        Adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, comp_list) ;

/////////////////////////////////////////////////////////////////////////////////////


        sendBtn = (Button) findViewById(R.id.sendBtn);
        chatLog = (TextView) findViewById(R.id.chatLog);
        sendMsgBox = (EditText) findViewById(R.id.sendMsgBox);
        customOutputBtn = (LinearLayout) findViewById(R.id.customOutputBtn);
        registSoruceBtn = (LinearLayout) findViewById(R.id.registSoruceBtn);
        showSourceListBtn = (Button) findViewById(R.id.showSourceListBtn);
        showCurrentSourceExistBtn = (Button) findViewById(R.id.showCurrentSourceExistBtn);
        showCurrentSourceListBtn = (Button) findViewById(R.id.showCurrentSourceListBtn);
        isConnected = (TextView) findViewById(R.id.isConnected);
        existCartridgeNum = (TextView) findViewById(R.id.existCartridgeNum);
        startBtn = (CardView) findViewById(R.id.startBtn);
        dotAnimation = (LottieAnimationView) findViewById(R.id.dotAnimation);
        tempBtn = (LinearLayout) findViewById(R.id.tempBtn);

        SauceParse sauceParse = new SauceParse();

        try {
            File dir = new File("/data/data/kr.ac.daejin.SauceProjectApp/files");
            File file = new File(dir + "/currentSauceList.json");

            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int cnt = 0;
        for (int i = 0; i < 6; i++) {
            if (sauceList.getSauceList().get(i).getId().equals("null")) {
            } else {
                cnt++;
            }
        }
        existCartridgeNum.setText(String.valueOf(cnt));

//         현재 소스 업데이트
        sauceList.updateSauceList();

        showSourceListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringBuilder sb = new StringBuilder();
                String info = sauceParse.getCurrentSauceInfo(sauceList);

                sb.append(info);
//                sb.append(sauceParse.parseSauceJson(info));

                chattingStringBuilder.append("저장된 데이터 :\n");
                chattingStringBuilder.append(sb).append("\n");
                chatLog.setText(chattingStringBuilder);
            }
        });

        showCurrentSourceListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringBuilder sb = new StringBuilder();
                sauceList.updateSauceList();
                for (int i = 0; i < 6; i++) {
                    sb.append(sauceList.getSauceList().get(i)).append("\n");
                }

                chattingStringBuilder.append("카트리지 소스 객체 :\n");
                chattingStringBuilder.append(sb).append("\n");
                chatLog.setText(chattingStringBuilder);
            }
        });

        showCurrentSourceExistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        });


        customOutputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), customOutput.class);
                startActivity(intent);

            }
        });

        tempBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), tempClass.class);
//                startActivity(intent);
                if (checkConnecting == 1) {
                    // 새로운 쓰레드 생성
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            byte[] buffer = new byte[1024];
                            try {
                                // 입력값 전송
                                buffer = "0".getBytes(StandardCharsets.UTF_8);
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
        });



        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {

                int cnt = 0;
                for (int i = 0; i < 6; i++) {
                    if (sauceList.getSauceList().get(i).getId().equals("null")) {
                    } else {
                        cnt++;
                    }
                }
                existCartridgeNum.setText(String.valueOf(cnt));
            }
        };

        final Handler connectedHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (checkConnecting == 1) {

                    isConnected.setText("서버와 통신 중");
                    isConnected.setTextColor(getResources().getColor(R.color.white));
                    dotAnimation.setVisibility(View.VISIBLE);
                    startBtn.setCardBackgroundColor(getResources().getColor(R.color.darkNavy));
                } else {
                    isConnected.setText("연결");
                    isConnected.setTextColor(getResources().getColor(R.color.black));
                    startBtn.setCardBackgroundColor(getResources().getColor(R.color.white));
                    dotAnimation.setVisibility(View.INVISIBLE);

                }
            }
        };

        registSoruceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), tab_layout.class);
                startActivity(intent);
            }
        });

        EditText[] cart = new EditText[6];
        Integer[] cartId = {
                R.id.cart1, R.id.cart2, R.id.cart3, R.id.cart4, R.id.cart5, R.id.cart6
        };
        for (int i = 0; i < 6; i++) {
            cart[i] = findViewById(cartId[i]);
        }


        // 로 textView 스크롤 구현
        chatLog.setMovementMethod(new ScrollingMovementMethod());

        // 로그
        chattingStringBuilder = new StringBuilder();

        final int[] port = {8080};
        // 연결 버튼을 눌렀을 때
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "수신 시작");
                chattingStringBuilder.append("연결 시작");
                // 현재 연결중인 상태인지 체크
                if (checkConnecting == 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // 소켓 연결
                                socket = new Socket("192.168.0.17", 8080);
                                inputStream = socket.getInputStream();
                                outputStream = socket.getOutputStream();
                                checkConnecting = 1;

                                Message msg = connectedHandler.obtainMessage();
                                connectedHandler.sendMessage(msg);


                                chattingStringBuilder.append("연결 성공\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                                chattingStringBuilder.append("연결 실패\n");
                            }
                            byte[] buffer = new byte[1024];
                            int bytes;

                            // 연결 성공시 로그 출력
                            Log.d(TAG, "수신 시작");

                            /*
                            // 연결시 nameBox의 이름 값을 전송.
                            // 서버에서 소켓이 연결되고 첫번째 입력은 이름으로 설정하도록 함
                            String message = nameBox.getText().toString();
                            byte[] name = new byte[1024];
                            try {
                                name = message.getBytes(StandardCharsets.UTF_8);
                                outputStream.write(name);
                                outputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            */
                            //////////////////////////////////////
                            try {
                                // 인터럽트가 걸리지 않았을 때 계속 수신
                                while (!Thread.currentThread().isInterrupted()) {
                                    Log.d(TAG, "수신 중");
                                    bytes = inputStream.read(buffer);
                                    String tmp = new String(buffer, 0, bytes);
                                    Log.d(TAG, tmp);

                                    JsonParser jsonParser = new JsonParser();
                                    JsonElement jsonElement = jsonParser.parse(tmp);

                                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                                    String messageType = jsonObject.get("messageType").getAsString();
                                    JsonArray body = jsonObject.get("body").getAsJsonArray();

                                    // 만약 메시지 타입이 2라면? (소스 스캔 json 배열)
                                    if (messageType.equals("2")) {

                                        Log.d(TAG, "메시지타입 2번 수신 : 카트리지 스캔 정보");

                                        // json 배열 저장
                                        String msg = body.toString();
                                        Log.d(TAG, "받은 메시지에서 body 파싱 : " + msg);

                                        sauceParse.saveSauceList(msg);
                                        Log.d(TAG, "1");
                                        sauceList.updateSauceList();
                                        Log.d(TAG, "2");
                                    }

                                    // TextView에 inputStream 내용 이어붙힘
                                    chattingStringBuilder.append(tmp + "\n");
                                    chatLog.setText(chattingStringBuilder);

                                    Message msg = handler.obtainMessage();
                                    handler.sendMessage(msg);
                                }
                            } catch (Exception e) {
                                chattingStringBuilder.append("연결 실패\n");
                                Log.d(TAG, "수신 종료");
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    // 이미 실행중일 때
//                    isConnected.setText("연결");
//                    isConnected.setTextColor(getResources().getColor(R.color.black));
//                    startBtn.setCardBackgroundColor(getResources().getColor(R.color.white));

                    try {
                        // 소켓 연결 해제 및 종료메시지 출력
                        socket.close();
                        chattingStringBuilder.append("disconnected\n");
                        Message msg = connectedHandler.obtainMessage();
                        connectedHandler.sendMessage(msg);
                        chatLog.setText(chattingStringBuilder);

                        // 채팅내역 저장 메소드 실행
                        saveChatLog(String.valueOf(chattingStringBuilder));
                        checkConnecting = 0;
                        chattingStringBuilder.append("연결 실패\n");
                   } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // 채팅중이 아님을 표시
                }
            }
        });




        // send버튼을 눌렀을 때
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 6; i++) {
                    sb.append(cart[i].getText().toString()).append(" ");
                }

                // 입력값 저장 및 입력창 초기화
                String message = sendMsgBox.getText().toString();
//                String message = sb.toString();
                sendMsgBox.setText(null);

                // 새로운 쓰레드 생성
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        byte[] buffer = new byte[1024];
                        try {
                            // 입력값 전송
                            buffer = message.getBytes(StandardCharsets.UTF_8);
                            outputStream.write(buffer);
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });

    }

    // 채팅 로그 저장 메소드
    public void saveChatLog(String str) {

        // 현재 절대경로 값 path에 할당
        String path = getFilesDir().getAbsolutePath();

        // 절대경로에 chatLog.txt 라는 파일로 저장
        File file = new File(path + "/chatLog.txt");

        // 저장경로 로그로 확인
        Log.d(TAG, "path = " + path);
        try {
            FileOutputStream fos = new FileOutputStream(file);

            // 입력받은 str값을 txt파일에 입력
            fos.write(str.getBytes());
            fos.close();
            Log.d(TAG, "저장 완료");

            // 토스트 메시지로 저장 성공 여부 확인
            Toast toast = Toast.makeText(getApplicationContext(), "저장 성공", Toast.LENGTH_SHORT);
            toast.show();
        } catch (IOException e) {
            Log.d(TAG, "저장 실패");
            Toast toast = Toast.makeText(getApplicationContext(), "저장 성공", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

//    // json 저장 메소드
    public void saveSauceList(String str) {

        // 현재 절대경로 값 path에 할당
        String path = getFilesDir().getAbsolutePath();

        // 절대경로에 chatLog.txt 라는 파일로 저장
        File file = new File(path + "/currentSauceList.json");

        // 저장경로 로그로 확인
        Log.d(TAG, "path = " + path);
        try {
            FileOutputStream fos = new FileOutputStream(file);

            // 입력받은 str값을 txt파일에 입력
            fos.write(str.getBytes());
            fos.close();
            Log.d(TAG, "저장 완료");

        } catch (IOException e) {
            Log.d(TAG, "저장 실패");
            Toast toast = Toast.makeText(getApplicationContext(), "저장 성공", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
//
//
//    public String getCurrentSauceInfo() {
//        // 현재 절대경로 값 path에 할당
//        String path = getFilesDir().getAbsolutePath();
//        StringBuilder sb = new StringBuilder();
//
//        File file = new File(path + "/currentSauceList.json");
//
//        // 저장경로 로그로 확인
//        Log.d(TAG, "path = " + path);
//        try {
//            FileInputStream fos = new FileInputStream(file);
//            InputStreamReader isr = new InputStreamReader(fos);
//            BufferedReader bufferedReader = new BufferedReader(isr);
//
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                sb.append(line);
//            }
//
//            fos.close();
//            Log.d(TAG, "읽기 완료");
//
//            return sb.toString();
//
//        } catch (IOException e) {
//            Log.d(TAG, "읽 실패");
//            return null;
//        }
//
//    }
//
//    public String parseSauceJson(String json) {
//        StringBuilder sb = new StringBuilder();
//        JsonParser jsonParser = new JsonParser();
//        JsonElement jsonElement = jsonParser.parse(json);
//
//        JsonArray data = jsonElement.getAsJsonArray();
//
//        for (JsonElement sauce : data) {
//            JsonObject JsonObject = sauce.getAsJsonObject();
//            String id = JsonObject.get("id").getAsString();
//            String name = JsonObject.get("name").getAsString();
//            String isLiquid = JsonObject.get("isLiquid").getAsString();
//            sb.append("id = " + id + " name = " + name + " isLiquid = " + isLiquid).append("\n");
//            System.out.println("id = " + id + " name = " + name + " isLiquid = " + isLiquid);
//        }
//        return sb.toString();
//    }


}







