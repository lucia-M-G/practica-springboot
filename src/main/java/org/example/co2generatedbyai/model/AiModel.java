package org.example.co2generatedbyai.model;

public class AiModel {
    private String model;
    private String tipoTask;
    private Double CO2Consulat;
    private Double consumo;

    public AiModel(String model, String tipoTask, Double CO2Consulat, Double consumo) {
        this.model = model;
        this.tipoTask = tipoTask;
        this.CO2Consulat = CO2Consulat;
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

    public Double getCO2Consulat() {
        return CO2Consulat;
    }

    public void setCO2Consulat(Double CO2Consulat) {
        this.CO2Consulat = CO2Consulat;
    }

    public Double getConsumo() {
        return consumo;
    }

    public void setConsumo(Double consumo) {
        this.consumo = consumo;
    }
}
