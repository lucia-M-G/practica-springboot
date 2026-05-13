package org.example.co2generatedbyai.model;

public class AiModel {
    private String model;
    private String tipoTask;
    private Double CO2Consulta;
    private Double consumo;

    public AiModel(String model, String tipoTask, Double CO2Consulta, Double consumo) {
        this.model = model;
        this.tipoTask = tipoTask;
        this.CO2Consulta = CO2Consulta;
        this.consumo = consumo;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTipoTask() {
        return tipoTask;
    }

    public void setTipoTask(String tipoTask) {
        this.tipoTask = tipoTask;
    }

    public Double getCO2Consulta() {
        return CO2Consulta;
    }

    public void setCO2Consulta(Double CO2Consulta) {
        this.CO2Consulta = CO2Consulta;
    }

    public Double getConsumo() {
        return consumo;
    }

    public void setConsumo(Double consumo) {
        this.consumo = consumo;
    }
}
