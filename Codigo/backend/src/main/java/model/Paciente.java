package model;

import java.util.Date;

public class Paciente {
    private int id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private Date nascimento;
    private String endereco;

    public Paciente() {}

    // Getters para o sistema ler os dados
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
    public Date getNascimento() { return nascimento; }
    public String getEndereco() { return endereco; }
}