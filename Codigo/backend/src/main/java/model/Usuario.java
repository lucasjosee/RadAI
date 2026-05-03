package model;

public class Usuario {
    public int id;
    public String nome;
    public String email;
    public String senha;
    public String tipo;
    public String documentoProfissional; 

    // Construtor vazio
    public Usuario() {}
    
    // Construtor 
    public Usuario(String nome, String email, String senha, String tipo, String doc) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
        this.documentoProfissional = doc;
    }

    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public String getTipo() { return tipo; }
}