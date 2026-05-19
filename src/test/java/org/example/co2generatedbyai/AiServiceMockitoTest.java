package org.example.co2generatedbyai;

import org.example.co2generatedbyai.model.AiModel;
import org.example.co2generatedbyai.model.AiSesion;
import org.example.co2generatedbyai.respository.*;
import org.example.co2generatedbyai.service.AiService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AiServiceMockitoTest {
    public static AiSesionRepository aiSesionRepository;
    public static AiModelRepository aiModelRepository;
    public static AiService aiService;
    public static AiModel modeloGpt4;

    @BeforeAll
    static void setUp() {
        aiSesionRepository = Mockito.mock(AiSesionRepository.class);
        aiModelRepository = Mockito.mock(AiModelRepository.class);

        modeloGpt4 = new AiModel("GPT-4", "texto", 6.5, 0.005);

        Mockito.when(aiModelRepository.findByNameAiModel("GPT-4")).thenReturn(modeloGpt4);

        aiService = new AiService();
        ReflectionTestUtils.setField(aiService, "aiSesionRepository", aiSesionRepository);
        ReflectionTestUtils.setField(aiService, "aiModelRepository", aiModelRepository);
    }

    @Nested
    class CreateSesionTests {

        @Test
        void createSesion_devuelveCo2Calculado() {
            AiSesion info = new AiSesion();
            info.setUsuario("Maria");
            info.setModel("GPT-4");
            info.setNumConsultas(6);
            info.setFecha(LocalDate.parse("2026-05-19"));
            info.setDescripcion("prueba");

            AiSesion resultado = aiService.createSesion(info);

            assertEquals("Maria", resultado.getUsuario());
            assertEquals(39.0, resultado.getCo2Estimado(), 0.01);
            assertEquals(0.03, resultado.getKwhEstimado(), 0.01);
        }
    }

    @Nested
    class GetSesionTests {

        @Test
        void getSesion_existe_devuelveSesion() {
            AiSesion guardada = new AiSesion(
                    "Maria", 5, LocalDate.parse("2026-05-19"), "prueba", modeloGpt4);
            guardada.setId(1);

            Mockito.doReturn(guardada).when(aiSesionRepository).findById(1);

            AiSesion resultado = aiService.getSesion(1);

            assertNotNull(resultado);
            assertEquals("Maria", resultado.getUsuario());
        }

        @Test
        void getSesion_noExiste_devuelveNull() {
            Mockito.doThrow(new EmptyResultDataAccessException(1))
                    .when(aiSesionRepository).findById(99);

            assertNull(aiService.getSesion(99));
        }
    }

    @Nested
    class DeleteSesionTests {

        @Test
        void deleteSesion_existe_devuelveTrue() {
            AiSesion guardada = new AiSesion(
                    "Maria", 5, LocalDate.parse("2026-05-19"), "prueba", modeloGpt4);
            guardada.setId(1);

            Mockito.doReturn(guardada).when(aiSesionRepository).findById(1);

            assertTrue(aiService.deleteSesion(1));
        }

        @Test
        void deleteSesion_noExiste_devuelveFalse() {
            Mockito.doReturn(null).when(aiSesionRepository).findById(99);

            assertEquals(false, aiService.deleteSesion(99));
        }
    }
}