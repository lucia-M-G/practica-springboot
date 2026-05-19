package org.example.co2generatedbyai.model;

import java.time.LocalDate;

public class AiSesion {
    private int id;
    private AiModel aiModel;
    private String usuario;
    private String model;
    private String tipoTask;
    private int numConsultas;
    private LocalDate fecha;
    private String descripcion;
    private Double co2Estimado;
    private Double kwhEstimado;

    // Constructor para que JSON pueda crear una sesión (formulario blanco para "cliente")
    public AiSesion() {
    }

    // Constructor con AiModel es para crear la sesión de verdad con los datos de la BD
    public AiSesion(String usuario, int numConsultas, LocalDate fecha, String descripcion, AiModel aiModel) {
        if (aiModel == null) {
            throw new IllegalArgumentException("No existe modelo de referencia");
        }
        if (numConsultas <= 0) {
            throw new IllegalArgumentException("No se pueden hacer sesiones sin numero de consultas");
        }

        this.aiModel = new AiModel(aiModel.getModel(), aiModel.getTipoTask(), aiModel.getCO2Consulta(), aiModel.getConsumo());
        this.usuario = usuario;
        this.model = aiModel.getModel();
        this.tipoTask = aiModel.getTipoTask();
        this.numConsultas = numConsultas;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.co2Estimado = this.numConsultas * aiModel.getCO2Consulta();
        this.kwhEstimado = this.numConsultas * aiModel.getConsumo();
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
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