package com.backend.controller;

import com.backend.exception.DoesntExist;
import com.backend.exception.ForbiddenAccess;
import com.backend.model.Secret;
import com.backend.service.SecretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/secret")
public class SecretController {

    @Autowired
    SecretService secretService;


    @PostMapping
    public ResponseEntity<String> create(@RequestBody Secret secret, @RequestParam String userId, UriComponentsBuilder uriComponentsBuilder) {
        try {
            String secretId = secretService.create(userId, secret);

            UriComponents uriComponent = uriComponentsBuilder.path("/api/v1/secret/{id}").buildAndExpand(secretId);

            return ResponseEntity.status(201).location(uriComponent.toUri()).build();

        } catch (InvalidKeySpecException | NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException | InvalidAlgorithmParameterException | NoSuchPaddingException e) {
            return ResponseEntity.status(503).body("Unexpected error");
        }
    }

    @GetMapping(value = "/{secretId}", produces = "application/json")
    public ResponseEntity<Secret> get(@PathVariable("secretId") String secretId, @RequestParam("decrypt") Boolean decrypt, @RequestParam("userId") String userId, UriComponentsBuilder uriComponentsBuilder) {
        try {
            Secret secret = secretService.get(userId, secretId, decrypt);

            UriComponents uriComponent = uriComponentsBuilder.buildAndExpand(secret);

            return ResponseEntity.status(200).location(uriComponent.toUri()).body(secret);

        } catch (ForbiddenAccess forbiddenAccess) {
            return ResponseEntity.status(401).build();
        } catch (DoesntExist doesntExist) {
            return ResponseEntity.status(404).build();
        } catch (BadPaddingException | InvalidAlgorithmParameterException | InvalidKeySpecException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return ResponseEntity.status(503).build();
        }
    }

    @GetMapping(value = "/getAllEncryptedUsersSecretList")
    public ResponseEntity<List<Secret>> getAllSecrets(@RequestParam("userId") String userId, UriComponentsBuilder uriComponentsBuilder) {
        try {
            List<Secret> allSecretsOfUser = secretService.getAllEncryptedUsersSecretList(userId);

            UriComponents uriComponent = uriComponentsBuilder.buildAndExpand(allSecretsOfUser);

            return ResponseEntity.status(200).location(uriComponent.toUri()).body(allSecretsOfUser);

        } catch (Exception e) {
            return ResponseEntity.status(503).build();
        }
    }

    @PutMapping(value = "/{secretId}")
    public ResponseEntity<Void> update(@PathVariable("secretId") String secretId, @RequestParam("userId") String userId, @RequestBody Secret secret) {
        try {
            secretService.update(userId, secret);
            return ResponseEntity.status(200).build();

        } catch (ForbiddenAccess forbiddenAccess) {
            forbiddenAccess.printStackTrace();
            return ResponseEntity.status(401).build();
        } catch (InvalidKeySpecException | IllegalBlockSizeException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | NoSuchAlgorithmException e) {
            return ResponseEntity.status(503).build();
        } catch (DoesntExist doesntExist) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping(value = "/{secretId}")
    public ResponseEntity<Void> delete(@RequestParam("userId") String userId, @PathVariable("secretId") String secretId) {
        try {
            secretService.delete(userId, secretId);
            return ResponseEntity.status(200).build();

        } catch (ForbiddenAccess forbiddenAccess) {
            forbiddenAccess.printStackTrace();
            return ResponseEntity.status(401).build();
        } catch (DoesntExist doesntExist) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping(value = "/deleteAllUserSecrets/{userId}")
    public int deleteAllUserSecrets(@PathVariable("userId") String userId) {
        return secretService.deleteAllUserSecrets(userId);
    }

}














