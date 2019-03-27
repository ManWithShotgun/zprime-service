package ru.ilia.data.repository.impl;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.context.annotation.Profile;
import ru.ilia.data.repository.CustomHotelRepository;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

// TODO: create class and use @ConfigurationProperties
@Profile("suspend")
public class CustomHotelRepositoryImpl implements CustomHotelRepository {

    private static final String TABLE_NAME = "Hotels";

    private final DynamoDB dynamoDB;

    public CustomHotelRepositoryImpl(DynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }

    @Override
    public void createTable() {

        try {
            Table table = dynamoDB.createTable(TABLE_NAME,
                    Collections.singletonList(new KeySchemaElement("id", KeyType.HASH)),
                    Collections.singletonList(new AttributeDefinition("id", ScalarAttributeType.S)),
                    new ProvisionedThroughput(10L, 10L));
            table.waitForActive();

            // TODO: add @Slf4j
//            log.info("Success. Table status: " + table.getDescription().getTableStatus());
        } catch (ResourceInUseException e) {
            // TODO: add @Slf4j
//            log.error("Table already Exists: {}", e.getMessage());
            // TODO: create spring exception handler @ControllerAdvice
//            throw new DuplicateTableException(e);

        } catch (Exception e) {
            // TODO: add @Slf4j
//            log.error("Unable to create table: {}", e.getMessage());
            // TODO: create spring exception handler @ControllerAdvice
//            throw new GenericDynamoDBException(e);
        }
    }

    @Override
    public void loadData() throws IOException {
        Table table = dynamoDB.getTable(TABLE_NAME);

        JsonParser parser = new JsonFactory().createParser(new File("hotels.json"));

        JsonNode rootNode = new ObjectMapper().readTree(parser);
        Iterator<JsonNode> iterator = rootNode.iterator();

        ObjectNode currentNode;

        while (iterator.hasNext()) {
            currentNode = (ObjectNode) iterator.next();

            String id = currentNode.path("id").asText();
            String name = currentNode.path("name").asText();
            String geo = currentNode.path("geo").toString();

            try {
                table.putItem(new Item()
                        .withPrimaryKey("id", id)
                        .withString("name", name)
                        .withJSON("geo", geo));
                // TODO: add @Slf4j
//                log.info("PutItem succeeded " + id + " " + name);
            } catch (Exception e) {
                // TODO: add @Slf4j
//                log.error("Unable to add hotel: {} - {}: \n{}", id, name, e.getMessage());
                break;
            }
        }
        parser.close();
    }

}
