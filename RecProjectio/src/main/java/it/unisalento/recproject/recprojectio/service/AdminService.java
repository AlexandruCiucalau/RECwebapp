package it.unisalento.recproject.recprojectio.service;

import it.unisalento.recproject.recprojectio.domain.Admin;
import it.unisalento.recproject.recprojectio.domain.Reward;
import it.unisalento.recproject.recprojectio.domain.SurplusEnergy;
import it.unisalento.recproject.recprojectio.domain.User;
import it.unisalento.recproject.recprojectio.dto.AdminDTO;
import it.unisalento.recproject.recprojectio.repositories.AdminRepository;
import it.unisalento.recproject.recprojectio.repositories.RewardRepository;
import it.unisalento.recproject.recprojectio.repositories.SurplusEnergyRepository;
import it.unisalento.recproject.recprojectio.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private SurplusEnergyRepository surplusEnergyRepository;
    public List<AdminDTO> getAllAdmins() {
        return adminRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<AdminDTO> getAdminById(String id) {
        return adminRepository.findById(id).map(this::convertToDTO);
    }

    public AdminDTO createAdmin(AdminDTO adminDTO) {
        Admin admin = new Admin();
        admin.setName(adminDTO.getName());
        admin.setEmail(adminDTO.getEmail());
        admin.setPassword(adminDTO.getPassword());
        Admin savedAdmin = adminRepository.save(admin);
        return convertToDTO(savedAdmin);
    }

    public Optional<AdminDTO> updateAdmin(String id, AdminDTO adminDTO) {
        Optional<Admin> adminOptional = adminRepository.findById(id);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            admin.setName(adminDTO.getName());
            admin.setEmail(adminDTO.getEmail());
            admin.setPassword(adminDTO.getPassword());
            Admin updatedAdmin = adminRepository.save(admin);
            return Optional.of(convertToDTO(updatedAdmin));
        }
        return Optional.empty();
    }

    public boolean deleteAdmin(String id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private AdminDTO convertToDTO(Admin admin) {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(admin.getId());
        adminDTO.setName(admin.getName());
        adminDTO.setEmail(admin.getEmail());
        adminDTO.setPassword(admin.getPassword());
        return adminDTO;
    }

    @Scheduled(fixedRate = 600000) // Schedule to run every 10 minutes
    public void processPaymentsAndRewards() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            // Calculate the rewards based on the user's surplus energy
            List<SurplusEnergy> surplusEnergies = surplusEnergyRepository.findByUserId(user.getId());
            double totalSurplusEnergy = surplusEnergies.stream().mapToDouble(SurplusEnergy::getSurplusAmount).sum();
            int rewardPoints = (int) (totalSurplusEnergy * 0.10);

            // Create and save the reward
            Reward reward = new Reward(user.getId(), rewardPoints, "Periodic Reward", LocalDateTime.now());
            rewardRepository.save(reward);
        }
    }
}