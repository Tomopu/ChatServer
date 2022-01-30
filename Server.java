import java.io.*;
import java.net.*;

public class Server{
    // サーバーの最大接続人数
    static final int ACCEPTMAX = 2;

    // サーバーを起動
    public void start(int port){
        // サーバー
        ServerSocket server;
        // 接続しているクライアントの人数
        int acceptCount = 0;
        try{
            // サーバーを起動
            server = new ServerSocket(port);
            // サーバー起動を通知
            System.out.println("Server Start!! (Port:" + port +")");
            // 接続している人数が最大人数未満なら
            while(acceptCount < ACCEPTMAX){
                try{
                    Socket socket = null;
                    // クライアントを受け入れ
                    socket = server.accept();
                    // スレッドを作成
                    new ServerThread(socket).start();
                    // 接続人数を追加
                    acceptCount++;
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }catch(IOException e){
            System.err.println(e);
        }
    }

    public static void main(String[] args){
        if(args.length == 1){
            int port = Integer.parseInt(args[0]);
            Server server = new Server();
            server.start(port);
        }else{
            System.out.println("引数: ポート番号");
        }
        return;
    }
}