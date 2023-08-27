package org.example;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
/*from w w  w  .j av a2s  . c  om*/
import javax.imageio.ImageIO;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {



        //チェックボックスの変更処理をDBへ反映させるための一意なバックグラウンドスレッドの生成
        SharedSwingWorker worker = SharedSwingWorker.getInstance();


//        SwingUtilities.invokeLater() メソッド：指定されたタスクをEDTにスケジュールし、非同期に実行する
//        →UIに関連する全ての処理はEDT内で行う必要あり（Swingコンポーネントのスレッドセーフ性を保証し、正しい表示と動作を実現）
        SwingUtilities.invokeLater(() -> {
            try {
                //publicなクラスのインスタンス生成＋メソッド実行
                TodoListGUI example = new TodoListGUI();
                example.createAndShowGUI(worker);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // シャットダウンフックを登録します
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                worker.execute(); // プログラム終了時にデータベースを更新
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

// ここに到達する時点でSwingWorkerのタスクが完了
    }

}

