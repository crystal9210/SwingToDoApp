package org.example;

import java.awt.*;
//import java.awt.event.ComponentAdapter;
import java.awt.image.ImageProducer;
import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;  //
import java.sql.Statement;  //
//import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableCellEditor;→ダメだったのでDefaultCellEditorをimportした
import javax.swing.table.TableCellRenderer;
import java.awt.event.*;

//the library to set the background image
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


//  this file includes the classes
public class TodoListGUI {


    private JTable table;
    private DefaultTableModel tableModel;

    //GUIを作成して表示するクラス
    protected void createAndShowGUI(SharedSwingWorker worker) throws IOException {

        //the field to hold the access to the GBT
        SharedSwingWorker BGT = worker;

        // テーブルのカラム名を定義
        String[] columnNames = {"is_done", "Todo", "deadline"};

        // テーブルモデルを作成、第二引数は初期状態の行の指定（0行）
        tableModel = new DefaultTableModel(columnNames, 0);

        // テーブルを作成
        table = new JTable(tableModel);

        // データベースからテーブルデータを読み込む
        loadTableDataFromDatabase();

        // チェックボックスセルレンダラーを設定(テーブルのセルにチェックボックスをレンダリングするためのカスタムのセルレンダラー)
        table.getColumnModel().getColumn(0).setCellRenderer(new CheckBoxRenderer());

        // チェックボックスセルエディターを設定（テーブルのセルにチェックボックスを編集するためのカスタムのセルエディタ→チェックぼく酢の値が変更された際にDBを更新する）
        table.getColumnModel().getColumn(0).setCellEditor(new CheckBoxEditor(BGT));

        //set the height of the table columns
        table.setRowHeight(16);

        // テーブルをスクロール可能なパネルに配置
        JScrollPane scrollPane = new JScrollPane(table);




        // フレームを作成し、パネルとテーブルを追加
        JFrame frame = new JFrame("Yourタスク管理App/ver.2.6");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // JScrollPaneの高さを設定
//        scrollPane.setBackground(Color.WHITE);

        ImageIcon desktopIcon = new ImageIcon("YourToDo.png");
        frame.setIconImage(desktopIcon.getImage());


        //make the texFields and the button to set tasks
        JLabel label1 = new JLabel("タスク名:");
        label1.setForeground(Color.ORANGE);
        JLabel label2 = new JLabel("月:");
        label2.setForeground(Color.ORANGE);
        JLabel label3 = new JLabel("日:");
        label3.setForeground(Color.ORANGE);
//        label1.setBackground(Color.white);
//        label2.setBackground(Color.white);
//        label3.setBackground(Color.white);

        // ToDo追加用テキストフィールドの生成
        JTextField toDoInputField1 = new JTextField();  //privateにしてはだめ？
        JTextField toDoInputField2 = new JTextField();
        JTextField toDoInputField3 = new JTextField();
        toDoInputField1.setColumns(10); // 幅を設定
        toDoInputField2.setColumns(10); // 幅を設定
        toDoInputField3.setColumns(10);
        toDoInputField1.setOpaque(true);
        toDoInputField1.setBackground(Color.white);
        toDoInputField2.setOpaque(true);
        toDoInputField2.setBackground(Color.white);
        toDoInputField3.setOpaque(true);
        toDoInputField3.setBackground(Color.white);


        JButton deleteButton = new JButton("Delete");
        deleteButton.setOpaque(true);
//        deleteButton.isBackgroundSet();
//        deleteButton.setBackground(Color.WHITE);
        JButton addButton = new JButton("Add");
        addButton.setOpaque(true);




        // create the layered pane to set the background image
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new BorderLayout());
        layeredPane.setPreferredSize(new Dimension(620,480));



        // create a panel for the other components
        JPanel contentPanel = new JPanel();
//        contentPanel.setBackground(Color.white); // make it transparent
        contentPanel.setLayout(new BorderLayout());

        JPanel panel2 = new JPanel();
        JPanel panelC = new JPanel();
        JPanel voidPane = new JPanel();


        // パネルを作成し、ボタンを追加
        panel2.add(label1);
        panel2.add(toDoInputField1);
        panel2.add(label2);
        panel2.add(toDoInputField2);
        panel2.add(label3);
        panel2.add(toDoInputField3);
        panel2.add(addButton);
        panel2.add(deleteButton);
        panel2.setLayout(new FlowLayout());
//        panel2.setOpaque(true);
//        panel2.setBackground(Color.white); // make it transparent

        contentPanel.add(panel2,BorderLayout.NORTH);
        panelC.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(panelC,BorderLayout.CENTER);
        contentPanel.add(voidPane, BorderLayout.SOUTH);
//        contentPanel.add(new JLabel("East"), BorderLayout.EAST);
//        contentPanel.add(new JLabel("West"), BorderLayout.WEST);

        contentPanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());


        //define background panel i.e. bp
        // 背景画像付きのパネルを作成
        //set the path to the graphical image・☆☆warning:you should write the relative path from the root directory of the project!!!!
//        String imagePath = ".\\src\\main\\java\\org\\example\\background.png";

        final ImageIcon[] icon=new ImageIcon[1];
        final Image[] image = new Image[1];
        try{
            Image originalImage=ImageLoader.loadImage("background.png");    //this is the fault point
            icon[0] = new ImageIcon(originalImage);
            image[0] = icon[0].getImage();

        }catch (IOException e){
            e.printStackTrace();
        }

            // 画像を表示するためのラベル
            ImageLabel label = new ImageLabel(icon[0]);

        JPanel bp = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // GraphicsをGraphics2Dにキャスト
                Graphics2D g2d = (Graphics2D) g.create();

                // コンポーネント領域を取得し、それをクリップ領域として設定
                for (Component comp : panel2.getComponents()) {
                    Rectangle r = comp.getBounds();
                    // クリップを設定する前に元のクリップ領域を保存
                    Shape oldClip = g2d.getClip();
                    // クリップ領域を設定
                    g2d.clipRect(r.x, r.y, r.width, r.height);
                    // 描画する画像をクリップ領域外に制限
                    g2d.setClip(oldClip);
                }

                for (Component comp : scrollPane.getComponents()) {
                    Rectangle r = comp.getBounds();
                    // クリップを設定する前に元のクリップ領域を保存
                    Shape oldClip = g2d.getClip();
                    // クリップ領域を設定
                    g2d.clipRect(r.x, r.y, r.width, r.height);
                    // 描画する画像をクリップ領域外に制限
                    g2d.setClip(oldClip);
                }

                // 描画の透明度を50%に設定
                g2d.setComposite(AlphaComposite.SrcOver.derive(0.7f));

                // 画像がパネル全体にフィットするように描画
                g2d.drawImage(image[0], 0, 0, getWidth(), getHeight(), this);

                // Graphics2Dオブジェクトを解放
                g2d.dispose();}
        };
        bp.add(label);




        // ボタンを作成
        deleteButton.addActionListener((ActionEvent e) -> {
            int[] selectedRows = table.getSelectedRows();
            if (selectedRows.length > 0) {
                // 選択された行を逆順に処理する（後ろから削除しても問題ないため）
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int modelRow = table.convertRowIndexToModel(selectedRows[i]);
                    Object todo = tableModel.getValueAt(modelRow, 1);
                    Object deadline = tableModel.getValueAt(modelRow, 2);
                    tableModel.removeRow(modelRow);

                    // データベースから値を削除
                    try {
                        // ドライバの登録
                        Class.forName("org.sqlite.JDBC");

                        // DBへの接続
                        Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");

                        // ステートメントの作成
                        Statement statement = connection.createStatement();

                        // 値を削除するクエリ
                        String deleteQuery = "DELETE FROM todolist WHERE Todo = '" + todo + "' AND deadline = '" + deadline + "'";

                        // クエリの実行
                        statement.executeUpdate(deleteQuery);

                        System.out.println("A row is deleted without issues.");

                        // ステートメントをクローズ
                        statement.close();

                        // コネクションをクローズ
                        connection.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            int tableWidth = (int) (contentPanel.getWidth() * 0.95);

            if(table.getRowCount()<=6) {
                scrollPane.setPreferredSize(new Dimension(tableWidth, 16 * (tableModel.getRowCount() + 1) + 7));
//                scrollPane.setMaximumSize(new Dimension(tableWidth, 16*6+7));
//                scrollPane.setMinimumSize(new Dimension(tableWidth, 0));
            }else{
                scrollPane.getViewport().setPreferredSize(new Dimension(tableWidth,16*6+7));
            }

            frame.revalidate();

            //下の処理により、テーブル内の要素を削除したとき、背景画像が再読み込みされる(ただし、1行だけ、消されずにその部分は残る)
        bp.repaint();
        });

        //ユーザ入力用コンポーネントの作成

        // ボタンを作成
        addButton.addActionListener((ActionEvent e) -> {
            String input1 = toDoInputField1.getText();
            String input2 = toDoInputField2.getText();
            String input3 = toDoInputField3.getText();

            input2 = input2 + "/" + input3;
            Object[] newRow = {false, input1, input2};
            //新しい行をテーブルに追加
            tableModel.addRow(newRow);

            int tableWidth=(int) (contentPanel.getWidth() * 0.95);
            if(table.getRowCount()<=6) {
                scrollPane.setPreferredSize(new Dimension(tableWidth, 16 * (tableModel.getRowCount() + 1) + 7));
//                scrollPane.setMaximumSize(new Dimension(tableWidth, 16*6+7));
//                scrollPane.setMinimumSize(new Dimension(tableWidth, 0));
            }else{
                scrollPane.getViewport().setPreferredSize(new Dimension(tableWidth,16*6+7));
            }

            //データベースに値を挿入
            try {
//            ドライバの登録
                Class.forName("org.sqlite.JDBC");

//            DBへの接続
                Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");

//            ステートメントの作成
                Statement statement = connection.createStatement();

//            値を挿入するクエリ
                String insertQuery = "INSERT INTO todolist (is_done, Todo, deadline) VALUES ("
                        + false + ", '" + input1 + "', '" + input2 + "')";

                // クエリの実行
                statement.executeUpdate(insertQuery);

                System.out.println("A row is added without issues.");

                // ステートメントをクローズ
                statement.close();

                // コネクションをクローズ
                connection.close();

                toDoInputField1.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            tableWidth = (int) (contentPanel.getWidth() * 0.95);

            frame.revalidate();

        });


        bp.setBounds(0, 0,layeredPane.getWidth(), layeredPane.getHeight());
//        contentPanel.setBounds(0, 0, layeredPane.getPreferredSize().width, layeredPane.getPreferredSize().height);
        // JPanelのサイズを親コンポーネントやウィンドウのサイズに合わせる
        contentPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                int width = contentPanel.getWidth();
                int height = contentPanel.getHeight();
                bp.setBounds(0, 0, width, height);
            }
        });



        bp.setLayout(new BorderLayout());
//        layeredPane.setPreferredSize(new Dimension(800, 600)); // set the preferred size

        // パネルを作成し、背景画像を設定、bp=backgroundPanel、i=image
//        LoadImage bp = new LoadImage();



// add panels to the layered pane with appropriate layers
        layeredPane.setLayout(new BorderLayout());
        layeredPane.setLayer(bp,0);
        layeredPane.setLayer(contentPanel,1);
        layeredPane.add(bp);
        layeredPane.add(contentPanel); // コンテンツパネルをパレットレイヤーに配置
        contentPanel.setOpaque(false);
        panel2.setOpaque(false);
//        layeredPane.setOpaque(false);
        voidPane.setOpaque(false);
        table.setOpaque(false);
//        scrollPane.setOpaque(false);
        panelC.setOpaque(false);
        frame.add(layeredPane);
        System.out.println(layeredPane.getLayer(bp));
        System.out.println(layeredPane.getLayer(contentPanel));

        // create a frame and set the layered pane as its content pane
//        frame.setContentPane(bp);
//        frame.setContentPane(contentPanel);
        //adjust the size of the window of the UI of the app to the size of the components
        frame.pack();
        scrollPane.setPreferredSize(new Dimension((int) (contentPanel.getWidth()*0.95), (int) (contentPanel.getWidth()*0.2)));

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int tableWidth = (int) (contentPanel.getWidth() * 0.95);

                if(table.getRowCount()<=6) {
                    scrollPane.setPreferredSize(new Dimension(tableWidth, 16 * (tableModel.getRowCount() + 1) + 7));
//                scrollPane.setMaximumSize(new Dimension(tableWidth, 16*6+7));
//                scrollPane.setMinimumSize(new Dimension(tableWidth, 0));
                }else{
                    scrollPane.getViewport().setPreferredSize(new Dimension(tableWidth,16*6+7));
                }
                frame.revalidate(); // コンポーネントのレイアウトを再計算
            }
        });


//        contentPanel.setBounds(0,0,layeredPane.getWidth(),layeredPane.getHeight());
//        contentPanel.setOpaque(false);
//        bp.setBounds(0,0,layeredPane.getWidth(),layeredPane.getHeight());
//        bp.setOpaque(false);

        frame.setVisible(true);
    }





    //DBからテーブルデータを読み込みテーブルモデルにデータを格納する関数
    private void loadTableDataFromDatabase() {

        int rowCount=0;

        try {
            // ドライバの登録
            Class.forName("org.sqlite.JDBC");

            // DBへの接続
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");

            // ステートメントの作成→Statementオブジェクト：executeUpdate() や executeQuery() などのメソッドを提供し、DBを操作することができる
            Statement statement = connection.createStatement();

            // テーブルが存在しない場合は作成
            String createTableQuery = "CREATE TABLE IF NOT EXISTS todolist (" +
                    "is_done BOOLEAN, " +
                    "Todo VARCHAR(100), " +
                    "deadline VARCHAR(100)" +
                    ")";

            //Statementインスタンスを使用してDBに対して更新クエリを実行するためのメソッド→引数のSQL文を実行、DBに対してクエリを送信する
            statement.executeUpdate(createTableQuery);

            // テーブルのデータを取得
            String selectQuery = "SELECT * FROM todolist";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            // DBかラ読み込んだデータをresultSetから行ごとに取得し、逐次テーブルモデルにデータを追加。
            // 補足→nextメソッド：次の行が存在する場合に、trueを返し、カーソルを移動する
            while (resultSet.next()) {

                rowCount++;

                boolean isDone = resultSet.getBoolean("is_done");
                String todo = resultSet.getString("Todo");
                String deadline = resultSet.getString("deadline");
                Object[] rowData = {isDone, todo, deadline};

//                テーブルモデルに取得したデータを追加
                tableModel.addRow(rowData);
            }
            tableModel.setRowCount(rowCount);

            // リソースを解放→メモリ、ネットワーク接続、カーソルなど、プログラムの実行中に占有され、正しく解放されない場合、
            // リソースリーク、パフォーマンスの低下、予期しない動作などの問題が発生する可能性があるから!
            //　プログラムの正常な実行とリソースの効率的な利用を確保するために必要な処理
            resultSet.close();
            statement.close();  //クエリの一向に関連するメモリやリソースを解放、実行中のクエリがあれば中断。DBとの接続は残存
            connection.close();    //DBとの接続を切断し、それに関連するリソースを解放
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

class ImageLabel extends JLabel {
    private Image image;

    public ImageLabel(ImageIcon icon) {
        super(icon);
        this.image = icon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}



//テーブルのセルにチェックボックスをレンダリングするカスタムのセルレンダラークラス→セルの真偽値データに基づいてチェックボックスの選択状態が設定される
class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {

    //JCheckBoxはデフォルトで、セル内で左揃えになっているので、セル内の中央に配置するように指定、なぜかコンストラクタにこの処理だけが指定されてる
    public CheckBoxRenderer() {
        setHorizontalAlignment(JCheckBox.CENTER);
    }


    //JTableの各セルにはオブジェクトが割り当てられていて、そのオブジェクトがvalueパラメータに渡される、例えば、JTableが文字列データを表示している場合、
    //valueはString型のオブジェクトになる
    //TableCellRendererインターフェースのメソッド、JTableのセルを描写するために呼び出される
    //引数リスト→第一引数:描写されるセルが属するJTableオブジェクト、value：セルの値を表現するオブジェクト・セル内に表示されるデータの実際の値が渡される
    //isSeleceted:セルが選択されているかを示すブール値、hasFocus：セルがフォーカスを持っているか同かを示すブール値
    //セルが属する行のインデックス、column：セルが属する列のインデックス
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        boolean selected = (boolean) value;
        setSelected(selected);
        return this;
    }
}

class  CheckBoxEditor extends DefaultCellEditor {
    private final JCheckBox checkBox;
    private Cell currentCell;
    //the field to get the access to the worker(background-thread)
    private SharedSwingWorker BGT;


    // the constructor
    public CheckBoxEditor(SharedSwingWorker worker) {
        //DefaultCellEditorクラスのコンストラクタを呼び出し、チェックボックスをエディタのコンポーネントとして設定
        super(new JCheckBox());
        //getComponent()メソッドを呼び出して、チェックボックスコンポーネントを取得し、checkBox変数に代入
        checkBox = (JCheckBox) getComponent();
        checkBox.setHorizontalAlignment(JCheckBox.CENTER);
        //et the BGT field and get the access to the GBT
        this.BGT = worker;
    }

    //編集されたセルの値を取得するメソッド
    @Override
    public Object getCellEditorValue() {
        boolean selected = checkBox.isSelected();

        //編集中のセルの行インデックスを返す、編集中のセルがない場合、-1が返される
//        int row = table.getEditingRow();


        return selected;
    }

    //セルが編集されると呼び出される駆動型メソッド、セルが編集モードになる
    //引数リストについて→table:編集されているJTableオブジェクト、value：現在のセルの値、isSelected:、row:編集中のセルの行、column：編集中のセルの列
    //isSelected：編集中のセルが選択されているかどうかを示す、通常セルが選択されている場合true,いない場合falseが返される


    // ...

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        checkBox.
                setSelected((Boolean) value);

        boolean select = !((boolean) value);

        String todo = table.getValueAt(row, 1).toString();

        Cell cell = new Cell(select, row, todo);
        // Store the cell object for future reference
        // You'll need a field in the current class to hold this reference.
        this.currentCell = cell;

        System.out.println(row + "is editing.");
        BGT.addCell(currentCell);
//        worker.doInBackground();

        return checkBox;
    }
}




















