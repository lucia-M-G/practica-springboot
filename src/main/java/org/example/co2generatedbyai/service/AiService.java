package org.example.co2generatedbyai.service;

import org.example.co2generatedbyai.model.*;
import org.example.co2generatedbyai.respository.*;

import org.springframework.beans.factory.annotation.Autowired;
//Excepcion de SpringBoot para consulta sin resulados
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class AiService {

    @Autowired
    private AiSesionRepository aiSesionRepository;

    @Autowired
    private AiModelRepository aiModelRepository;

    public List<AiSesion> getAllSesions() {
        return aiSesionRepository.findAll();
    }

    public AiSesion getSesion(int id) {
        try {
            return aiSesionRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public AiSesion createSesion(AiSesion info) {
        LocalDate fecha = info.getFecha() != null ? info.getFecha() : LocalDate.now();
        String descripcion = info.getDescripcion() != null ? info.getDescripcion() : "";

        AiModel modelo = aiModelRepository.findByNombreAndTipoTarea(
                info.getModel(),
                info.getTipoTask()
        );

        AiSesion nuevaSesion = new AiSesion(
                info.getUsuario(),
                info.getNumConsultas(),
                fecha,
                descripcion,
                modelo
        );

        aiSesionRepository.save(nuevaSesion);  // save con KeyHolder para rellenar el id
        return nuevaSesion;
    }

    public boolean deleteSesion(int id) {
        if (getSesion(id) == null) {
            return false;
        }

        aiSesionRepository.deleteById(id);
        return true;
    }

    public List<AiUserReport> getReport() {
        return aiSesionRepository.getReport();
    }

    public List<AiSesion> exportSesions() {
        List<AiSesion> sesiones = aiSesionRepository.findAll();
        try {
            // Crear carpeta de salida de informe si no existe
            new java.io.File("exports").mkdirs();
            BufferedWriter escribir = new BufferedWriter(new FileWriter("exports/sesions.csv"));
            for (AiSesion sesion : sesiones) {
                escribir.write(sesion.getId() + ","
                        + sesion.getUsuario() + ","
                        + sesion.getModel() + ","
                        + sesion.getTipoTask() + ","
                        + sesion.getNumConsultas() + ","
                        + sesion.getFecha() + ","
                        + sesion.getDescripcion() + ","
                        + sesion.getCo2Estimado() + ","
                        + sesion.getKwhEstimado());
                escribir.newLine();
            }
            escribir.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sesiones;
    }
}