package dao;

import org.sql2o.Connection;
import model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDao {
    
	public static void inserir(Usuario u) {
        String senhaHash = BCrypt.hashpw(u.senha, BCrypt.gensalt());
        u.senha = senhaHash; // Substitui a senha original pelo Hash

        String sql = "INSERT INTO usuarios (nome, email, senha, tipo, documento_profissional) " +
                     "VALUES (:nome, :email, :senha, :tipo, :documentoProfissional)";
        
        try (Connection con = Conexao.getSql2o().open()) {
            con.createQuery(sql)
                .bind(u)
                .executeUpdate();
        }
    }

    public static Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = :email";
        
        try (Connection con = Conexao.getSql2o().open()) {
            return con.createQuery(sql)
                .addParameter("email", email)
                .addColumnMapping("documento_profissional", "documentoProfissional")
                .executeAndFetchFirst(Usuario.class);
        } catch (Exception e) {
            System.out.println("ERRO CRÍTICO NO BANCO: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}