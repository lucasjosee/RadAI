package dao;

import org.sql2o.Connection;

public class ExameDao {
    public static void salvarResultado(int consultaId, String nomeArquivo, String resultado, double confianca, boolean revisao) {
        String sql = "INSERT INTO exames (consulta_id, nome_arquivo, resultado_ia, confianca, precisa_revisao) " +
                     "VALUES (:cid, :nome, :res, :conf, :rev)";
        
        try (Connection con = Conexao.getSql2o().open()) {
            con.createQuery(sql)
                .addParameter("cid", consultaId)
                .addParameter("nome", nomeArquivo)
                .addParameter("res", resultado)
                .addParameter("conf", confianca)
                .addParameter("rev", revisao)
                .executeUpdate();
        }
        
        // Atualiza o status da consulta também
        String sqlUpdate = "UPDATE consultas SET diagnostico_ia = :res, status = 'FINALIZADA' WHERE id = :id";
        try (Connection con = Conexao.getSql2o().open()) {
            con.createQuery(sqlUpdate)
                .addParameter("res", resultado)
                .addParameter("id", consultaId)
                .executeUpdate();
        }
    }
}