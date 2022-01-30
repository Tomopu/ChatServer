import java.io.BufferedReader;
import java.io.*;

public class Client{
    
    // サーバーに接続する
    public void connect(String ip, int port){
        // スレッド
        ClientThread client;
        client = new ClientThread(ip, port);
        // スレッドを作成
        client.start();
        String message;

        // キーボード入力についての設定
        BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));

        try{
            // 文字入力を受け付け
            while((message = keyIn.readLine()).length() > 0){
                // メッセージを送信
                client.send(message);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }   

    public static void main(String[] args){
        // 引数が2つあるか確認
        if(args.length == 2){
            String ip = args[0];
            int port = Integer.parseInt(args[1]);
            // サーバーに接続
            new Client().connect(ip, port);
        }else{
            System.out.println("引数: サーバーのIPアドレス　ポート番号");
        }
        return;
    }
}

