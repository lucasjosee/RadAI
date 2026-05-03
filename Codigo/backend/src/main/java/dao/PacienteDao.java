package dao;

import org.sql2o.Connection;
import model.Paciente;
import java.util.List;

public class PacienteDao {
    
    public static void inserir(Paciente p) {
        String sql = "INSERT INTO pacientes (nome, email, cpf, telefone, nascimento, endereco) " +
                     "VALUES (:nome, :email, :cpf, :telefone, :nascimento, :endereco)";
        
        try (Connection con = Conexao.getSql2o().open()) {
            con.createQuery(sql).bind(p).executeUpdate();
        }
    }

    public static List<Paciente> listarTodos() {
        String sql = "SELECT * FROM pacientes";
        try (Connection con = Conexao.getSql2o().open()) {
            return con.createQuery(sql).executeAndFetch(Paciente.class);
        }
    }
}