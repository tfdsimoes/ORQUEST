package com.orquest.registerhours.controller.rest;

import com.orquest.registerhours.controller.resources.register.RegisterNewRequest;
import com.orquest.registerhours.controller.resources.register.RegisterUpdateRequest;
import com.orquest.registerhours.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for register operations
 */
@RestController
@Slf4j
@RequestMapping( value = "register", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE } )
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController( RegisterService registerService ) {
        this.registerService = registerService;
    }

    /**
     * Controller that handles new registers
     *
     * @param request List of {@link RegisterNewRequest} New registers to insert
     * @return List of Strings with the ids of registers inserted
     */
    @PostMapping()
    public ResponseEntity<List<String>> newRegister( @RequestBody List<RegisterNewRequest> request ) {
        log.info("Insert new registers {}", request);
        return ResponseEntity.ok(registerService.newRegisters(request));
    }

    /**
     * Controller that handles the update of a register
     *
     * @param id Id of register to change
     * @param request {@link RegisterUpdateRequest} Data to be updated
     * @return Id of register that updated
     */
    @PutMapping( value = "/{id}" )
    public ResponseEntity<String> updateRegister( @PathVariable String id, @RequestBody RegisterUpdateRequest request ) {
        log.info("Update register {} with {}", id, request);
        return ResponseEntity.ok( registerService.updateRegister(id, request) );
    }

    /**
     * Controller that deletes a register
     *
     * @param id Id of register to delete
     */
    @DeleteMapping( value = "/{id}" )
    public ResponseEntity<Void> deleteRegister( @PathVariable String id ) {
        log.info("Delete register {}", id);
        registerService.deleteRegister(id);
        return ResponseEntity.ok().build();
    }
}
