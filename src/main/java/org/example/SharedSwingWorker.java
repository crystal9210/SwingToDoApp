package org.example;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

class SharedSwingWorker extends SwingWorker<Void , Void> {

    //make the instance of the queue to make threadsafe process to save the change of the record
    private Queue<Cell> queue = new ConcurrentLinkedQueue<>();

    //the method to add a cell to the queue
    public void addCell(Cell cell) {
        queue.offer(cell);  //the method "add" is also ok.
    }

//     don't use this method
//    public void setCell(Cell cell){
//        queue.offer(cell);
//    }

    // Singleton instance
    private static SharedSwingWorker instance;

    // Make the constructor private to prevent instantiation
    private SharedSwingWorker() {
    }

    // Provide a global point of access to the singleton object
    public static synchronized SharedSwingWorker getInstance() {
        if (instance == null) {
            instance = new SharedSwingWorker();
        }
        return instance;
    }

    // doInBackgroundで使われるデータベース更新ロジックを新しいメソッドに分割
    public void updateDatabase() throws Exception {
        // データベース更新ロジック（doInBackgroundメソッドから移動）

        try {
            // ドライバの登録
            Class.forName("org.sqlite.JDBC");

            // DBへの接続
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");

            // キュー内のすべてのセルに対して処理を行う
            while (!queue.isEmpty()) {
                Cell cell = queue.poll();  // キューからセルを取得し、それをキューから削除

                boolean select = cell.isSelect();
                String todo = cell.getTodo();
                System.out.println(todo);
                System.out.println(select);

                // ステートメントの作成
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE todolist SET is_done = ? WHERE Todo = ?;"
                );

                // プリペアドステートメントに値をセット
                preparedStatement.setBoolean(1, select);
                preparedStatement.setString(2, todo);

                connection.setAutoCommit(false);  // Before the query

                // クエリの実行
                int updatedRows=preparedStatement.executeUpdate();
                System.out.println("Updated rows: " + updatedRows);

                connection.commit();  // After the query

                // プリペアドステートメントをクローズする
                preparedStatement.close();
            }

            // 接続をクローズする
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //the method to update the DB table
    @Override
    protected Void doInBackground() throws Exception {
        updateDatabase();
        return null;
    }
}
