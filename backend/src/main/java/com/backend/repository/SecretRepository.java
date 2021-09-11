package com.backend.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.backend.model.Secret;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Repository
public class SecretRepository {

    @Autowired
    private DynamoDbEnhancedClient dynamoDbenhancedClient;

    // Store the secret item in the database
    public void save(final Secret secret) {
        getTable().putItem(secret);
    }

    // Retrieve a single secret item from the database
    public Secret getByIds(final String secretId, final String userId) {
        // Construct the key with partition and sort key
        Key key = Key.builder().partitionValue(secretId).sortValue(userId).build();

        Secret secret = getTable().getItem(key);

        return secret;
    }

    public List<Secret> findByUserId(final String userId) {
        AttributeValue secretIdAttributeValue = AttributeValue.builder().s("").build();
        AttributeValue userIdAttributeValue = AttributeValue.builder().s(userId).build();

        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":secretId", secretIdAttributeValue);
        expressionValues.put(":userId", userIdAttributeValue);

        Expression expression = Expression.builder()
                .expression("id <> :secretId and userId = :userId")
                .expressionValues(expressionValues)
                .build();

        // Get items in the Customer table and write out the ID value
        PageIterable<Secret> results = getTable().scan(r -> r.filterExpression(expression));

        List<Secret> allSecretsOfUser = new ArrayList<>();
        for (Secret secret: results.items()) {
            allSecretsOfUser.add(secret);
        }

        return allSecretsOfUser;
    }

    // Delete a single user item from the database
    public void deleteByIds(final String secretId, final String userId) {
        // Construct the key with partition key
        Key key = Key.builder().partitionValue(secretId).sortValue(userId).build();

        DeleteItemEnhancedRequest deleteRequest = DeleteItemEnhancedRequest.builder().key(key).build();

        getTable().deleteItem(deleteRequest);
    }

    private DynamoDbTable<Secret> getTable() {
        // Create a tablescheme to scan our bean class secret
        return dynamoDbenhancedClient.table("Secret", TableSchema.fromBean(Secret.class));
    }

}
