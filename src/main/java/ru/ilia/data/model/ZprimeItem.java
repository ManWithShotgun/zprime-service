package ru.ilia.data.model;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.springframework.data.redis.core.RedisHash;

@DynamoDBTable(tableName = "ZprimeResults")
@RedisHash("ZprimeItem")
public class ZprimeItem {

    private Long id;
    private String result;
    private String mass;
    private String ksi;

    public ZprimeItem(Long id, String result, String mass, String ksi) {
        this.id = id;
        this.result = result;
        this.mass = mass;
        this.ksi = ksi;
    }

    @DynamoDBHashKey(attributeName = "Id")
    public Long getId() {
        return id;
    }

    public void setId(Long Id) {
        this.id = Id;
    }

    @DynamoDBAttribute(attributeName = "result")
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @DynamoDBAttribute(attributeName = "mass")
    public String getMass() {
        return mass;
    }

    public void setMass(String mass) {
        this.mass = mass;
    }

    @DynamoDBAttribute(attributeName = "ksi")
    public String getKsi() {
        return ksi;
    }

    public void setKsi(String ksi) {
        this.ksi = ksi;
    }
}
