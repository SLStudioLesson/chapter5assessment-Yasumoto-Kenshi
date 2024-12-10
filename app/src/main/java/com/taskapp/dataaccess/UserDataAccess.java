package com.taskapp.dataaccess;

// 必要なライブラリをインポート
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.taskapp.model.User; // Userクラスをインポート

public class UserDataAccess {
    private final String filePath; // CSVファイルのパスを保持するフィールド

    // デフォルトコンストラクタ: ファイルパスを初期化
    public UserDataAccess() {
        this.filePath = "app/src/main/resources/users.csv"; // デフォルトのファイルパスを設定
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     *
     * @param filePath CSVファイルのパス
     */
    public UserDataAccess(String filePath) {
        this.filePath = filePath; // 指定されたファイルパスをフィールドに設定
    }

    /**
     * メールアドレスとパスワードを基にユーザーデータを探します。
     *
     * @param email    メールアドレス
     * @param password パスワード
     * @return 見つかったユーザー
     */
    public User findByEmailAndPassword(String email, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line; // 読み取った行を保持する変数
            reader.readLine(); // ヘッダー行をスキップ
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(","); // カンマ区切りで文字列を分割
                if (values.length == 4) { // 必要な列数を確認
                    String csvEmail = values[2].trim(); // メールアドレスを取得
                    String csvPassword = values[3].trim(); // パスワードを取得
                    if (csvEmail.equals(email) && csvPassword.equals(password)) {
                        int code = Integer.parseInt(values[0].trim()); // コードを整数に変換
                        String name = values[1].trim(); // ユーザー名を取得
                        return new User(code, name, csvEmail, csvPassword); // ユーザーを返す
                    }
                } else {
                    System.err.printf("Invalid row format: %s%n", line); // 行フォーマットが無効な場合にエラーメッセージを出力
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath); // ファイル読み取りエラーを通知
            e.printStackTrace(); // 詳細なエラー情報を表示
        } catch (NumberFormatException e) {
            System.err.println("Error parsing numeric data in file: " + filePath); // 数値変換エラーを通知
            e.printStackTrace();
        }
        return null; // 一致するユーザーが見つからない場合はnullを返す
    }

    /**
     * コードを基にユーザーデータを取得します。
     *
     * @param code 取得するユーザーのコード
     * @return 見つかったユーザー
     */
    public User findByCode(int code) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line; // 読み取った行を保持する変数
            reader.readLine(); // ヘッダー行をスキップ
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(","); // カンマ区切りで文字列を分割
                if (values.length == 4) { // 必要な列数を確認
                    int userCode = Integer.parseInt(values[0].trim()); // ユーザーコードを取得
                    if (userCode == code) { // コードが一致する場合
                        String name = values[1].trim(); // ユーザー名を取得
                        String email = values[2].trim(); // メールアドレスを取得
                        String password = values[3].trim(); // パスワードを取得
                        return new User(userCode, name, email, password); // ユーザーを返す
                    }
                } else {
                    System.err.printf("Invalid row format: %s%n", line); // 行フォーマットが無効な場合にエラーメッセージを出力
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath); // ファイル読み取りエラーを通知
            e.printStackTrace(); // 詳細なエラー情報を表示
        } catch (NumberFormatException e) {
            System.err.println("Error parsing numeric data in file: " + filePath); // 数値変換エラーを通知
            e.printStackTrace();
        }
        return null; // 一致するユーザーが見つからない場合はnullを返す
    }
}