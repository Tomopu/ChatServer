import java.io.*;
import java.net.*;

public class ClientThread extends Thread{
    private Socket socket;
    // ストリーム
    private InputStream in;
    private OutputStream out;

    // IPアドレスとポート番号
    private String ip;
    private int port;

    // インスタンス
    public ClientThread(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    // スレッドの作成
    public void run(){
        try{
            // サーバーに接続
            connect(this.ip, this.port);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // サーバーに接続
    public void connect(String ip, int port){
        // 送られてきたデータのサイズ
        int size;
        String str;
        byte[] b = new byte[1024];
        try{
            socket = new Socket(ip, port);
            System.out.println("接続しました:" + socket.getRemoteSocketAddress());
            in = socket.getInputStream();
            out = socket.getOutputStream();

            //　送られてくるデータを監視
            while(socket != null && socket.isConnected()){
                size = in.read(b);
                if(size <= 0) continue;
                str = new String(b, 0, size, "UTF-8");
                System.out.println(str);
            }
        }catch(IOException e){
            e.printStackTrace();
            disconnect();
        }
    }

    // サーバーにデータを送信
    public void send(String message){
        byte[] b = message.getBytes();
        try{
            if(out != null){
                out.write(b);
                out.flush();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // サーバーから切断
    public void disconnect(){
        try{
            if(socket != null){
                socket.close();
                socket = null;
                System.out.println("切断しました。");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
