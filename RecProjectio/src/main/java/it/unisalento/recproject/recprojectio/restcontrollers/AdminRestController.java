package it.unisalento.recproject.recprojectio.restcontrollers;

import it.unisalento.recproject.recprojectio.domain.Reward;
import it.unisalento.recproject.recprojectio.domain.SurplusEnergy;
import it.unisalento.recproject.recprojectio.domain.User;
import it.unisalento.recproject.recprojectio.dto.RewardDTO;
import it.unisalento.recproject.recprojectio.dto.UserDTO;
import it.unisalento.recproject.recprojectio.repositories.SurplusEnergyRepository;
import it.unisalento.recproject.recprojectio.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import it.unisalento.recproject.recprojectio.repositories.RewardRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private SurplusEnergyRepository surplusEnergyRepository;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(convertToDTO(userOptional.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }
    private RewardDTO convertToDTO(Reward reward) {
        RewardDTO rewardDTO = new RewardDTO();
        rewardDTO.setId(reward.getId());
        rewardDTO.setUserId(reward.getUserId());
        rewardDTO.setPoints(reward.getPoints());
        rewardDTO.setRewardType(reward.getRewardType());
        rewardDTO.setDateEarned(reward.getDateEarned());
        return rewardDTO;
    }

    @PostMapping("/rewards")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RewardDTO> addReward(@RequestBody RewardDTO rewardDTO) {
        List<SurplusEnergy> surplusEnergies = surplusEnergyRepository.findByUserId(rewardDTO.getUserId());
        double totalSurplusEnergy = surplusEnergies.stream().mapToDouble(SurplusEnergy::getSurplusAmount).sum();
        int rewardPoints = (int) (totalSurplusEnergy * 0.10);

        Reward reward = new Reward(rewardDTO.getUserId(), rewardPoints, rewardDTO.getRewardType(), LocalDateTime.now());
        Reward savedReward = rewardRepository.save(reward);

        return ResponseEntity.ok(convertToDTO(savedReward));
    }
    @GetMapping("/rewards/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RewardDTO> getRewardsByUserId(@PathVariable String userId) {
        List<Reward> rewards = rewardRepository.findByUserId(userId);
        return rewards.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @DeleteMapping("/rewards/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteReward(@PathVariable String id) {
        if (rewardRepository.existsById(id)) {
            rewardRepository.deleteById(id);
            return ResponseEntity.ok("Reward deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}