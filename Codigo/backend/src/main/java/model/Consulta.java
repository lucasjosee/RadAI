package model;

public class Consulta {
    public int id;
    public int paciente_id;
    public String data_hora;
    public String sintomas;
    public String diagnostico_ia; 
    public String status;

    public Consulta() {}

    public int getId() { return id; }
    public int getPaciente_id() { return paciente_id; }
    public String getData_hora() { return data_hora; }
    public String getSintomas() { return sintomas; }
    public String getDiagnostico_ia() { return diagnostico_ia; }
    public String getStatus() { return status; }
}