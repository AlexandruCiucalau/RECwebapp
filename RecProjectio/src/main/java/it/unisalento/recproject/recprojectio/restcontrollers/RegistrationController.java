package it.unisalento.recproject.recprojectio.restcontrollers;

import it.unisalento.recproject.recprojectio.di.IRenewableSource;
import it.unisalento.recproject.recprojectio.domain.User;
import it.unisalento.recproject.recprojectio.dto.UserDTO;
import it.unisalento.recproject.recprojectio.dto.UsersListDTO;
import it.unisalento.recproject.recprojectio.exceptions.UserNotFoundException;
import it.unisalento.recproject.recprojectio.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.unisalento.recproject.recprojectio.configuration.SecurityConfig.passwordEncoder;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping( method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO post(@RequestBody UserDTO userDTO)
    {
        User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPassword(passwordEncoder().encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole());
        user = userRepository.save(user);
        userDTO.setId(user.getId());
       //
        return userDTO;
    }


}
