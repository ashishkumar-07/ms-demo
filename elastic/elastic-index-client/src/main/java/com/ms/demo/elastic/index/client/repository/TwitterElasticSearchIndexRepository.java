package com.ms.demo.elastic.index.client.repository;

import com.ms.demo.elastic.model.IndexModel;
import com.ms.demo.elastic.model.impl.TwitterIndexModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitterElasticSearchIndexRepository extends ElasticsearchRepository<TwitterIndexModel, String> {
}
