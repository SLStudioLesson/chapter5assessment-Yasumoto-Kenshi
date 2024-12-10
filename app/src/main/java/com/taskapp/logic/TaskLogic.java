package com.taskapp.logic;

import java.time.LocalDate;
import java.util.List;

import com.taskapp.dataaccess.LogDataAccess;
import com.taskapp.dataaccess.TaskDataAccess;
import com.taskapp.dataaccess.UserDataAccess;
import com.taskapp.exception.AppException;
import com.taskapp.model.Log;
import com.taskapp.model.Task;
import com.taskapp.model.User;

public class TaskLogic {
    private final TaskDataAccess taskDataAccess; // タスクデータへのアクセスを管理するオブジェクト

    private final LogDataAccess logDataAccess; // ログデータへのアクセスを管理するオブジェクト

    private final UserDataAccess userDataAccess; // ユーザーデータへのアクセスを管理するオブジェクト

    public TaskLogic() { // デフォルトコンストラクタ
        taskDataAccess = new TaskDataAccess(); // TaskDataAccessオブジェクトを初期化

        logDataAccess = new LogDataAccess(); // LogDataAccessオブジェクトを初期化

        userDataAccess = new UserDataAccess(); // UserDataAccessオブジェクトを初期化
    }

    // 他のコンポーネントを外部から受け取るコンストラクタ
    public TaskLogic(TaskDataAccess taskDataAccess, LogDataAccess logDataAccess, UserDataAccess userDataAccess) {
        this.taskDataAccess = taskDataAccess; // 渡されたTaskDataAccessをフィールドに設定

        this.logDataAccess = logDataAccess; // 渡されたLogDataAccessをフィールドに設定

        this.userDataAccess = userDataAccess; // 渡されたUserDataAccessをフィールドに設定
    }

    /**
     * 全てのタスクを表示します。
     * 設問2追加
     * @see com.taskapp.dataaccess.TaskDataAccess#findAll()
     * @param loginUser ログインユーザー
     */
    public void showAll(User loginUser) { // タスクの一覧を取得する
        List<Task> tasks = taskDataAccess.findAll(); // 表示するタスクの番号を管理する変数
        int index = 1;

        // 取得したタスクを1件ずつ処理する
        for (Task task : tasks) {
            // タスクのステータスを文字列に変換する
            String taskStatus = switch (task.getStatus()) {
                case 0 -> "未着手";
                // ステータスが0の場合は"未着手"
                case 1 -> "着手中";
                // ステータスが1の場合は"着手中"
                case 2 -> "完了";
                // ステータスが2の場合は"完了"
                default -> "不明";
                // ステータスがそれ以外の場合は"不明"
            };

            // タスクの担当者がログインユーザーかどうかを判定
            String assignee = task.getRepUser().equals(loginUser)
                    ? "あなたが担当しています"
                    // ログインユーザーが担当者の場合
                    : task.getRepUser().getName();
                    // それ以外の場合は担当者の名前を取得

            // タスク情報をフォーマットしてコンソールに出力する
            System.out.printf("%d. タスク名：%s, 担当者名：%s, ステータス：%s%n",
                    index++, task.getName(), assignee, taskStatus);
        }
    }

    /**
     * 新しいタスクを保存します。
     *
     * @see com.taskapp.dataaccess.UserDataAccess#findByCode(int)
     * @see com.taskapp.dataaccess.TaskDataAccess#save(com.taskapp.model.Task)
     * @see com.taskapp.dataaccess.LogDataAccess#save(com.taskapp.model.Log)
     * @param code        タスクコード
     * @param name        タスク名
     * @param repUserCode 担当ユーザーコード
     * @param loginUser   ログインユーザー
     * @throws AppException ユーザーコードが存在しない場合にスローされます
     */
    public void save(int code, String name, int repUserCode, User loginUser) throws AppException {
        // 担当者のユーザーが存在するか確認
        User repUser = userDataAccess.findByCode(repUserCode);
        if (repUser == null) {
            // ユーザーが見つからない場合例外をスロー
            throw new AppException("存在するユーザーコードを入力してください");
        }

        // 新しいタスクの作成
        Task task = new Task(code, name, 0, repUser);

        // タスクデータを保存
        taskDataAccess.save(task);

        // ログデータの作成
        Log log = new Log(code, loginUser.getCode(), 0, LocalDate.now());
        logDataAccess.save(log);
    }

    /**
     * タスクのステータスを変更します。
     *
     * @see com.taskapp.dataaccess.TaskDataAccess#findByCode(int)
     * @see com.taskapp.dataaccess.TaskDataAccess#update(com.taskapp.model.Task)
     * @see com.taskapp.dataaccess.LogDataAccess#save(com.taskapp.model.Log)
     * @param code      タスクコード
     * @param status    新しいステータス
     * @param loginUser ログインユーザー
     * @throws AppException タスクコードが存在しない、またはステータスが前のステータスより1つ先でない場合にスローされます
     */
    public void changeStatus(int code, int status, User loginUser) throws AppException {
    //タスクを検索
    Task task = taskDataAccess.findByCode(code);
    if (task == null) {
        throw new AppException("存在するタスクコードを入力してください");
    }

    // 現在のステータスが変更後のステータスの1つ前であることを確認
    if ((task.getStatus() == 0 && status != 1) || (task.getStatus() == 1 && status != 2)) {
        throw new AppException("ステータスは、前のステータスより1つ先のもののみを選択してください");
    }

    // ステータスの更新
    task.setStatus(status);
    taskDataAccess.update(task);

    // ログの記録
    Log log = new Log(task.getCode(), loginUser.getCode(), status, LocalDate.now());
    logDataAccess.save(log);
}

    /**
     * タスクを削除します。
     *
     * @see com.taskapp.dataaccess.TaskDataAccess#findByCode(int)
     * @see com.taskapp.dataaccess.TaskDataAccess#delete(int)
     * @see com.taskapp.dataaccess.LogDataAccess#deleteByTaskCode(int)
     * @param code タスクコード
     * @throws AppException タスクコードが存在しない、またはタスクのステータスが完了でない場合にスローされます
     */
    // public void delete(int code) throws AppException {
    // }
}