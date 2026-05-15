package org.example.co2generatedbyai;

import org.example.co2generatedbyai.model.AiModel;
import org.example.co2generatedbyai.model.AiSesion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class AiSesionTest {

    private static AiSesion aiSesion1;
    private static AiSesion aiSesion2;
    private static AiModel aiModel1;


    @BeforeAll
    public static void setUp() {
        aiModel1 = new AiModel("GPT-4", "texto", 6.5, 0.005);
        AiModel aiModel2 = new AiModel("Stable Diffusion", "imagen", 8.75, 0.012);
        aiSesion1 = new AiSesion("Maria", 6, LocalDateTime.now(), "", aiModel1);
        aiSesion2 = new AiSesion("Julio", 5, LocalDateTime.now(), "", aiModel2);

    }

    @Nested
    class calcularTest {
        @Test
        void aiSesion1CO2Estimado() {
            assertEquals(39.0, aiSesion1.getCo2Estimado(), 0.01);
        }

        @Test
        void aiSesion1kwhEstimado() {
            assertEquals(0.00003, aiSesion1.getKwhEstimado(), 0.01);
        }

        @Test
        void aiSesion2CO2Estimado() {
            assertEquals(43.75, aiSesion2.getCo2Estimado(), 0.01);
        }

        @Test
        void aiSesion2kwhEstimado() {
            assertEquals(0.00006, aiSesion2.getKwhEstimado(), 0.01);
        }
    }

    @Nested
    class ExceptionTest {
        @Test
        void aiSesion3Error() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                AiSesion aiSesion3 = new AiSesion("Julio", 5, LocalDateTime.now(), "", null);
            });

            assertEquals("No existe modelo de referencia", exception.getMessage());
        }

        @Test
        void aiSEsion4Error() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                AiSesion aiSesion4 = new AiSesion("Lucia", 0, LocalDateTime.now(), "", aiModel1);
            });

            assertEquals("No se pueden hacer sesiones sin numero de consultas", exception.getMessage());
        }
    }

}
