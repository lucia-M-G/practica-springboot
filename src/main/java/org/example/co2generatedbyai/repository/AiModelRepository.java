package org.example.co2generatedbyai.repository;

import org.example.co2generatedbyai.model.AiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AiModelRepository {

    @Autowired
    private JdbcTemplate jdbc;

    private final RowMapper<AiModel> aiModelRowMapper = (rs, rowNum) -> new AiModel(
            rs.getString("nombre"),
            rs.getString("tipo_tarea"),
            rs.getDouble("co2_consulta"),
            rs.getDouble("consumo")
    );

    public List<AiModel> getAllAiModel() {
        return jdbc.query("SELECT * FROM ai_model", aiModelRowMapper);
    }

    public AiModel findByNombreAndTipoTarea(String nombre, String tipoTarea) {
        String sql = "SELECT * FROM ai_model WHERE nombre = ? AND tipo_tarea = ?";
        return jdbc.queryForObject(sql, new Object[]{nombre, tipoTarea}, aiModelRowMapper);
    }


    public List<AiModel> getAllAiModelOrderByEficency() {
        return jdbc.query("SELECT * FROM ai_model ORDER BY consumo", aiModelRowMapper);
    }

}
