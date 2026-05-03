package dao;

import org.sql2o.Sql2o;

public class Conexao {
    
    private static Sql2o sql2o;

    public static Sql2o getSql2o() {
        if (sql2o == null) {
            // Tenta pegar as configurações da Nuvem
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPass = System.getenv("DB_PASS");

            if (dbUrl != null) {

                sql2o = new Sql2o(dbUrl, dbUser, dbPass);
            } else {
                sql2o = new Sql2o("jdbc:postgresql://localhost:5432/saas_raiox", "postgres", "123456");
            }
        }
        return sql2o;
    }
}