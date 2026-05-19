package org.example.co2generatedbyai;

import org.example.co2generatedbyai.controller.AiController;
import org.example.co2generatedbyai.model.AiModel;
import org.example.co2generatedbyai.model.AiSesion;
import org.example.co2generatedbyai.model.AiUserReport;
import org.example.co2generatedbyai.service.AiService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AiController.class)
@AutoConfigureMockMvc(addFilters = false) // para np preocuparnos de añadir tockens al controller
@ExtendWith(MockitoExtension.class)
public class AiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // para facilitar el acceso al service
    private AiService aiService;

    @Autowired
    private ObjectMapper objectMapper;

    private AiSesion aiSesion;
    private List<AiSesion> aiSesions;
    private List<AiUserReport> aiUserReports;

    @BeforeEach
    public void setUp() {
        aiSesion = new AiSesion("Maria", 5, LocalDate.parse("2026-05-19"), "prueba",new AiModel("GPT-4", "texto", 6.5, 0.005));

        aiSesions = new ArrayList<>();
        aiSesions.add(aiSesion);
        aiSesions.add(new AiSesion("Lucia", 10, LocalDate.parse("2025-04-11"), "prueba1",new AiModel("GPT-3.5", "texto", 0.65, 0.001)));
        aiSesions.add(new AiSesion("Julio", 2, LocalDate.parse("2026-04-14"), "prueba2",new AiModel("Stable Diffusion", "imagen", 8.75, 0.012)));

        aiUserReports = new ArrayList<>();
        aiUserReports.add(new AiUserReport("Maria", aiSesion.getCo2Estimado(), aiSesion.getKwhEstimado()));
        aiUserReports.add(new AiUserReport("Lucia", aiSesions.get(1).getCo2Estimado(), aiSesions.get(1).getKwhEstimado()));
        aiUserReports.add(new AiUserReport("Lucia", aiSesions.get(2).getCo2Estimado(), aiSesions.get(2).getKwhEstimado()));
    }

    @Test
    void getIndexTest() throws Exception {
        mockMvc.perform(get("api/"))
                .andExpect(content().string("CO2 generado por IA - API funcionando"));
    }

    @Test
    void getSesion_ReturnSesion_WhenFound() throws Exception {
        AiSesion aiSesion = new AiSesion("Maria", 5, LocalDate.parse("2026-05-19"), "prueba",new AiModel("GPT-4", "texto", 6.5, 0.005));
        when(aiService.getSesion(aiSesion.getId())).thenReturn(aiSesion);

        mockMvc.perform(get("/sesions/1"))
                .accept(MediaType.APPLICATION_JSON)
                .andExpect(status().isOk())
                .andExpect(content().contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$.usuario").value("Maria"));
    }

    // test del post de sesions
    @Test
    void creatSesionTest_ReturnCreated() throws Exception {
        AiSesion aiSesion1 = aiService.createSesion(ArgumentMatchers.any());
        given(aiSesion1).willAnswer((invocation -> invocation.getArgument(0)));

        //traemos nuesto MockMvc
        ResultActions response = mockMvc.perform(post("api/sesions")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(aiSesion1)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.usuario", CoreMatchers.is(aiSesion1.getUsuario()))) //comprovamos el valor del usuario de la sesion
                .andExpect(MockMvcResultMatchers.jsonPath("$.numConsultas", CoreMatchers.is(aiSesion1.getNumConsultas())));
                // .andDo(MockMvcResultHandlers.print()); //find what is actually coming back from de api
    }

    @Test
    void getSesionsTest_ReturnResponseDto() throws Exception {
        // imitar aiService.getAllSesions()
        when(aiService.getAllSesions()).thenReturn(aiSesions);

        ResultActions response = mockMvc.perform(get("api/sesions"))
    }


}
