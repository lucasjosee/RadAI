package dao;

import org.sql2o.Connection;
import model.Consulta;
import java.util.List;

public class ConsultaDao {
    
    public static void agendar(Consulta c) {
        String sql = "INSERT INTO consultas (paciente_id, data_hora, sintomas) " +
                     "VALUES (:paciente_id, :data_hora, :sintomas)";
        
        try (Connection con = Conexao.getSql2o().open()) {
            con.createQuery(sql).bind(c).executeUpdate();
        }
    }

    public static List<Consulta> listarTodas() {
        String sql = "SELECT * FROM consultas ORDER BY id DESC";
        try (Connection con = Conexao.getSql2o().open()) {
            return con.createQuery(sql).executeAndFetch(Consulta.class);
        }
    }
}