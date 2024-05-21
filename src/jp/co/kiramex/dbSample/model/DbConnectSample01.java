package jp.co.kiramex.dbSample.model;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;//修正

public class DbConnectSample01 {

    public static void main(String[] args) {
        // 3. データベース接続と結果取得のための変数宣言
        Connection con = null;
        PreparedStatement pstmt = null;//修正
        ResultSet rs = null;


        try {
            //ドライバをプロジェクトに取り込む★
            // 1. ドライバのクラスをJava上で読み込む
            Class.forName("com.mysql.cj.jdbc.Driver");
            //try-with-resources構文
            // 2. DBと接続する
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/world?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "20240501Umonica3!!");
            // 4. DBとやりとりする窓口（Statementオブジェクト）の作成
            String sql = "SELECT * FROM country WHERE Name = ?";    // ← 修正
            pstmt = con.prepareStatement(sql);  // ← 修正


            // 5, 6. Select文の実行と結果を格納／代入
            System.out.print("検索キーワードを入力してください > ");
            String input = keyIn();

            // PreparedStatementオブジェクトの?に値をセット  // ← 追記
            pstmt.setString(1, input);  // ← 追記

            rs = pstmt.executeQuery();  // ← 修正

            // 7. 結果を表示する
            while( rs.next() ){//rs.next()表の中に「次の行のデータ」があるかどうかを指し示す(1行ずつ処理することが必須)
                // Name列の値を取得
                String name = rs.getString("Name");//数値を取り出したいときrs.getString("カラム名")/文字列を取り出したいときは、getString("取り出したいカラム名")
                // Population列の値を取得 　← 追記
                int population = rs.getInt("Population");  // ← 追記
                // 取得した値を表示
                System.out.println(name);
                System.out.println(population);   // ← 追記
            }


        } catch (ClassNotFoundException e) {
            System.err.println("JDBCドライバのロードに失敗しました。");
            e.printStackTrace();
        }

        catch (SQLException e) {
            System.err.println("データベースに異常が発生しました。");
            e.printStackTrace();
        } finally {
            // 8. 接続を閉じる
            if (pstmt != null) {//修正
                try {
                    pstmt.close();//修正
                } catch (SQLException e) {
                    System.err.println("ResultSetを閉じるときにエラーが発生しました。");
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("Statementを閉じるときにエラーが発生しました。");
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
     * キーボードから入力された値をStringで返す 引数：なし 戻り値：入力された文字列    // ← 追記
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
