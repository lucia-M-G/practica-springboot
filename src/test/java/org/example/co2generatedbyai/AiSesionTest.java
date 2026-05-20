package org.example.co2generatedbyai;

import org.example.co2generatedbyai.model.AiModel;
import org.example.co2generatedbyai.model.AiSesion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AiSesionTest {

    private AiSesion aiSesion1;
    private AiSesion aiSesion2;
    private AiModel aiModel1;

    @BeforeEach
    void setUp() {
        aiModel1 = new AiModel("GPT-4", "texto", 6.5, 0.005);
        AiModel aiModel2 = new AiModel("Stable Diffusion", "imagen", 8.75, 0.012);
        aiSesion1 = new AiSesion("Maria", 6, LocalDate.now(), "", aiModel1);
        aiSesion2 = new AiSesion("Julio", 5, LocalDate.now(), "", aiModel2);
    }

    @Nested
    class CalculoEmisionesTest {
        @Test
        void maria_co2Estimado() {
            assertEquals(39.0, aiSesion1.getCo2Estimado(), 0.01);
        }

        @Test
        void maria_kwhEstimado() {
            assertEquals(0.03, aiSesion1.getKwhEstimado(), 0.01);
        }

        @Test
        void julio_co2Estimado() {
            assertEquals(43.75, aiSesion2.getCo2Estimado(), 0.01);
        }

        @Test
        void julio_kwhEstimado() {
            assertEquals(0.06, aiSesion2.getKwhEstimado(), 0.01);
        }

        @Test
        void copiaDatosDesdeAiModel() {
            assertEquals("GPT-4", aiSesion1.getModel());
            assertEquals("texto", aiSesion1.getTipoTask());
            assertEquals("Maria", aiSesion1.getUsuario());
            assertEquals(6, aiSesion1.getNumConsultas());
        }
    }

    @Nested
    class ValidacionesConstructorTest {
        @Test
        void modeloNull_lanzaExcepcion() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    new AiSesion("Julio", 5, LocalDate.now(), "", null)
            );
            assertEquals("No existe modelo de referencia", exception.getMessage());
        }

        @Test
        void ceroConsultas_lanzaExcepcion() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    new AiSesion("Lucia", 0, LocalDate.now(), "", aiModel1)
            );
            assertEquals("No se pueden hacer sesiones sin numero de consultas", exception.getMessage());
        }

        @Test
        void consultasNegativas_lanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () ->
                    new AiSesion("Lucia", -1, LocalDate.now(), "", aiModel1)
            );
        }
    }
}