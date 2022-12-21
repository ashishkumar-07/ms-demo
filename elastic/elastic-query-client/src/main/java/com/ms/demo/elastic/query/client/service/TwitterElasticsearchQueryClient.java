package com.ms.demo.elastic.query.client.service;

import com.ms.demo.config.ElasticConfigData;
import com.ms.demo.elastic.model.IndexModel;
import com.ms.demo.elastic.query.client.elastic.repo.ElasticsearchQueryRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TwitterElasticsearchQueryClient<T extends IndexModel> implements ElasticSearchQueryClient<T> {

    private ElasticsearchQueryRepo<T> elasticSearchQuery;
    private ElasticConfigData elasticConfigData;


    @Override
    public T getIndexModelById(String Id) throws Exception {
        return elasticSearchQuery.getDocumentById(elasticConfigData.getIndexName(), Id);
    }

    @Override
    public List<T> getIndexModelsByText(String text) throws Exception {
        return elasticSearchQuery.getDocumentsAcrossAllFields(elasticConfigData.getIndexName(), text);
    }

    @Override
    public List<T> getAllIndexModels(String fieldName, String text) throws Exception {
        return elasticSearchQuery.getDocumentsByMatchField(elasticConfigData.getIndexName(),fieldName , text);
    }
}
