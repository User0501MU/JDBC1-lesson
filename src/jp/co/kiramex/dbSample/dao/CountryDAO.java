//3,DAOの役割を担う CountryDAO クラスを作成しましょう

package jp.co.kiramex.dbSample.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.kiramex.dbSample.entity.Country;
import jp.co.kiramex.dbSample.util.DatabaseManager;

public class CountryDAO {
    // データベース接続と結果取得のための変数
    private PreparedStatement pstmt;
    private ResultSet rs;

    /*
     * 検索結果に合致するCountryオブジェクトリストを取得するメソッド
     */
    public List<Country> getCountryFromName(String name) {
        // メソッドの結果として返すリスト
        List<Country> results = new ArrayList<Country>();

        try {
            // 1,2. ドライバを読み込み、DBに接続
            Connection con = DatabaseManager.getConnection();

            // 3. DBとやりとりする窓口（Statementオブジェクト）の作成
            String sql = "select * from country where Name = ?";
            pstmt = con.prepareStatement(sql);

            // 4, 5. Select文の実行と結果を格納／代入
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();

            // 6. 結果を表示する
            while (rs.next()) {
                // 1件ずつCountryオブジェクトを生成して結果を詰める
                Country country = new Country();
                country.setName(rs.getString("Name"));
                country.setPopulation(rs.getInt("Population"));

                // リストに追加
                results.add(country);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            if( rs != null ){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if( pstmt != null ){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DatabaseManager.close();
        }
        return results;
    }
}