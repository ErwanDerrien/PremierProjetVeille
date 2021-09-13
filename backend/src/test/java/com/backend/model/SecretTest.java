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
        assertNull(emptySecret.getName());
        assertEquals("name", fullSecret.getName());
    }

    @Test
    void testGetUserId() {
        assertNull(emptySecret.getUserId());
        assertEquals("userId", fullSecret.getUserId());
    }

    @Test
    void testSetContent() {
        assertEquals("content", fullSecret.getContent());
        fullSecret.setContent("reset");
        assertEquals("reset", fullSecret.getContent());
    }

    @Test
    void testSetId() {
        assertEquals("id", fullSecret.getId());
        fullSecret.setId("newId");
        assertEquals("newId", fullSecret.getId());
    }

    @Test
    void testSetName() {
        assertEquals("name", fullSecret.getName());
        fullSecret.setName("newName");
        assertEquals("newName", fullSecret.getName());
    }

    @Test
    void testSetUserId() {
        assertEquals("name", fullSecret.getName());
        fullSecret.setName("newName");
        assertEquals("newName", fullSecret.getName());
    }

    @Test
    void testToString() {
        assertEquals("Secret{id='null', userId='null', name='null', content='null', seed=null}",
                emptySecret.toString());
        assertEquals("Secret{id='id', userId='userId', name='name', content='content', seed=null}",
                fullSecret.toString());
    }
}
