package com.backend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.backend.model.User;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;

@Repository
public class UserRepository {
    @Autowired
    private DynamoDbEnhancedClient dynamoDbenhancedClient;

    // Store the user item in the database
    public void save(final User user) {
        getTable().putItem(user);
    }

    // Retrieve a single user item from the database
    public User getById(final String userId) {
        // Construct the key with partition key
        Key key = Key.builder().partitionValue(userId).build();

        User user = (User) getTable().getItem(key);

        return user;
    }

    // Delete a single user item from the database
    public void deleteById(final String userId) {
        // Construct the key with partition key
        Key key = Key.builder().partitionValue(userId).build();

        DeleteItemEnhancedRequest deleteRequest = DeleteItemEnhancedRequest.builder().key(key).build();

        getTable().deleteItem(deleteRequest);
    }

    private DynamoDbTable<User> getTable() {
        // Create a tablescheme to scan our bean class user
        return dynamoDbenhancedClient.table("User", TableSchema.fromBean(User.class));
    }

}
