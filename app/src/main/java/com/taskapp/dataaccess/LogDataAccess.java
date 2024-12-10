package com.taskapp.dataaccess;

import java.io.BufferedWriter; // ファイル書き込みのためのクラス
import java.io.FileWriter; // ファイル書き込みのためのクラス
import java.io.IOException; // 入出力例外処理のためのクラス
import java.util.List;

import com.taskapp.model.Log; // Logモデルクラスをインポート

public class LogDataAccess {
    private final String filePath; // ログデータのCSVファイルのパス

    // デフォルトコンストラクタ
    public LogDataAccess() {
        filePath = "app/src/main/resources/logs.csv"; // デフォルトのCSVファイルパス
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath CSVファイルパス
     */
    public LogDataAccess(String filePath) {
        this.filePath = filePath; // 指定されたCSVファイルパスを使用
    }

    /**
     * ログをCSVファイルに保存します。
     *
     * @param log 保存するログ
     */
    public void save(Log log) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // CSVフォーマットでデータを書き込む
            String line = String.format("%d,%d,%d,%s", 
                log.getTaskCode(), log.getChangeUserCode(), log.getStatus(), log.getChangeDate().toString());
            writer.write(line); // データを書き込む
            writer.newLine(); // 改行を追加
        } catch (IOException e) {
            // ファイル書き込み時の例外をキャッチしてスタックトレースを出力
            e.printStackTrace();
        }
    }

    /**
     * すべてのログを取得します。
     *
     * @return すべてのログのリスト
     */
    // public List<Log> findAll() {
    //     try () {

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     return null;
    // }

    /**
     * 指定したタスクコードに該当するログを削除します。
     *
     * @see #findAll()
     * @param taskCode 削除するログのタスクコード
     */
    // public void deleteByTaskCode(int taskCode) {
    //     try () {

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * ログをCSVファイルに書き込むためのフォーマットを作成します。
     *
     * @param log フォーマットを作成するログ
     * @return CSVファイルに書き込むためのフォーマット
     */
    // private String createLine(Log log) {
    // }
}