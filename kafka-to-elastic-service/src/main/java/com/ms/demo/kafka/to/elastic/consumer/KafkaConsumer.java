package com.ms.demo.kafka.to.elastic.consumer;

import com.ms.demo.kafka.avro.model.TwitterAvroModel;
import org.apache.avro.specific.SpecificRecordBase;

import java.io.Serializable;
import java.util.List;

public interface KafkaConsumer <K extends Serializable, V extends SpecificRecordBase> {

    void receive(List<V> messages, List<K> keys, List<Integer> partitions, List<Long> offset);
}
