package org.example.co2generatedbyai.model;

public class AiUserReport {
    private String usuario;
    private Double totalCo2;
    private Double totalKwh;

    public AiUserReport() {
    }

    public AiUserReport(String usuario, Double totalCo2, Double totalKwh) {
        this.usuario = usuario;
        this.totalCo2 = totalCo2;
        this.totalKwh = totalKwh;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Double getTotalCo2() {
        return totalCo2;
    }

    public void setTotalCo2(Double totalCo2) {
        this.totalCo2 = totalCo2;
    }

    public Double getTotalKwh() {
        return totalKwh;
    }

    public void setTotalKwh(Double totalKwh) {
        this.totalKwh = totalKwh;
    }
}