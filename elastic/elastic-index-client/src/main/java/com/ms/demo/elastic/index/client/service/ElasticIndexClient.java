package com.ms.demo.elastic.index.client.service;

import com.ms.demo.elastic.model.IndexModel;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ElasticIndexClient <T extends IndexModel> {
    Set<String> save(List<T> documents) throws IOException;
}
