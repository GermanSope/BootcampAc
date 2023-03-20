package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
      return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    };

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    };


    //current hace referencia al cliente q esta logueado en el momento
    //Autentication trae el cliente logueado(usuario o contraseña).
    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName()).get();
        return new ClientDTO(client);
    }

    @GetMapping("/clients/email/{email}")
    public ClientDTO getClient(@PathVariable String email) {
        return clientRepository.findByEmail(email).map(ClientDTO::new).orElse(null);
    };

    @GetMapping("/clients/firstname/{firstname}")
    public List<ClientDTO> getClientByName(@PathVariable String firstname) {
        return clientRepository.findByFirstName(firstname).stream().map(ClientDTO::new).collect(toList());
    };

    @GetMapping("/clients/emailAndName/{email}{firstName}")
    public ClientDTO getClientByEmailAndName(@PathVariable String email, @PathVariable String firstName) {
        return new ClientDTO(clientRepository.findByEmailAndFirstName(email,firstName).get());
    };

    @GetMapping("/clients/lastname/{lastName}")
    public List<ClientDTO> getClientByLastname(@PathVariable String lastName) {
        return clientRepository.findByLastName(lastName).stream().map(ClientDTO::new).collect(toList());
    };

    @PostMapping("/clients")
    public ResponseEntity<Object> createClient(@RequestParam String firstName, @RequestParam String lastName,
                                               @RequestParam String email, @RequestParam String password){
        //Verifica si los parametros no estan vacios
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        //Verifica si el mail ya existe
        if (clientRepository.findByEmail(email).isPresent()){
            return new ResponseEntity<>("UserName already exists", HttpStatus.FORBIDDEN);
        }

        // Verifica que la contraseña sea major a 8 caracteres.
        if(password.length()<=8){
            return new ResponseEntity<>("Contraseña poco confiable", HttpStatus.FORBIDDEN);
        }

        try {
            Client client =new Client(firstName, lastName, email, passwordEncoder.encode(password));
            Account account;
            do {
                //account = new Account(4/0, client); //No permite el loggin pero continua el programa
                account = new Account(0.0, client);
            } while (accountRepository.findByNumber(account.getNumber()).isPresent());
            clientRepository.save(client);
            accountRepository.save(account);
            //Si todó salió bien. Retorno un estado CREATED que es correcto
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
