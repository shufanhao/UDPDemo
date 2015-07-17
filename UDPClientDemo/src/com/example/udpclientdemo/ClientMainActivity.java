package com.example.udpclientdemo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ClientMainActivity extends Activity {

	public static TextView show;  
	public static Button press;  
	public static boolean flag;  
	/*private static final int MAX_DATA_PACKET_LENGTH = 40;//最大的数据包长度
	private byte[] buffer = new byte[MAX_DATA_PACKET_LENGTH];  
	private DatagramPacket dataPacket; 
	private DatagramSocket socket;  */
   
	// Called when the activity is first created.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_main);
        press = (Button)findViewById(R.id.sendudp);  
        press.setOnClickListener(new Button.OnClickListener()  
        {  
            @Override  
            public void onClick(View v)  
            {  
            	Thread threadClientSocket = new Thread(new ThreadClient());//必要要在子线程中访问网络在主线程中访问网络是不行的
    			threadClientSocket.start();   
            }  
        });  
    }
	public void connectServerWithUDPSocket() {  
	    try {  
	        //创建DatagramSocket对象并指定一个端口号，注意，如果客户端需要接收服务器的返回数据,   
	        //还需要使用这个端口号来receive，所以一定要记住   
	    	//System.out.println("相应按钮");
	        DatagramSocket socket = new DatagramSocket(9400);  
	        //使用InetAddress(Inet4Address).getByName把IP地址转换为网络地址     
	        InetAddress serverAddress = InetAddress.getByName("192.168.1.120");  
	        String str = "123";//设置要发送的报文    
	        byte data[] = str.getBytes();//把字符串str字符串转换为字节数组     
	        System.out.println("发送数据"+ data);
	        //创建一个DatagramPacket对象，用于发送数据。     
	        //参数一：要发送的数据  参数二：数据的长度  参数三：服务端的网络地址  参数四：服务器端端口号    
	        DatagramPacket packet = new DatagramPacket(data, data.length ,serverAddress ,9400);    
	        socket.send(packet);//把数据发送到服务端。 
	        socket.close();//要加这个close()如果不加的话只能收到一次数据
	    } catch (SocketException e) {  
	        e.printStackTrace();  
	    } catch (UnknownHostException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }    
	}
	public class ThreadClient implements Runnable{
    	public void run(){
    		try{
    	   connectServerWithUDPSocket();//第一步去连接
           System.out.println("已经运行到线程这个一步了");
           Thread.sleep(50);	 
    	}
    	catch(Throwable t){
		}
    }
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.client_main, menu);
		return true;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);  
    		builder.setTitle("提示");  
    		builder.setMessage("亲！您确定要退出程序吗？");  
    		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO Auto-generated method stub
    	            System.exit(0);
    			}    		  
    		});  
    		builder.setNegativeButton("取消", null);  
    		builder.show();
        }
		return true;
    }
}
