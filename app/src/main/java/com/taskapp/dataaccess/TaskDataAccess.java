package com.taskapp.dataaccess;

import java.io.BufferedReader;  // ファイル読み込みのためのクラス
import java.io.BufferedWriter;  // ファイル書き込みのためのクラス
import java.io.FileReader;      // ファイルを読み取るためのクラス
import java.io.FileWriter;      // ファイルを書き込むためのクラス
import java.io.IOException;     // 入出力例外を処理するためのクラス
import java.util.ArrayList;     // 動的配列のためのクラス
import java.util.List;          // リスト型データ構造のためのインターフェース

import com.taskapp.model.Task; // タスクモデルクラスをインポート
import com.taskapp.model.User; // ユーザーモデルクラスをインポート

// タスクデータアクセスを行うクラス
public class TaskDataAccess {

    private final String filePath; // タスクデータのCSVファイルパスを保持
    private final UserDataAccess userDataAccess; // ユーザーデータアクセス用のインスタンス

    // デフォルトコンストラクタ: デフォルトのファイルパスとUserDataAccessを使用
    public TaskDataAccess() {
        filePath = "app/src/main/resources/tasks.csv"; // デフォルトのCSVファイルパスを設定
        userDataAccess = new UserDataAccess(); // ユーザーデータアクセスインスタンスを生成
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath CSVファイルパス
     * @param userDataAccess ユーザーデータアクセスインスタンス
     */
    public TaskDataAccess(String filePath, UserDataAccess userDataAccess) {
        this.filePath = filePath; // 指定されたファイルパスを使用
        this.userDataAccess = userDataAccess; // 指定されたユーザーデータアクセスを使用
    }

    /**
     * CSVから全てのタスクデータを取得します。
     * @return タスクのリスト
     */
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>(); // タスクのリストを格納するための動的配列を初期化
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line; // ファイルの1行を保持する変数
            reader.readLine(); // ヘッダー行をスキップ

            while ((line = reader.readLine()) != null) {                    // ファイルの内容を1行ずつ読み込む
                String[] values = line.split(",");                    // 行をカンマで区切って分割する
                if (values.length == 4) {                                   // 必要な列数（4列）があるか確認
                    int code = Integer.parseInt(values[0].trim());          // タスクコードを整数に変換
                    String name = values[1].trim();                         // タスク名を取得
                    int status = Integer.parseInt(values[2].trim());        // ステータスを整数に変換
                    int repUserCode = Integer.parseInt(values[3].trim());   // 担当ユーザーコードを整数に変換
                    User repUser = userDataAccess.findByCode(repUserCode);  // 担当ユーザーコードを基にユーザー情報を取得

                    if (repUser != null) { // 担当ユーザーが存在する場合のみタスクをリストに追加
                        tasks.add(new Task(code, name, status, repUser)); // タスクリストに追加
                    }
                }
            }
        } catch (IOException e) { // ファイル読み込み時の例外を処理
            e.printStackTrace(); // スタックトレースを出力
        }
        return tasks; // タスクのリストを返す
    }

    /**
     * タスクをCSVに保存します。
     * @param task 保存するタスク
     */
    public void save(Task task) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String line = String.format("%d,%s,%d,%d", 
                task.getCode(), task.getName(), task.getStatus(), task.getRepUser().getCode()); // タスク情報をフォーマット
            writer.write(line);   // データを書き込む
            writer.newLine();     // 改行
        } catch (IOException e) { // ファイル書き込み時の例外を処理
            e.printStackTrace();  // スタックトレースを出力
        }
    }

    /**
     * 指定されたタスクコードに対応するタスクを検索します。
     * @param code タスクコード
     * @return 該当するタスク、存在しない場合は null
     */
    public Task findByCode(int code) {
        List<Task> tasks = findAll();       // 全てのタスクを取得
        for (Task task : tasks) {           // 各タスクを確認
            if (task.getCode() == code) {   // コードが一致するタスクを返す
                return task;
            }
        }
        return null; // 該当するタスクが存在しない場合はnullを返す
    }

    /**
     * タスクデータを更新します。
     * @param updateTask 更新するタスク
     */
    public void update(Task updateTask) {
        List<Task> tasks = findAll(); // 全てのタスクを取得
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Code,Name,Status,RepUserCode"); // ヘッダー行を書き込む
            writer.newLine(); // 改行
            for (Task task : tasks) { // 各タスクを確認
                if (task.getCode() == updateTask.getCode()) { // 更新対象のタスクか確認
                    task = updateTask; // タスクのデータを更新
                }
                writer.write(String.format("%d,%s,%d,%d", task.getCode(), task.getName(), task.getStatus(), task.getRepUser().getCode())); // タスク情報をCSVフォーマットで書き込む
                writer.newLine(); // 改行
            }
        } catch (IOException e) { // ファイル書き込み時の例外を処理
            e.printStackTrace(); // スタックトレースを出力
        }
    }
}