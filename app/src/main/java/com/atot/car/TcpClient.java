package com.atot.car;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: oldthank
 * @date: 2021/6/18
 */
public class TcpClient {

    private ExecutorService mThreadPool;
    private Socket socket;
    private String ip;
    private Integer port;
    private String TAG = this.getClass().toString();

    public  TcpClient(Socket socket, String ip, int port) {
        this.socket = socket;
        this.ip = ip;
        this.port = port;

    }

    public void init(){
        // 初始化线程池
        mThreadPool = Executors.newCachedThreadPool();
        try {
            mThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket = new Socket(ip,port);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     * @param message 字符串消息
     */
    public void sendMessage(String message){
       mThreadPool.execute(new Runnable() {
           @Override
           public void run() {
               try {
                   OutputStream outputStream = socket.getOutputStream();
                   outputStream.write(message.getBytes("UTF-8"));
                   outputStream.flush();
               } catch (IOException e) {
                   e.printStackTrace();
               }

           }
       });
    }
}
