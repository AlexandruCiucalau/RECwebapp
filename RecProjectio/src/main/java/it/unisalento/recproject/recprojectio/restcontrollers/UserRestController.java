package it.unisalento.recproject.recprojectio.restcontrollers;
import it.unisalento.recproject.recprojectio.di.IRenewableSource;
import it.unisalento.recproject.recprojectio.domain.*;
import it.unisalento.recproject.recprojectio.dto.*;
import it.unisalento.recproject.recprojectio.exceptions.UserNotFoundException;
import it.unisalento.recproject.recprojectio.repositories.*;
import it.unisalento.recproject.recprojectio.security.JwtUtilities;
import it.unisalento.recproject.recprojectio.service.RenewableEnergyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtilities jwtUtilities;

    @Autowired
    EnergyConsumptionRepository energyConsumptionRepository;

    @Autowired
    SurplusEnergyRepository surplusEnergyRepository;

    @Autowired
    CreditRepository creditRepository;

    @Autowired
    private RenewableEnergyService renewableEnergyService;

    @Autowired
    RewardRepository rewardRepository;

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public UserDTO get(@PathVariable String id) throws UserNotFoundException {

        Optional<User>user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new UserNotFoundException();
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.get().getId());
        userDTO.setName(user.get().getName());
        userDTO.setEmail(user.get().getEmail());

        return userDTO; //DTO
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public UsersListDTO getAll(){

        UsersListDTO usersList = new UsersListDTO();
        ArrayList<UserDTO> users = new ArrayList<>();
        usersList.setList(users);

        List<User> userList = userRepository.findAll();

        for(User user : userList){
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setEmail(user.getEmail());
            userDTO.setName(user.getName());
            userDTO.setPassword(user.getPassword());
            users.add(userDTO);
        }
        return usersList;
    }
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public UsersListDTO search(@RequestParam (required = false) String name){
        UsersListDTO usersListDTO = new UsersListDTO();
        ArrayList<UserDTO> users = new ArrayList<>();
        usersListDTO.setList(users);

        List<User> list = userRepository.findByName(name);
        for(User user : list){
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setEmail(user.getEmail());
            userDTO.setName(user.getName());
            users.add(userDTO);
        }
        return usersListDTO;

    }
    @DeleteMapping(value = "/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException();
        }
        userRepository.delete(user);
        return ResponseEntity.ok("User deleted successfully");
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginDTO loginDTO) throws UserNotFoundException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );
        User user = userRepository.findByEmail(authentication.getName());
        if (user == null)
        {
            throw new UserNotFoundException();
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwt = jwtUtilities.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthenticationResponseDTO(jwt));
    }

    @PostMapping(value = "/{userId}/energy-consumption", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> registerEnergyConsumption(
            @PathVariable String userId,
            @RequestBody EnergyConsumptionDTO energyConsumptionDTO
    ) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        EnergyConsumption energyConsumption = new EnergyConsumption(
                userId,
                energyConsumptionDTO.getDeviceName(),
                energyConsumptionDTO.getEnergyConsumed(),
                energyConsumptionDTO.getDate()
        );

        energyConsumptionRepository.save(energyConsumption);

        return ResponseEntity.ok("Energy consumption registered successfully");
    }

    @GetMapping(value = "/energy-consumption", produces = "application/json")
    public List<EnergyConsumptionDTO> getAllEnergyConsumption() {
        List<EnergyConsumption> energyConsumptionList = energyConsumptionRepository.findAll();
        List<EnergyConsumptionDTO> energyConsumptionDTOList = new ArrayList<>();

        for (EnergyConsumption energyConsumption : energyConsumptionList) {
            EnergyConsumptionDTO energyConsumptionDTO = new EnergyConsumptionDTO();
            energyConsumptionDTO.setId(energyConsumption.getId());
            energyConsumptionDTO.setUserId(energyConsumption.getUserId());
            energyConsumptionDTO.setDeviceName(energyConsumption.getDeviceName());
            energyConsumptionDTO.setEnergyConsumed(energyConsumption.getEnergyConsumed());
            energyConsumptionDTO.setDate(energyConsumption.getDate());
            energyConsumptionDTOList.add(energyConsumptionDTO);
        }

        return energyConsumptionDTOList;
    }

    @PostMapping(value = "/{userId}/surplus-energy", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> registerSurplusEnergy(
            @PathVariable String userId,
            @RequestBody SurplusEnergyDTO surplusEnergyDTO
    ) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        // Calculate total generated energy
        List<RenewableEnergy> renewableSources = renewableEnergyService.getRenewableSourcesByUserId(userId);
        double totalGeneratedEnergy = renewableSources.stream()
                .mapToDouble(RenewableEnergy::getGeneratedEnergy)
                .sum();

        // Calculate total consumed energy
        List<EnergyConsumption> energyConsumptions = energyConsumptionRepository.findByUserId(userId);
        double totalConsumedEnergy = energyConsumptions.stream()
                .mapToDouble(EnergyConsumption::getEnergyConsumed)
                .sum();

        // Calculate available surplus energy
        double availableSurplusEnergy = totalGeneratedEnergy - totalConsumedEnergy;

        if (surplusEnergyDTO.getSurplusAmount() > availableSurplusEnergy) {
            return ResponseEntity.badRequest().body("Surplus energy amount exceeds available surplus energy.");
        }

        SurplusEnergy surplusEnergy = new SurplusEnergy(
                userId,
                surplusEnergyDTO.getSurplusAmount(),
                surplusEnergyDTO.getDate()
        );

        surplusEnergyRepository.save(surplusEnergy);

        // Update or create credit record
        Optional<Credit> creditOptional = creditRepository.findByUserId(userId);
        Credit credit;
        if (creditOptional.isPresent()) {
            credit = creditOptional.get();
            credit.setCreditScore(credit.getCreditScore() + surplusEnergy.getSurplusAmount());
        } else {
            credit = new Credit(userId, surplusEnergy.getSurplusAmount());
        }
        creditRepository.save(credit);

        return ResponseEntity.ok("Surplus energy registered and credits updated successfully");
    }

    @GetMapping(value = "/surplus-energy", produces = "application/json")
    public List<SurplusEnergyDTO> getAllSurplusEnergy() {
        List<SurplusEnergy> surplusEnergyList = surplusEnergyRepository.findAll();
        List<SurplusEnergyDTO> surplusEnergyDTOList = new ArrayList<>();

        for (SurplusEnergy surplusEnergy : surplusEnergyList) {
            SurplusEnergyDTO surplusEnergyDTO = new SurplusEnergyDTO();
            surplusEnergyDTO.setId(surplusEnergy.getId());
            surplusEnergyDTO.setUserId(surplusEnergy.getUserId());
            surplusEnergyDTO.setSurplusAmount(surplusEnergy.getSurplusAmount());
            surplusEnergyDTO.setDate(surplusEnergy.getDate());
            surplusEnergyDTOList.add(surplusEnergyDTO);
        }

        return surplusEnergyDTOList;
    }

    @GetMapping(value = "/credits", produces = "application/json")
    public List<CreditDTO> getAllCredits() {
        List<Credit> creditList = creditRepository.findAll();
        List<CreditDTO> creditDTOList = new ArrayList<>();

        for (Credit credit : creditList) {
            CreditDTO creditDTO = new CreditDTO();
            creditDTO.setId(credit.getId());
            creditDTO.setUserId(credit.getUserId());
            creditDTO.setCreditScore(credit.getCreditScore());
            creditDTOList.add(creditDTO);
        }

        return creditDTOList;
    }

    @GetMapping(value = "/{userId}/credits", produces = "application/json")
    public ResponseEntity<CreditDTO> getCreditsByUserId(@PathVariable String userId) throws UserNotFoundException {
        Optional<Credit> creditOptional = creditRepository.findByUserId(userId);
        if (creditOptional.isEmpty()) {
            throw new UserNotFoundException();
        }

        Credit credit = creditOptional.get();
        CreditDTO creditDTO = new CreditDTO();
        creditDTO.setId(credit.getId());
        creditDTO.setUserId(credit.getUserId());
        creditDTO.setCreditScore(credit.getCreditScore());

        return ResponseEntity.ok(creditDTO);
    }
    @GetMapping("/{userId}/renewable-sources")
    public List<RenewableEnergy> getUserRenewableSources(@PathVariable String userId) {
        return renewableEnergyService.getRenewableSourcesByUserId(userId);
    }

    @PostMapping("/{userId}/add-renewable-source")
    public ResponseEntity<String> addRenewableSourceForUser(
            @PathVariable String userId,
            @RequestBody RenewableEnergy renewableEnergy
    ) {
        renewableEnergy.setUserId(userId);
        renewableEnergyService.addRenewableSource(renewableEnergy);
        return ResponseEntity.ok("Renewable source added successfully for user.");
    }

    @PostMapping("/{userId}/generate-energy/{sourceId}")
    public ResponseEntity<String> generateEnergyForSource(
            @PathVariable String userId,
            @PathVariable String sourceId
    ) {
        RenewableEnergy renewableEnergy = renewableEnergyService.generateEnergy(sourceId);
        return ResponseEntity.ok("Generated energy: " + renewableEnergy.getGeneratedEnergy());
    }

    @GetMapping("/{userId}/rewards")
    public List<RewardDTO> getUserRewards(@PathVariable String userId) {
        List<Reward> rewards = rewardRepository.findByUserId(userId);
        return rewards.stream().map(this::convertToRewardDTO).collect(Collectors.toList());
    }

    private RewardDTO convertToRewardDTO(Reward reward) {
        RewardDTO rewardDTO = new RewardDTO();
        rewardDTO.setId(reward.getId());
        rewardDTO.setUserId(reward.getUserId());
        rewardDTO.setPoints(reward.getPoints());
        rewardDTO.setRewardType(reward.getRewardType());
        rewardDTO.setDateEarned(reward.getDateEarned());
        return rewardDTO;
    }
    @GetMapping("/{userId}/energy-summary")
    public ResponseEntity<EnergySummaryDTO> getUserEnergySummary(@PathVariable String userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        // Calculate total generated energy
        List<RenewableEnergy> renewableSources = renewableEnergyService.getRenewableSourcesByUserId(userId);
        double totalGeneratedEnergy = renewableSources.stream()
                .mapToDouble(RenewableEnergy::getGeneratedEnergy)
                .sum();

        // Calculate total consumed energy
        List<EnergyConsumption> energyConsumptions = energyConsumptionRepository.findByUserId(userId);
        double totalConsumedEnergy = energyConsumptions.stream()
                .mapToDouble(EnergyConsumption::getEnergyConsumed)
                .sum();

        // Calculate total surplus energy
        List<SurplusEnergy> surplusEnergies = surplusEnergyRepository.findByUserId(userId);
        double totalSurplusEnergy = surplusEnergies.stream()
                .mapToDouble(SurplusEnergy::getSurplusAmount)
                .sum();

        // Create and return the energy summary
        EnergySummaryDTO energySummary = new EnergySummaryDTO();
        energySummary.setUserId(userId);
        energySummary.setTotalGeneratedEnergy(totalGeneratedEnergy);
        energySummary.setTotalConsumedEnergy(totalConsumedEnergy);
        energySummary.setTotalSurplusEnergy(totalSurplusEnergy);

        return ResponseEntity.ok(energySummary);
    }
}
