package it.unisalento.recproject.recprojectio;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.recproject.recprojectio.domain.Credit;
import it.unisalento.recproject.recprojectio.domain.RenewableEnergy;
import it.unisalento.recproject.recprojectio.domain.User;
import it.unisalento.recproject.recprojectio.dto.EnergyConsumptionDTO;
import it.unisalento.recproject.recprojectio.dto.LoginDTO;
import it.unisalento.recproject.recprojectio.dto.SurplusEnergyDTO;
import it.unisalento.recproject.recprojectio.repositories.*;
import it.unisalento.recproject.recprojectio.security.JwtUtilities;
import it.unisalento.recproject.recprojectio.service.RenewableEnergyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnergyConsumptionRepository energyConsumptionRepository;

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private SurplusEnergyRepository surplusEnergyRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilities jwtUtilities;

    @Autowired
    private RenewableEnergyService renewableEnergyService;

    private String jwtToken;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        // Create a test user and generate a JWT token
        User testUser = new User();
        testUser.setId("testId");
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        userRepository.save(testUser);

        jwtToken = jwtUtilities.generateToken(testUser.getEmail());
    }

    @Test
    public void testGetUserById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/testId")
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":\"testId\",\"name\":\"Test User\",\"email\":\"test@example.com\"}"));
    }

    @Test
    public void testRegisterEnergyConsumption() throws Exception {
        EnergyConsumptionDTO energyConsumptionDTO = new EnergyConsumptionDTO();
        energyConsumptionDTO.setDeviceName("Device1");
        energyConsumptionDTO.setEnergyConsumed(50);
        energyConsumptionDTO.setDate(LocalDate.now());

        String json = objectMapper.writeValueAsString(energyConsumptionDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/testId/energy-consumption")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Energy consumption registered successfully"));
    }


    @Test
    public void testGetEnergyConsumption() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/energy-consumption")
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetSurplusEnergy() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/surplus-energy")
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddRenewableSource() throws Exception {
        RenewableEnergy renewableEnergy = new RenewableEnergy();
        renewableEnergy.setType("Solar Panel");
        renewableEnergy.setGeneratedEnergy(100.0);
        renewableEnergy.setUserId("testId");

        String json = objectMapper.writeValueAsString(renewableEnergy);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/testId/add-renewable-source")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Renewable source added successfully for user."));
    }
    @Test
    public void testGetCreditsByUserId() throws Exception {
        // Setup a credit for the test user
        Credit credit = new Credit();
        credit.setUserId("testId");
        credit.setCreditScore(100.0);
        creditRepository.save(credit);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/testId/credits")
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"userId\":\"testId\",\"creditScore\":100.0}"));
    }

}