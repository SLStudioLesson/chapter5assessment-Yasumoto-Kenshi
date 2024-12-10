package com.taskapp.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.taskapp.logic.TaskLogic;
import com.taskapp.logic.UserLogic;
import com.taskapp.model.User;

public class TaskUI {
    private final BufferedReader reader;

    private final UserLogic userLogic;

    private final TaskLogic taskLogic;

    private User loginUser;

    public TaskUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        userLogic = new UserLogic();
        taskLogic = new TaskLogic();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param reader
     * @param userLogic
     * @param taskLogic
     */
    public TaskUI(BufferedReader reader, UserLogic userLogic, TaskLogic taskLogic) {
        this.reader = reader;
        this.userLogic = userLogic;
        this.taskLogic = taskLogic;
    }

    /**
     * メニューを表示し、ユーザーの入力に基づいてアクションを実行します。
     *
     * @see #inputLogin()
     * @see com.taskapp.logic.TaskLogic#showAll(User)
     * @see #selectSubMenu()
     * @see #inputNewInformation()
     */
    public void displayMenu() {
        System.out.println("タスク管理アプリケーションにようこそ!!");
    
        // ログイン処理を実行
        inputLogin();
    
        // メインメニュー
        boolean flg = true;
        while (flg) {
            try {
                System.out.println("以下1~3のメニューから好きな選択肢を選んでください。");
                System.out.println("1. タスク一覧, 2. タスク新規登録, 3. ログアウト");
                System.out.print("選択肢：");
                String selectMenu = reader.readLine();
    
                System.out.println();
    
                switch (selectMenu) {
                    case "1":
                        taskLogic.showAll(loginUser); // タスク一覧表示
                        break;
                    case "2":
                        inputNewInformation(); // タスク新規登録機能を呼び出す
                        break;
                    case "3":
                        System.out.println("ログアウトしました。");
                        flg = false;
                        break;
                    default:
                        System.out.println("選択肢が誤っています。1~3の中から選択してください。");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    /**
     * ユーザーからのログイン情報を受け取り、ログイン処理を行います。
     *
     * @see com.taskapp.logic.UserLogic#login(String, String)
     */
    public void inputLogin() {
        boolean isLoginSuccessful = false;
    
        while (!isLoginSuccessful) {
            try {
                System.out.print("メールアドレスを入力してください：");
                String email = reader.readLine();
                System.out.print("パスワードを入力してください：");
                String password = reader.readLine();
    
                try {
                    // ログイン成功時に loginUser を設定
                    loginUser = userLogic.login(email, password);
                    System.out.println("ユーザー名：" + loginUser.getName() + "でログインしました。\n");
                    isLoginSuccessful = true;
                } catch (Exception e) {
                    System.out.println(e.getMessage() + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ユーザーからの新規タスク情報を受け取り、新規タスクを登録します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#save(int, String, int, User)
     */
    public void inputNewInformation() {
        while (true) {
            try {
                System.out.print("タスクコードを入力してください：");
                String codeInput = reader.readLine();
                if (!isNumeric(codeInput)) {
                    System.out.println("コードは半角の数字で入力してください\n");
                    continue;
                }

                int code = Integer.parseInt(codeInput);

                System.out.print("タスク名を入力してください：");
                String name = reader.readLine();
                if (name.length() > 10) {
                    System.out.println("タスク名は10文字以内で入力してください\n");
                    continue;
                }

                System.out.print("担当するユーザーのコードを選択してください：");
                String repUserCodeInput = reader.readLine();
                if (!isNumeric(repUserCodeInput)) {
                    System.out.println("ユーザーのコードは半角の数字で入力してください\n");
                    continue;
                }

                int repUserCode = Integer.parseInt(repUserCodeInput);

                // タスク登録
                taskLogic.save(code, name, repUserCode, loginUser);
                System.out.printf("%sの登録が完了しました。\n\n", name);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage() + "\n");
            }
        }
    }

    /**
     * 指定された文字列が数値であるかどうかを判定します。
     * 負の数は判定対象外とする。
     *
     * @param inputText 判定する文字列
     * @return 数値であればtrue、そうでなければfalse
     */
    private boolean isNumeric(String inputText) {
        if (inputText == null || inputText.isEmpty()) {
            return false; // nullや空文字の場合はfalseを返す
        }
        try {
            int value = Integer.parseInt(inputText); // 整数に変換を試みる
            return value >= 0; // 負の値でなければtrueを返す
        } catch (NumberFormatException e) {
            return false; // 数字に変換できない場合はfalseを返す
        }
    }
}