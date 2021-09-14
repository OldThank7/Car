package com.atot.car;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnLongClickListener, View.OnClickListener {

    private final String TAG = this.getClass().toString();
    private WifiManager wifiManager;
    private LocationManager locationManager;
    private TcpClient tcpClient;
    private WebView viewById;
    private String ip = "192.168.0.1";
    private int port = 55789;
    private Socket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init(){
        viewById = findViewById(R.id.webview);

        findViewById(R.id.btn_U).setOnTouchListener(this);
        findViewById(R.id.btn_B).setOnTouchListener(this);
        findViewById(R.id.btn_L).setOnTouchListener(this);
        findViewById(R.id.btn_R).setOnTouchListener(this);

        findViewById(R.id.btn_Stop).setOnLongClickListener(this);
        findViewById(R.id.btn_Start).setOnLongClickListener(this);
        findViewById(R.id.btn_showCvan).setOnLongClickListener(this);

        findViewById(R.id.btn_Start).setOnClickListener(this);
        findViewById(R.id.btn_horn).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (socket != null) {
            int action = event.getAction();
            switch (v.getId()) {
                case R.id.btn_U:
                    if (action == MotionEvent.ACTION_DOWN) {
                        tcpClient.sendMessage("0");
                    }
                    else if(action == MotionEvent.ACTION_UP) {
                        tcpClient.sendMessage("5");
                    }
                    break;
                case R.id.btn_B:
                    if (action == MotionEvent.ACTION_DOWN)
                        tcpClient.sendMessage("1");
                    else if (action == MotionEvent.ACTION_UP)
                        tcpClient.sendMessage("5");
                    break;
                case R.id.btn_L:
                    if (action == MotionEvent.ACTION_DOWN) {
                        tcpClient.sendMessage("2");
                    }
                    else if (action == MotionEvent.ACTION_UP) {
                        tcpClient.sendMessage("5");
                    }
                    break;
                case R.id.btn_R:
                    if (action == MotionEvent.ACTION_DOWN)
                        tcpClient.sendMessage("3");
                    else if (action == MotionEvent.ACTION_UP)
                        tcpClient.sendMessage("5");
                    break;
            }
        }else if (socket == null){
            Toast.makeText(this, "请先启动小车...", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.btn_Start:
                if (socket != null) {
                    try {
                        socket.close();
                        Toast.makeText(this, "小车已关闭...", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if(socket == null)
                    Toast.makeText(this, "请先启动小车...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_Stop:
                if (socket != null)
                    tcpClient.sendMessage("5");
                else if(socket == null)
                    Toast.makeText(this, "请先启动小车...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_showCvan:
                String str = "http://192.168.0.1:81/stream";
                viewById.loadUrl(str);
                break;
            case R.id.lamp:
                tcpClient.sendMessage("7");
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_Start:
                socket = new Socket();
                //启动连接
                tcpClient = new TcpClient(socket, ip, port);
                tcpClient.init();
                Toast.makeText(this, "小车正在启动...", Toast.LENGTH_SHORT).show();
                tcpClient.sendMessage("6");
                break;
            case R.id.btn_horn:
                //小车喇叭
                tcpClient.sendMessage("6");
                break;
        }
    }
}