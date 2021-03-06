package com.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class UserTest {

    private User emptyUser;
    private User fullUser;

    @BeforeEach
    public void prepareSecrets() {
        emptyUser = new User();
        fullUser = new User();
        fullUser.setId("id");
        fullUser.setPassword("password");
    }

    @BeforeEach

    @Test
    void testGetId() {
        assertNull(emptyUser.getId());
        assertEquals("id", fullUser.getId());
        assertEquals("id", fullUser.getId());
    }

    @Test
    void testGetPassword() {
        assertNull(emptyUser.getPassword());
        assertEquals("password", fullUser.getPassword());
    }

    @Test
    void testSetId() {
        assertEquals("id", fullUser.getId());
        fullUser.setId("newId");
        assertEquals("newId".toLowerCase(Locale.ROOT), fullUser.getId());
    }

    @Test
    void testSetPassword() {
        assertEquals("password", fullUser.getPassword());
        fullUser.setPassword("newPassword");
        assertEquals("newPassword", fullUser.getPassword());
    }

    @Test
    void testToString() {
        assertEquals("User{id='null', password='null'}", emptyUser.toString());
        assertEquals("User{id='id', password='password'}", fullUser.toString());
    }
}
