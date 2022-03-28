package io.security.SpringSecurityJwt.Controller;

import io.security.SpringSecurityJwt.Authentication.AuthenticationRequest;
import io.security.SpringSecurityJwt.Authentication.AuthenticationResponse;
import io.security.SpringSecurityJwt.JWT.JwtUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
public class IndexController {

    private AuthenticationManager authenticationManager;

    private UserDetailsService userDetailsService;

    private JwtUtill jwtTokenUtill;

    public IndexController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtill jwtUtill) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtill = jwtUtill;
    }

    @GetMapping("/hello")
    public String Hello(){
        return "HELLO WORLD";
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    //@PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e){
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails= userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt= jwtTokenUtill.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));

    }
}
