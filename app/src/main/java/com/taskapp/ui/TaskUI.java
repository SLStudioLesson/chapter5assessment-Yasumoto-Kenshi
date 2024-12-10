package com.taskapp.ui;

import java.io.BufferedReader;      // 標準入力を読み取るためのクラス
import java.io.IOException;         // 入出力例外を処理するためのクラス
import java.io.InputStreamReader;   // 標準入力ストリームを読み取るためのクラス
import com.taskapp.logic.TaskLogic; // タスクのロジックを管理するクラス
import com.taskapp.logic.UserLogic; // ユーザーのロジックを管理するクラス
import com.taskapp.model.User;      // ユーザーのデータモデルクラス

public class TaskUI {                    // タスク管理アプリケーションのユーザーインターフェースを提供するクラス
    private final BufferedReader reader; // 標準入力からのデータを読み取るためのBufferedReader
    private final UserLogic userLogic;   // ユーザー管理のロジックを操作するためのインスタンス
    private final TaskLogic taskLogic;   // タスク管理のロジックを操作するためのインスタンス
    private User loginUser;              // 現在ログインしているユーザー情報を保持する変数

    public TaskUI() {                                                   // デフォルトコンストラクタ
        reader = new BufferedReader(new InputStreamReader(System.in));  // 標準入力を初期化
        userLogic = new UserLogic();                                    // ユーザー管理ロジックのインスタンスを生成
        taskLogic = new TaskLogic();                                    // タスク管理ロジックのインスタンスを生成
    }

    public TaskUI(BufferedReader reader, UserLogic userLogic, TaskLogic taskLogic) { // コンストラクタ（引数付き）
        this.reader = reader;                                                        // 指定されたBufferedReaderを使用
        this.userLogic = userLogic;                                                  // 指定されたUserLogicを使用
        this.taskLogic = taskLogic;                                                  // 指定されたTaskLogicを使用
    }

    public void displayMenu() {                                        // メインメニューを表示するメソッド
        System.out.println("タスク管理アプリケーションにようこそ!!");   // 挨拶メッセージ
        inputLogin();                                                  // ログイン処理を実行
    
        boolean flg = true;     // メニューを繰り返し表示するためのフラグ
        while (flg) {           // メニューが継続されている間ループ
            try {
                System.out.println("以下1~3のメニューから好きな選択肢を選んでください。");   // メニュー案内
                System.out.println("1. タスク一覧, 2. タスク新規登録, 3. ログアウト");      // メニュー選択肢
                System.out.print("選択肢：");                                             // 入力プロンプトを表示
                String selectMenu = reader.readLine();                                     // ユーザー入力を読み取る
                System.out.println();                                                      // 改行を挿入

                switch (selectMenu) {                               // 入力に基づいて処理を分岐
                    case "1":                                       // タスク一覧の表示
                        taskLogic.showAll(loginUser);               // ログインユーザーのタスク一覧を表示
                        selectSubMenu();                            // サブメニューを表示
                        break;
                    case "2":                                       // 新規タスク登録
                        inputNewInformation();                      // 新しいタスクを登録
                        break;
                    case "3":                                       // ログアウト処理
                        System.out.println("ログアウトしました。"); // ログアウトメッセージ
                        flg = false;                                // フラグをfalseにしてループ終了
                        break;
                    default:                                        // 無効な選択肢の場合
                        // エラーメッセージ
                        System.out.println("選択肢が誤っています。1~3の中から選択してください。");
                        break;
                }
            } catch (IOException e) {   // 入力例外が発生した場合
                e.printStackTrace();    // エラー詳細を出力
            }
            System.out.println();       // メニュー間の間隔として改行を追加
        }
    }

    public void selectSubMenu() {                                                   // サブメニューを表示するメソッド
        while (true) {                                                              // サブメニューが継続されている間ループ
            System.out.println("以下1~2から好きな選択肢を選んでください。");          // サブメニュー案内
            System.out.println("1. タスクのステータス変更, 2. メインメニューに戻る"); // サブメニュー選択肢
            System.out.print("選択肢：");                                          // 入力プロンプトを表示
            try {
                String choice = reader.readLine();  // ユーザー入力を読み取る
                switch (choice) {                   // 入力に基づいて処理を分岐
                    case "1":                       // ステータス変更処理
                        inputChangeInformation();   // ステータス変更を実行
                        break;
                    case "2":                       // メインメニューに戻る
                        return;                     // サブメニューを終了
                    default:                        // 無効な選択肢の場合
                        System.out.println("選択肢が誤っています。1~2の中から選択してください。"); // エラーメッセージ
                }
            } catch (IOException e) {               // 入力例外が発生した場合
                e.printStackTrace();                // エラー詳細を出力
            }
        }
    }

    public void inputChangeInformation() {            // タスクのステータス変更を行うメソッド
        while (true) {                                // ステータス変更処理が継続されている間ループ
            try {
                // タスクコード入力プロンプト
                System.out.print("ステータスを変更するタスクコードを入力してください：");
                String codeInput = reader.readLine();                            // ユーザー入力を読み取る
                if (!isNumeric(codeInput)) {                                     // 入力が数値かチェック
                    System.out.println("コードは半角の数字で入力してください\n"); // エラーメッセージ
                    continue;                                                    // 再入力を要求
                }

                int code = Integer.parseInt(codeInput); // 入力を数値に変換

                System.out.println("どのステータスに変更するか選択してください。");   // ステータス選択案内
                System.out.println("1. 着手中, 2. 完了");                          // ステータス選択肢
                System.out.print("選択肢：");                                      // 入力プロンプトを表示
                String statusInput = reader.readLine();                             // ユーザー入力を読み取る
                if (!isNumeric(statusInput)) {                                      // 入力が数値かチェック
                    System.out.println("ステータスは半角の数字で入力してください\n"); // エラーメッセージ
                    continue;                                                       // 再入力を要求
                }

                int status = Integer.parseInt(statusInput);                          // 入力を数値に変換
                if (status != 1 && status != 2) {                                    // 有効なステータスかチェック
                    System.out.println("ステータスは1・2の中から選択してください\n"); // エラーメッセージ
                    continue;                                                        // 再入力を要求
                }

                taskLogic.changeStatus(code, status, loginUser);         // ステータス変更を実行
                System.out.println("ステータスの変更が完了しました。\n"); // 完了メッセージ
                break;                                                   // 処理終了
            } catch (Exception e) {                                      // 例外が発生した場合
                System.out.println(e.getMessage() + "\n");               // エラーメッセージを出力
            }
        }
    }

    public void inputLogin() {              // ログイン処理を行うメソッド
        boolean isLoginSuccessful = false;  // ログイン成功フラグを初期化

        while (!isLoginSuccessful) {        // ログインが成功するまでループ
            try {
                System.out.print("メールアドレスを入力してください："); // メールアドレス入力プロンプト
                String email = reader.readLine();                      // ユーザー入力を読み取る
                System.out.print("パスワードを入力してください：");     // パスワード入力プロンプト
                String password = reader.readLine();                   // ユーザー入力を読み取る

                try {
                    loginUser = userLogic.login(email, password);      // ログイン処理を実行
                    System.out.println("ユーザー名：" + loginUser.getName() + "でログインしました。\n"); // 成功メッセージ
                    isLoginSuccessful = true;                          // フラグをtrueに設定
                } catch (Exception e) {                                // ログイン例外が発生した場合
                    System.out.println(e.getMessage() + "\n");         // エラーメッセージを出力
                }
            } catch (IOException e) { // 入力例外が発生した場合
                e.printStackTrace();  // エラー詳細を出力
            }
        }
    }

    public void inputNewInformation() { // 新しいタスクを登録するメソッド
        while (true) {                  // タスク登録が継続されている間ループ
            try {
                System.out.print("タスクコードを入力してください：");             // タスクコード入力プロンプト
                String codeInput = reader.readLine();                            // ユーザー入力を読み取る
                if (!isNumeric(codeInput)) {                                     // 入力が数値かチェック
                    System.out.println("コードは半角の数字で入力してください\n"); // エラーメッセージ
                    continue;                                                    // 再入力を要求
                }

                int code = Integer.parseInt(codeInput);                             // 入力を数値に変換

                System.out.print("タスク名を入力してください：");                   // タスク名入力プロンプト
                String name = reader.readLine();                                    // ユーザー入力を読み取る
                if (name.length() > 10) {                                           // タスク名の長さをチェック
                    System.out.println("タスク名は10文字以内で入力してください\n");  // エラーメッセージ
                    continue; // 再入力を要求
                }

                System.out.print("担当するユーザーのコードを選択してください：");           // 担当ユーザーコード入力プロンプト
                String repUserCodeInput = reader.readLine();                              // ユーザー入力を読み取る
                if (!isNumeric(repUserCodeInput)) {                                       // 入力が数値かチェック
                    System.out.println("ユーザーのコードは半角の数字で入力してください\n"); // エラーメッセージ
                    continue; // 再入力を要求
                }

                int repUserCode = Integer.parseInt(repUserCodeInput);           // 入力を数値に変換

                taskLogic.save(code, name, repUserCode, loginUser);             // 新しいタスクを登録
                System.out.printf("%sの登録が完了しました。\n\n", name);   // 完了メッセージ
                break; // 処理終了
            } catch (Exception e) {                                             // 例外が発生した場合
                System.out.println(e.getMessage() + "\n");                      // エラーメッセージを出力
            }
        }
    }

    private boolean isNumeric(String inputText) {       // 入力が数値かどうかを判定するメソッド
        if (inputText == null || inputText.isEmpty()) { // 入力が空の場合
            return false;                               // 数値でないと判定
        }
        try {
            int value = Integer.parseInt(inputText); // 入力を整数に変換
            return value >= 0;                       // 正の数であればtrueを返す
        } catch (NumberFormatException e) {          // 数値変換例外が発生した場合
            return false;                            // 数値でないと判定
        }
    }
}