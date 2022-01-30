import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread{
    // スレッドを格納するためのリスト
    private static List<ServerThread> threads = new ArrayList<ServerThread>();
    // ソケット
    private Socket socket;
    // 自分の接続番号
    private int number = 0;

    // コンストラクタ
    public ServerThread(Socket socket){
        this.socket = socket;
        // リストにスレッドを格納
        threads.add(this);
        number = threads.indexOf(this);
        // クライアントの接続を通知
        System.out.println("new client connected!! (no. " + number + ")");
    }

    // スレッドを作成
    public void run(){
        InputStream in = null;
        String message;
        int size;
        byte[] w = new byte[1024];
        try{
            in = socket.getInputStream();
            // クライアントからのデータの受信を監視
            while(true){
                try{
                    size = in.read(w);
                    if(size<=0) throw new IOException();
                    message = new String(w, 0, size, "UTF-8");
                    // 接続しているクライアント全員にメッセージを送信
                    this.sendMessageAll(number + ":" + message);
                // クライアントが切断したら...
                }catch(IOException e){
                    System.err.println("*** Connection closed ***");
                    // ソケットを閉じて、リストからこのスレッドを削除
                    socket.close();
                    threads.remove(this);
                return;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // 接続しているクライアント全員にメッセージを送信
    public void sendMessageAll(String message){
        // クライアントのスレッド
        ServerThread thread;
        for(int i=0; i<threads.size(); i++){
            // リストのi番目のクライアント(サーバー側)のスレッドを取得
            thread = (ServerThread)threads.get(i);
            // もしも送信主(自分自身)だったら、メッセージを送信しない
            if(i == this.number) continue;
            // スレッドが存在したら、そのクライアントにメッセージを送信
            if(thread.isAlive()) thread.sendMessage(message);
        }
    }

    // クライアントにメッセージを送信
    public void sendMessage(String message){
        try{
            OutputStream out = socket.getOutputStream();
            byte[] b = message.getBytes("UTF-8");
            // メッセージを送信
            out.write(b);
            out.flush();
            System.out.println("送信しました。");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
