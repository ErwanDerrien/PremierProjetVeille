package com.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    }

    @Test
    void testGetPassword() {
        assertNull(emptyUser.getPassword());
        assertEquals(emptyUser.getPassword(), "password");
    }

    @Test
    void testSetId() {

    }

    @Test
    void testSetPassword() {

    }

    @Test
    void testToString() {

    }
}
