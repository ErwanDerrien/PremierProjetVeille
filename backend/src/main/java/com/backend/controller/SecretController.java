package com.backend.controller;

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
import java.net.URI;
import java.net.URISyntaxException;
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
    public ResponseEntity<String> create (@RequestBody Secret secret, @RequestParam String userId, UriComponentsBuilder uriComponentsBuilder) {
        try {
            String secretId = secretService.create(userId, secret);

            UriComponents uriComponent = uriComponentsBuilder.path("/{id}").buildAndExpand(secretId);

            return ResponseEntity.status(201).location(
                    uriComponent.toUri()
            ).body(secretId);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @PostMapping
    public String create(@RequestBody Secret secret, @RequestParam String userId) {
        try {
            return secretService.create(userId, secret);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping(value = "get/{secretId}")
    public Secret get(@PathVariable("secretId") String secretId) {
        try {
            return secretService.get("TOKEN", secretId, true);
        } catch (ForbiddenAccess forbiddenAccess) {
            forbiddenAccess.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "getAllEncryptedUsersSecretList/{userId}")
    public List<Secret> getAllEncryptedUsersSecretList(@PathVariable("userId") String userId) {
        return secretService.getAllEncryptedUsersSecretList(userId);
    }

    @RequestMapping(value = "/update")
    public Secret update(@RequestBody Secret secret) {
        try {
            return secretService.update("TOKEN", secret);
        } catch (ForbiddenAccess forbiddenAccess) {
            forbiddenAccess.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "/delete/{secretId}")
    public void delete(@PathVariable("secretId") String secretId) {
        try {
            secretService.delete("TOKEN", secretId);
        } catch (ForbiddenAccess forbiddenAccess) {
            forbiddenAccess.printStackTrace();
        }
    }

    @GetMapping(value = "/deleteAllUserSecrets/{userId}")
    public int deleteAllUserSecrets(@PathVariable("userId") String userId) {
        return secretService.deleteAllUserSecrets(userId);
    }

}














