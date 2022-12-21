package com.ms.demo.elastic.query.client.service;

import com.ms.demo.common.util.CollectionUtil;
import com.ms.demo.elastic.model.impl.TwitterIndexModel;
import com.ms.demo.elastic.query.client.elastic.repo.TwitterSpringElasticQueryRepo;
import com.ms.demo.elastic.query.client.exception.ElasticQueryClientException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Primary
public class TwitterSpringDataElasticClient implements ElasticSearchQueryClient<TwitterIndexModel> {
    private TwitterSpringElasticQueryRepo  twitterSpringElasticQueryRepo;
    @Override
    public TwitterIndexModel getIndexModelById(String Id) throws Exception {
        return twitterSpringElasticQueryRepo.findById(Id).orElseThrow(()->
                 new ElasticQueryClientException("No document fund with Id "+ Id));
    }

    @Override
    public List<TwitterIndexModel> getIndexModelsByText(String text) throws Exception {
        List<TwitterIndexModel> models = twitterSpringElasticQueryRepo.findByText(text);
        log.info("{} no of documents found with text {}", models.size(), text);
        return models;
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels(String fieldName, String text) throws Exception {
        List<TwitterIndexModel> models = CollectionUtil.getInstance().getCollectionFromIterable(twitterSpringElasticQueryRepo.findAll());
        log.info("{} no of documents found with text {}", models.size(), text);
        return models;
    }
}
