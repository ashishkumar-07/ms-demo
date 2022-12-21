package com.ms.demo.elastic.index.client.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.ms.demo.config.ElasticConfigData;
import com.ms.demo.elastic.index.client.service.ElasticIndexClient;
import com.ms.demo.elastic.index.client.util.ElasticIndexUtil;
import com.ms.demo.elastic.model.impl.TwitterIndexModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue ="false")
public class TwitterElasticIndexClient implements ElasticIndexClient<TwitterIndexModel> {
    private ElasticIndexUtil<TwitterIndexModel> elasticIndexUtil;
    private ElasticsearchClient elasticsearchClient;
    private ElasticConfigData elasticConfigData;
    @Override
    public Set<String> save(List<TwitterIndexModel> documents) throws IOException {
        BulkRequest bulkIndex = elasticIndexUtil.getBulkIndexQueryFromDocuments(elasticConfigData.getIndexName(), documents);
        BulkResponse result = elasticsearchClient.bulk(bulkIndex);
        logErrorsIfAny(result);
        Set<String> ids = result.items().stream().filter(i -> i.error() == null).map(i -> i.id()).collect(Collectors.toSet());
        log.info("Documents indexed successfully with type: {} and ids: {}", TwitterIndexModel.class.getName(),
                ids);
        return ids;
    }

    private void logErrorsIfAny(BulkResponse result) {
        // Log errors, if any
        if (result.errors()) {
            log.error("Bulk had errors");
            for (BulkResponseItem item: result.items()) {
                if (item.error() != null) {
                    log.error(item.error().reason());
                }
            }
        }
    }
}
