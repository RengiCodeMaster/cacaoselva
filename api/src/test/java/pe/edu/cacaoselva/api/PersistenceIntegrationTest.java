package pe.edu.cacaoselva.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import pe.edu.cacaoselva.application.port.LoteRepository;

@SpringBootTest
@AutoConfigureMockMvc
class PersistenceIntegrationTest {

    @Autowired
    private LoteRepository loteRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void debeEjecutarCrudCompletoConPersistencia() throws Exception {
        assertThat(loteRepository.count()).isEqualTo(10);

        MvcResult creacion = mockMvc.perform(post("/lotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"socio":"Socio de prueba","pesoKg":42.50,"estado":"PENDIENTE"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        int id = objectMapper.readTree(creacion.getResponse().getContentAsString())
                .get("id")
                .asInt();
        assertThat(creacion.getResponse().getHeader("Location")).isEqualTo("/lotes/" + id);

        mockMvc.perform(put("/lotes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"socio":"Socio actualizado","pesoKg":50.25,"estado":"LIQUIDADO"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.socio").value("Socio actualizado"))
                .andExpect(jsonPath("$.estado").value("LIQUIDADO"));

        mockMvc.perform(delete("/lotes/{id}", id))
                .andExpect(status().isNoContent());

        assertThat(loteRepository.findById(id)).isEmpty();
    }
}
