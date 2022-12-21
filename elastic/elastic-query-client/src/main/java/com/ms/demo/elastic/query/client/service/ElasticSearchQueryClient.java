package com.ms.demo.elastic.query.client.service;


import com.ms.demo.elastic.model.IndexModel;
import com.ms.demo.elastic.query.client.exception.ElasticQueryClientException;

import java.io.IOException;
import java.util.List;

public interface ElasticSearchQueryClient <T extends IndexModel> {
    public T getIndexModelById(String Id) throws Exception;
    public List<T> getIndexModelsByText(String text) throws Exception;
    public List<T> getAllIndexModels(String fieldName, String text) throws Exception ;
}
