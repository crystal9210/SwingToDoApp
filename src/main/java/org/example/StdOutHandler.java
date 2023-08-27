package org.example;

import java.util.logging.Level;
import java.util.logging.StreamHandler;

class StdOutHandler extends StreamHandler {
    {
        // 標準出力への出力設定
        setOutputStream(System.out);
        // ログレベルの設定（ALLに設定しないとログレベルをコントロールできない）
        setLevel(Level.ALL);
    }
}
