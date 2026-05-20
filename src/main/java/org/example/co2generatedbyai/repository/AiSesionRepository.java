package org.example.co2generatedbyai.repository;

import org.example.co2generatedbyai.model.AiSesion;
import org.example.co2generatedbyai.model.AiUserReport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.example.co2generatedbyai.model.AiModel;

@Repository
public class AiSesionRepository {

    private final JdbcTemplate jdbcTemplate;
    private final AiModelRepository aiModelRepository;

    public AiSesionRepository(JdbcTemplate jdbcTemplate, AiModelRepository aiModelRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.aiModelRepository = aiModelRepository;
    }

    public List<AiSesion> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM sesions",
            (rs, rowNum) -> mapearSesion(rs)
        );
    }

    public AiSesion findById(int id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM sesions WHERE id = ?",
            new Object[]{id},
            (rs, rowNum) -> mapearSesion(rs)
        );
    }

    public void save(AiSesion sesion) {
        jdbcTemplate.update(
            "INSERT INTO sesions (usuario, modelo, tipo_tarea, num_consultas, fecha, descripcion, co2_estimado, kwh_estimado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            sesion.getUsuario(),
            sesion.getModel(),
            sesion.getTipoTask(),
            sesion.getNumConsultas(),
            sesion.getFecha(),
            sesion.getDescripcion(),
            sesion.getCo2Estimado(),
            sesion.getKwhEstimado()
        );

        // Recuperar id real para POST
        Integer id = jdbcTemplate.queryForObject(
                "SELECT LAST_INSERT_ID()",
                Integer.class
        );

        if (id != null) {
            sesion.setId(id);
        }
    }

    public void deleteById(int id) {
        jdbcTemplate.update(
            "DELETE FROM sesions WHERE id = ?",
            id
        );
    }

    public List<AiSesion> findByUsuario(String usuario) {
        return jdbcTemplate.query(
            "SELECT * FROM sesions WHERE usuario = ?",
            new Object[]{usuario},
            (rs, rowNum) -> mapearSesion(rs)
        );
    }

    // GET /sessions/report — total CO₂ y kWh agrupado por usuario
    public List<AiUserReport> getReport() {
        String sql = "SELECT usuario, " +
                     "SUM(co2_estimado) as totalCo2, " +
                     "SUM(kwh_estimado) as totalKwh " +
                     "FROM sesions GROUP BY usuario";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new AiUserReport(
                rs.getString("usuario"),
                rs.getDouble("totalCo2"),
                rs.getDouble("totalKwh")
        ));
    }

    // Actualizado al nuevo Aisesion
    private AiSesion mapearSesion(ResultSet rs) throws SQLException {
        // Primero busca el AiModel por nombre
        AiModel aiModel = aiModelRepository.findByNombreAndTipoTarea(
                rs.getString("modelo"),
                rs.getString("tipo_tarea")
        );

        java.sql.Date fechaSql = rs.getDate("fecha");
        LocalDate fecha = fechaSql != null ? fechaSql.toLocalDate() : LocalDate.now();

        // Luego crea la sesión con ese AiModel
        AiSesion sesion = new AiSesion(
            rs.getString("usuario"),
            rs.getInt("num_consultas"),
            fecha,
            rs.getString("descripcion"),
            aiModel
        );
        sesion.setId(rs.getInt("id"));
        return sesion;
    }
}