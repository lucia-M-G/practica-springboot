package org.example.co2generatedbyai.model;

import java.time.LocalDateTime;

public class AiSesion {
    private int id;
    private String usuario;
    private String model;
    private String tipoTask;
    private int numConsultas;
    private LocalDateTime fecha;
    private String descripcion;
    private Double co2Estimado;
    private Double kwhEstimado;

    public AiSesion(String usuario, String model, String tipoTask, int numConsultas, LocalDateTime fecha, String descripcion) {
        this.usuario = usuario;
        this.model = model;
        this.tipoTask = tipoTask;
        this.numConsultas = numConsultas;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public void calcularEmisiones(AiModel aiModel) {
        if (aiModel == null) {
            throw new IllegalArgumentException("No existe modelo de referencia");
        }

        this.co2Estimado = this.numConsultas * aiModel.getCO2Consulta();
        this.kwhEstimado = this.numConsultas * (aiModel.getConsumo() / 1000.0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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

    public int getNumConsultas() {
        return numConsultas;
    }

    public void setNumConsultas(int numConsultas) {
        this.numConsultas = numConsultas;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getCo2Estimado() {
        return co2Estimado;
    }

    public void setCo2Estimado(Double co2Estimado) {
        this.co2Estimado = co2Estimado;
    }

    public Double getKwhEstimado() {
        return kwhEstimado;
    }

    public void setKwhEstimado(Double kwhEstimado) {
        this.kwhEstimado = kwhEstimado;
    }
}