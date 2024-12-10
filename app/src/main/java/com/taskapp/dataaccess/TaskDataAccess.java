package com.taskapp.dataaccess;

import java.io.BufferedReader;
import java.io.BufferedWriter; // ファイル書き込みのためのクラス
import java.io.FileReader; // ファイル読み込みのためのクラス
import java.io.FileWriter; // ファイル書き込みのためのクラス
import java.io.IOException; // 入出力例外処理のためのクラス
import java.util.ArrayList;
import java.util.List;

import com.taskapp.model.Task;
import com.taskapp.model.User;

public class TaskDataAccess {

    private final String filePath; // タスクデータのCSVファイルのパス
    private final UserDataAccess userDataAccess; // ユーザーデータアクセス用のインスタンス

    public TaskDataAccess() {
        filePath = "app/src/main/resources/tasks.csv"; // デフォルトのCSVファイルパス
        userDataAccess = new UserDataAccess(); // デフォルトのUserDataAccessインスタンスを生成
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath CSVファイルパス
     * @param userDataAccess ユーザーデータアクセスインスタンス
     */
    public TaskDataAccess(String filePath, UserDataAccess userDataAccess) {
        this.filePath = filePath; // 指定されたCSVファイルパスを使用
        this.userDataAccess = userDataAccess; // 指定されたUserDataAccessインスタンスを使用
    }

    /**
     * 設問2追加
     * CSVから全てのタスクデータを取得します。
     *
     * @see com.taskapp.dataaccess.UserDataAccess#findByCode(int)
     * @return タスクのリスト
     */
    public List<Task> findAll() {
        // タスクのリストを格納するためのリストを初期化
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // ファイルから1行ずつ読み込むためのBufferedReaderを作成
            String line;
            reader.readLine(); // ヘッダー行をスキップ

            // ファイルの内容を1行ずつ読み込む
            while ((line = reader.readLine()) != null) {
                // 読み込んだ行をカンマで区切って分割する
                String[] values = line.split(",");
                // 必要な列数があるか確認する
                if (values.length == 4) {
                    // タスクコードを整数に変換
                    int code = Integer.parseInt(values[0].trim());
                    // タスク名を取得
                    String name = values[1].trim();
                    // ステータスを整数に変換
                    int status = Integer.parseInt(values[2].trim());
                    // 担当ユーザーコードを整数に変換
                    int repUserCode = Integer.parseInt(values[3].trim());

                    // 担当ユーザーコードを基にユーザー情報を取得
                    User repUser = userDataAccess.findByCode(repUserCode);
                    // 担当ユーザーが存在する場合のみタスクをリストに追加
                    if (repUser != null) {
                        tasks.add(new Task(code, name, status, repUser));
                    }
                }
            }
        } catch (IOException e) {
            // ファイル読み込み時の例外をキャッチしてスタックトレースを出力
            e.printStackTrace();
        }
        // タスクのリストを返す
        return tasks;
    }

    /**
     * タスクをCSVに保存します。
     * @param task 保存するタスク
     */
    public void save(Task task) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // CSVフォーマットでデータを書き込む
            String line = String.format("%d,%s,%d,%d", 
                task.getCode(), task.getName(), task.getStatus(), task.getRepUser().getCode());
            writer.write(line); // 書き込み
            writer.newLine(); // 改行
        } catch (IOException e) {
            // ファイル書き込み時の例外をキャッチしてスタックトレースを出力
            e.printStackTrace();
        }
    }

    /**
     * コードを基にタスクデータを1件取得します。
     * @param code 取得するタスクのコード
     * @return 取得したタスク
     */
    // public Task findByCode(int code) {
    //     try () {

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     return null;
    // }

    /**
     * タスクデータを更新します。
     * @param updateTask 更新するタスク
     */
    // public void update(Task updateTask) {
    //     try () {

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * コードを基にタスクデータを削除します。
     * @param code 削除するタスクのコード
     */
    // public void delete(int code) {
    //     try () {

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * タスクデータをCSVに書き込むためのフォーマットを作成します。
     * @param task フォーマットを作成するタスク
     * @return CSVに書き込むためのフォーマット文字列
     */
    // private String createLine(Task task) {
    // }
}