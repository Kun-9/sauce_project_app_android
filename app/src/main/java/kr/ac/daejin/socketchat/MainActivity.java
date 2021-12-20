package kr.ac.daejin.socketchat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;

import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button startBtn;
    Button sendBtn;
    TextView chatLog;
    EditText sendMsgBox;
    EditText nameBox;
    Socket socket;
    public InputStream inputStream;
    public OutputStream outputStream;

    // 채팅 중 상태
    public int checkChatting = 0;
    // Log 태그
    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = (Button) findViewById(R.id.startBtn);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        chatLog = (TextView) findViewById(R.id.chatLog);
        sendMsgBox = (EditText) findViewById(R.id.sendMsgBox);
        nameBox = (EditText) findViewById(R.id.nameBox);

        // 채팅 내역 textView 스크롤 구현
        chatLog.setMovementMethod(new ScrollingMovementMethod());

        // 채팅 내역
        StringBuilder chattingStringBuilder = new StringBuilder();

        // CHAT START 버튼을 눌렀을 때
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 현재 채팅중인 상태인지 체크
                if (checkChatting == 0) {
                    startBtn.setText("CHAT STOP");
                    checkChatting = 1;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                // 소켓 연결
                                socket = new Socket("192.168.0.21", 5004);
                                inputStream = socket.getInputStream();
                                outputStream = socket.getOutputStream();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            byte[] buffer = new byte[1024];
                            int bytes;

                            // 연결 성공시 로그 출력
                            Log.d(TAG, "수신 시작");

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
                            //////////////////////////////////////
                            try {
                                // 인터럽트가 걸리지 않았을 때 계속 수신
                                while (!Thread.currentThread().isInterrupted()) {
                                    Log.d(TAG, "수신 대기");
                                    bytes = inputStream.read(buffer);
                                    String tmp = new String(buffer, 0, bytes);
                                    Log.d(TAG, tmp);
                                    // TextView에 inputStream 내용 이어붙힘
                                    chattingStringBuilder.append(tmp + "\n");
                                    chatLog.setText(chattingStringBuilder);
                                }
                            } catch (Exception e) {
                                Log.d(TAG, "수신 종료");
                            }
                        }
                    }).start();
                } else {
                    // 이미 실행중일 때
                    startBtn.setText("CHAT START");
                    try {
                        // 소켓 연결 해제 및 종료메시지 출력
                        socket.close();
                        chattingStringBuilder.append("disconnected\n");
                        chatLog.setText(chattingStringBuilder);

                        // 채팅내역 저장 메소드 실행
                        saveChatLog(String.valueOf(chattingStringBuilder));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // 채팅중이 아님을 표시
                    checkChatting = 0;
                }
            }
        });

        // send버튼을 눌렀을 때
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력값 저장 및 입력창 초기화
                String message = sendMsgBox.getText().toString();
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
}


