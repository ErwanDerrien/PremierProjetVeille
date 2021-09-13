package com.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SecretTest {

    private Secret emptySecret;
    private Secret fullSecret;

    @BeforeEach
    public void prepareSecrets() {
        emptySecret = new Secret();
        fullSecret = new Secret();
        fullSecret.setId("id");
        fullSecret.setName("name");
        fullSecret.setUserId("userId");
        fullSecret.setContent("content");
    }

    @Test
    void testGetContent() {
        assertNull(emptySecret.getContent());
        assertEquals("content", fullSecret.getContent());
    }

    @Test
    void testGetId() {
        assertNull(emptySecret.getId());
        assertEquals("id", fullSecret.getId());
    }

    @Test
    void testGetName() {

    }

    @Test
    void testGetSeed() {

    }

    @Test
    void testGetUserId() {

    }

    @Test
    void testSetContent() {
        assertEquals("content", fullSecret.getContent());
        fullSecret.setContent("reset");
        assertEquals("reset", fullSecret.getContent());
    }

    @Test
    void testSetId() {

    }

    @Test
    void testSetName() {

    }

    @Test
    void testSetSeed() {

    }

    @Test
    void testSetUserId() {

    }

    @Test
    void testToString() {

    }
}
