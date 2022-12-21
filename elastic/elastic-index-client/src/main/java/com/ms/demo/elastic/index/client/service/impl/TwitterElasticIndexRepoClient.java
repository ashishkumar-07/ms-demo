package com.ms.demo.elastic.index.client.service.impl;

import com.ms.demo.elastic.index.client.repository.TwitterElasticSearchIndexRepository;
import com.ms.demo.elastic.index.client.service.ElasticIndexClient;
import com.ms.demo.elastic.model.impl.TwitterIndexModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "true", matchIfMissing = true)
public class TwitterElasticIndexRepoClient implements ElasticIndexClient<TwitterIndexModel> {

    private final TwitterElasticSearchIndexRepository twitterElasticSearchIndexRepository;
    @Override
    public Set<String> save(List<TwitterIndexModel> documents) throws IOException {
        List<TwitterIndexModel> repositoryResponse =
                (List<TwitterIndexModel>) twitterElasticSearchIndexRepository.saveAll(documents);
        Set<String> ids = repositoryResponse.stream().map(TwitterIndexModel::getId).collect(Collectors.toSet());
        log.info("Documents indexed successfully with type: {} and ids: {}", TwitterIndexModel.class.getName(), ids);
        return ids;
    }
}
