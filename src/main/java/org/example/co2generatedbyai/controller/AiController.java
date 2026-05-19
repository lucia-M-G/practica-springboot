package org.example.co2generatedbyai.controller;

import org.example.co2generatedbyai.model.*;
import org.example.co2generatedbyai.respository.AiModelRepository;
import org.example.co2generatedbyai.service.AiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
public class AiController {
    @Autowired
    private AiService aiService;
    @Autowired
    private AiModelRepository aiModelRepository;

    @GetMapping("/")
    public String getIndex() {
        return "CO2 generado por IA - API funcionando";
    }

    @GetMapping("/sesions")
    public ResponseEntity<List<AiSesion>> getSesions() {
        return ResponseEntity.ok(aiService.getAllSesions());
    }

    @GetMapping("/sesions/report")
    public ResponseEntity<List<AiUserReport>> getReport() {
        return ResponseEntity.ok(aiService.getReport());
    }

    @GetMapping("/sesions/export")
    public ResponseEntity<List<AiSesion>> getExport() {
        return ResponseEntity.ok(aiService.exportSesions());
    }

    // getSesionById después de report y export para evitar confundir /report con /{id}
    @GetMapping(path = "/sesions/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AiSesion> getSesion(@PathVariable int id) {
        AiSesion sesion = aiService.getSesion(id);
        if (sesion == null) {
            // 404: Not Found
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(sesion);
    }

    @PostMapping("/sesions")
    public ResponseEntity<AiSesion> createSession(@RequestBody AiSesion sesion) {
        if (sesion.getUsuario() == null || sesion.getUsuario().trim().isEmpty()
                || sesion.getModel() == null || sesion.getModel().trim().isEmpty()) {
            // 400: Bad request (formato erróneo)
            return ResponseEntity.status(400).build();
        }

        AiSesion creada = aiService.createSesion(sesion);
        // 201: Creada
        System.out.println("Sesión añadida: " + creada.getId());
        return ResponseEntity.status(201).body(creada);
    }

    @DeleteMapping("/sesions/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable int id) {
        if (!aiService.deleteSesion(id)) {
            // 404: Not Found
            return ResponseEntity.status(404).build();
        }

        // 204: No Content
        System.out.println("Sesión eliminada: " + id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/modelRanking")
    public ResponseEntity<List<AiModel>> rankingModel () {
        return ResponseEntity.ok(aiService.getAiModelRanking());
    }
}