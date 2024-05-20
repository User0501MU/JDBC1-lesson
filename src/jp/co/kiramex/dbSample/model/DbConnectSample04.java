//動的なSQLを利用する際は、必ずプリペアードステートメントを活用
//SQLインジェクションが実行可能な状態のままでは、データベースに保存されているデータがすべて取得できるので個人情報の漏洩につながります。
//SQLインジェクション対策になる方法
//' or 'a' = 'a実行しても表示されない
//PreparedStatementは executeUpdate()メソッド（更新系）でも使えます。05

package jp.co.kiramex.dbSample.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;  // ← 修正
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnectSample04 {

    public static void main(String[] args) {
        // 3. データベース接続と結果取得のための変数宣言
        Connection con = null;
        PreparedStatement pstmt = null; // ← 修正
        ResultSet rs = null;

        try {
            // 1. ドライバのクラスをJava上で読み込む
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. DBと接続する
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/world?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "20240501Umonica3!!");

            // 4. DBとやりとりする窓口（PreparedStatementオブジェクト）の作成
            String sql = "SELECT * FROM country WHERE Name = ?";    // ← 修正
            pstmt = con.prepareStatement(sql);  // ← 修正

            // 5, 6. Select文の実行と結果を格納／代入
            System.out.print("検索キーワードを入力してください > ");
            String input = keyIn();

            // PreparedStatementオブジェクトの?に値をセット  // ← 追記
            pstmt.setString(1, input);  // ← 追記

            rs = pstmt.executeQuery();  // ← 修正

            // 7. 結果を表示する
            while (rs.next()) {
                // Name列の値を取得
                String name = rs.getString("Name");
                // Population列の値を取得 ← 追記
                int population = rs.getInt("Population");

                // 取得した値を表示
                System.out.println(name);
                System.out.println(population);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("JDBCドライバのロードに失敗しました。");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("データベースに異常が発生しました。");
            e.printStackTrace();
        } finally {
            // 8. 接続を閉じる
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("ResultSetを閉じるときにエラーが発生しました。");
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {    // ← 修正
                try {
                    pstmt.close();    // ← 修正
                } catch (SQLException e) {
                    System.err.println("PreparedStatementを閉じるときにエラーが発生しました。");    // ← 修正
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("データベース切断時にエラーが発生しました。");
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * キーボードから入力された値をStringで返す 引数：なし 戻り値：入力された文字列
     */
    private static String keyIn() {
        String line = null;
        try {
            BufferedReader key = new BufferedReader(new InputStreamReader(System.in));
            line = key.readLine();
        } catch (IOException e) {

        }
        return line;
    }

}