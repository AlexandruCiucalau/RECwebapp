package it.unisalento.recproject.recprojectio;

import it.unisalento.recproject.recprojectio.domain.Reward;
import it.unisalento.recproject.recprojectio.domain.SurplusEnergy;
import it.unisalento.recproject.recprojectio.domain.User;
import it.unisalento.recproject.recprojectio.dto.RewardDTO;
import it.unisalento.recproject.recprojectio.repositories.RewardRepository;
import it.unisalento.recproject.recprojectio.repositories.SurplusEnergyRepository;
import it.unisalento.recproject.recprojectio.repositories.UserRepository;
import it.unisalento.recproject.recprojectio.security.JwtUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private SurplusEnergyRepository surplusEnergyRepository;

    @Autowired
    private JwtUtilities jwtUtilities;

    private String adminToken;

    @BeforeEach
    public void setUp() {
        // Clean up the repositories before each test
        userRepository.deleteAll();
        rewardRepository.deleteAll();
        surplusEnergyRepository.deleteAll();

        // Add a test user
        User testUser = new User();
        testUser.setId("testId");
        testUser.setName("Test User");
        testUser.setEmail("admin@example.com");
        testUser.setPassword("password");
        testUser.setRole("ADMIN");
        userRepository.save(testUser);

        // Generate JWT token for admin user
        adminToken = jwtUtilities.generateToken(testUser.getEmail());

        // Add some surplus energy
        SurplusEnergy surplusEnergy = new SurplusEnergy("testId", 100.0, LocalDate.now());
        surplusEnergyRepository.save(surplusEnergy);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/users")
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetUserById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/users/testId")
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":\"testId\",\"name\":\"Test User\",\"email\":\"admin@example.com\",\"role\":\"ADMIN\"}"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/users/testId")
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    @Test
    public void testAddReward() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/rewards")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"userId\": \"testId\", \"rewardType\": \"Test Reward\" }")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetRewardsByUserId() throws Exception {
        // Pre-add a reward
        Reward reward = new Reward("testId", 10, "Test Reward", LocalDateTime.now());
        rewardRepository.save(reward);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/rewards/user/testId")
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testDeleteReward() throws Exception {
        // Pre-add a reward
        Reward reward = new Reward("testId", 10, "Test Reward", LocalDateTime.now());
        rewardRepository.save(reward);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/rewards/" + reward.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Reward deleted successfully"));
    }
}