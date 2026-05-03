package dao;

import org.sql2o.Sql2o;

public class Conexao {
    
    private static Sql2o sql2o;

    public static Sql2o getSql2o() {
        if (sql2o == null) {
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPass = System.getenv("DB_PASS");
            sql2o = new Sql2o(dbUrl, dbUser, dbPass);
        }
        return sql2o;
    }
}