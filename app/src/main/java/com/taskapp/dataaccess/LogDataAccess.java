package com.taskapp.dataaccess;

import java.io.BufferedWriter;      // ファイル書き込みのためのクラス
import java.io.FileWriter;          // ファイル書き込みのためのクラス
import java.io.IOException;         // 入出力例外処理のためのクラス
import java.util.ArrayList;         // リスト操作のためのクラス
import java.util.List;              // リストを利用するためのクラス
import com.taskapp.model.Log;       // Logモデルクラスをインポート

public class LogDataAccess {
    // ログデータのCSVファイルのパス
    private final String filePath;

    // デフォルトコンストラクタ
    public LogDataAccess() {
        // デフォルトのCSVファイルパス
        filePath = "app/src/main/resources/logs.csv";
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath CSVファイルパス
     */
    public LogDataAccess(String filePath) {
        // 指定されたCSVファイルパスを使用
        this.filePath = filePath;
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
    public List<Log> findAll() {
        List<Log> logs = new ArrayList<>(); // ログのリストを初期化
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line; // 読み取り用変数
            reader.readLine(); // ヘッダー行をスキップ

            while ((line = reader.readLine()) != null) { // 各行を読み込む
                String[] values = line.split(","); // カンマで分割
                if (values.length == 4) { // 必要な列数を確認
                    int taskCode = Integer.parseInt(values[0].trim());
                    int changeUserCode = Integer.parseInt(values[1].trim());
                    int status = Integer.parseInt(values[2].trim());
                    String changeDate = values[3].trim();

                    // ログオブジェクトを生成してリストに追加
                    logs.add(new Log(taskCode, changeUserCode, status, changeDate));
                }
            }
        } catch (IOException e) {
            // ファイル読み込み時の例外をキャッチしてスタックトレースを出力
            e.printStackTrace();
        }
        return logs; // ログのリストを返す
    }

    /**
     * 指定したタスクコードに該当するログを削除します。
     *
     * @param taskCode 削除するログのタスクコード
     */
    public void deleteByTaskCode(int taskCode) {
        List<Log> logs = findAll(); // すべてのログを取得
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("TaskCode,ChangeUserCode,Status,ChangeDate"); // ヘッダー行を書き込む
            writer.newLine(); // 改行を追加
            for (Log log : logs) {
                if (log.getTaskCode() != taskCode) { // 削除対象でないログを保持
                    writer.write(String.format("%d,%d,%d,%s", 
                        log.getTaskCode(), log.getChangeUserCode(), log.getStatus(), log.getChangeDate()));
                    writer.newLine(); // 改行を追加
                }
            }
        } catch (IOException e) {
            // ファイル書き込み時の例外をキャッチしてスタックトレースを出力
            e.printStackTrace();
        }
    }
}