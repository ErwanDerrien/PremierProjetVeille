package com.backend.controller;

import com.backend.exception.ForbiddenAccess;
import com.backend.model.Secret;
import com.backend.model.User;
import com.backend.service.SecretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
public class SecretController {

    @Autowired
    SecretService secretService;

    @RequestMapping(value = "/create/", method = RequestMethod.POST)
    public Secret update(@RequestBody Secret secret) {
        try {
            return secretService.update("TOKEN", secret);
        } catch (ForbiddenAccess forbiddenAccess) {
            forbiddenAccess.printStackTrace();
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
        }
        return null;
    }

    @GetMapping("get/{secretId}")
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
}
