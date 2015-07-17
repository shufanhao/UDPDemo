package com.example.tcpserverdemo;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;
public class ServerMainActivity extends Activity {

	private final int MSG_REFRESH = 0;
    private Handler myHandler;
    private TextView txtView;
    private MediaPlayer mediaPlayer;
    String result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_main);
		txtView = (TextView)findViewById(R.id.txtResult);//结果返回
		Thread threadSocketServer = new Thread(new ServerThread());
        threadSocketServer.start();
        myHandler = new MyHandler();
	}
     public class ServerThread implements Runnable {
	    	public void run() {
	    		try{   
	    		ServerReceviedByUdp();
	    		Thread.sleep(50);
	    		}
	    		catch(Throwable t){
	    		}
	    	}
	 }
	public void ServerReceviedByUdp(){  
	    //创建一个DatagramSocket对象，并指定监听端口。（UDP使用DatagramSocket）     
	    DatagramSocket socket;
	    while(true){
	    try {  
	        socket = new DatagramSocket(9400);  
	        //创建一个byte类型的数组，用于存放接收到得数据     
	        byte data[] = new byte[4*1024];    
	        //创建一个DatagramPacket对象，并指定DatagramPacket对象的大小     
	        DatagramPacket packet = new DatagramPacket(data,data.length);    
	        //读取接收到得数据     
	        socket.receive(packet);    
	        //把客户端发送的数据转换为字符串。     
	        //使用三个参数的String方法。参数一：数据包 参数二：起始位置 参数三：数据包长     
	        //调试的时候出现的问题：因为之前写的是String result = new String(packet.getData(),packet,getOffset())但是
	        result = new String(packet.getData(),packet.getOffset() ,packet.getLength());
	    	result = result.trim();//去掉空格
	        System.out.println("服务器端收到的数据是"+ result);
	        if(result != null) {
				myHandler.sendEmptyMessage(MSG_REFRESH);   //报告到UI界面，更新图。 用sendEmptyMessage（）向Handler发送消息
														   //发送的消息是MSG_REFRESH
				 if(result.equals("nikonphoto")){ //在这个地方加。。
			        	//Toast.makeText(ServerMainActivity.this,"登录成功", Toast.LENGTH_SHORT).show();之前用这个语句出问题。
						mediaPlayer=MediaPlayer.create(ServerMainActivity.this, R.raw.nikon_shot);
						System.out.println("已经播放完音乐");
						mediaPlayer.start(); //启动或者恢复播放
						mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {//
					        public void onCompletion(MediaPlayer arg0) {  
					        mediaPlayer.release(); 
					      }
						});  
		        	}
	        }                        
	        /*String line;
    		line=result.readLine();*/
    		//一旦检测到有数据就会调用以下函数将数据发送到Handler的handleMessage()让它来进行处理
	      
	    } catch (SocketException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }
	    }
	}
	class MyHandler extends Handler{
    	public void handleMessage(Message msg) { //handler用handleMessage方法去接收消息，然后根据消息的不同执行不同的操作
			// TODO Auto-generated method stub   
			switch (msg.what) {                 //判断what值
			case MSG_REFRESH:                   //what等于MSG_REFRESH时
				System.out.println("Handler服务器端收到的数据是"+ result);
				//之前 这个地方的输出的是NULL但是上边的那个输出的是正确的。
				//txtView.setText(result); setText()  把以前的内容冲掉了，而append()在以前的内容后面添加
				txtView.append(result);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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