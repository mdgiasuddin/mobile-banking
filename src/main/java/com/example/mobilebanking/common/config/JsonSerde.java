package com.example.mobilebanking.common.config;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import tools.jackson.databind.ObjectMapper;

public class JsonSerde<T> implements Serde<T> {

    private final ObjectMapper mapper;
    private final Class<T> clazz;

    public JsonSerde(Class<T> clazz, ObjectMapper mapper) {
        this.clazz = clazz;
        this.mapper = mapper;
    }

    @Override
    public Serializer<T> serializer() {
        return (topic, data) -> mapper.writeValueAsBytes(data);
    }

    @Override
    public Deserializer<T> deserializer() {
        return (topic, bytes) -> mapper.readValue(bytes, clazz);
    }
}

