package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.actuator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ActuatorTest {
    private final String baseUrl = "/actuator/";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void metricsShouldReturnAllMetrics() throws Exception {
        mockMvc.perform(get(baseUrl + "metrics"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("\"names\"")));
    }

    @Test
    public void healthShouldReturnUp() throws Exception {
        mockMvc.perform(get(baseUrl + "health"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("\"status\":\"UP\"")));
    }
}
