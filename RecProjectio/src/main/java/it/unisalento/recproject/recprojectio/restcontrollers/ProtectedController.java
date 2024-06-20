package it.unisalento.recproject.recprojectio.restcontrollers;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.unisalento.recproject.recprojectio.security.JwtUtilities;
import it.unisalento.recproject.recprojectio.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Date;

@RestController
@RequestMapping("/api/users/authenticate")
public class ProtectedController {

    @Autowired
    private JwtUtilities jwtUtilities;

    @RequestMapping(method= RequestMethod.GET)
    public String protectedEndpoint(@RequestHeader("Authorization") String token) {
        try {
            // Rimuovi "Bearer " dalla stringa dell'header per ottenere solo il token JWT.
            String jwtToken = token.substring(7);

            // Verifica e decodifica il token JWT utilizzando il segreto condiviso.
            Date expirationDate = jwtUtilities.extractExpiration(jwtToken);

            if (expirationDate.before(new Date())) {
                // Token scaduto
                return "Token scaduto";
            }

            String username = jwtUtilities.extractUsername(jwtToken);

            // Il token è valido, puoi eseguire le operazioni protette qui.
            return "Ciao "+username;
        } catch (Exception e) {
            // Gestione delle eccezioni nel caso in cui il token non sia valido o non sia stato fornito.
            return "Errore nella verifica del token: " + e.getMessage();
        }
    }
}
