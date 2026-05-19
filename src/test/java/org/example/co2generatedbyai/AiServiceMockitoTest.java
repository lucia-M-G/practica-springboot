package org.example.co2generatedbyai;


import org.example.co2generatedbyai.respository.AiModelRepository;
import org.example.co2generatedbyai.respository.AiSesionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AiServiceMockitoTest {

    public static AiSesionRepository aiSesionRepository;
    public static AiModelRepository aiModelRepository;

    @BeforeAll
    public static void setUp() {
        aiModelRepository = Mockito.mock(AiModelRepository.class);
        aiSesionRepository = Mockito.mock(AiSesionRepository.class);
    }
}
