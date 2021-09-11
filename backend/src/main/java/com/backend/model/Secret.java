package com.backend.model;

import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class Secret {
    private String id;

    private String userId;
    private String name;
    private String content;
    private List<Byte> seed;

    public Secret() {}

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDbSortKey
    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public List<Byte> getSeed() { return seed; }

    public void setSeed(List<Byte> seed) { this.seed = seed; }

    @Override
    public String toString() {
        return "Secret{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", seed=" + seed +
                '}';
    }
}
