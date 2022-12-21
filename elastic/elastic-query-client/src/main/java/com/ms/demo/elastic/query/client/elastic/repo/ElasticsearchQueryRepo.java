package com.ms.demo.elastic.query.client.elastic.repo;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.ms.demo.elastic.model.IndexModel;
import com.ms.demo.elastic.query.client.exception.ElasticQueryClientException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;


@Slf4j
public abstract class ElasticsearchQueryRepo<T extends IndexModel>  {
    private final ElasticsearchClient elasticsearchClient;
    private final Class<T> type;

    protected ElasticsearchQueryRepo(ElasticsearchClient elasticsearchClient, Class<T> type) {
        this.elasticsearchClient = elasticsearchClient;
        this.type = type;
    }

    public T getDocumentById(String index, String id) throws IOException, ElasticQueryClientException {
        GetResponse<T> response = elasticsearchClient.get(g -> g
                        .index(index)
                        .id(id),
                        type);
        if (!response.found()) {
            log.error("No document found at elasticsearch with id {}", id);
            throw new ElasticQueryClientException("No document found at elasticsearch with id " + id);
        }
        log.info("Document with id {} retrieved successfully", response.source().getId());
        return response.source();
    }

    public List<T> getDocumentsAcrossAllFields(String index, String textToSearch) throws IOException {
        Query query = QueryBuilders.queryString(m -> m.query(textToSearch));
        return searchAll(index, query, "{} of documents with text {} retrieved successfully", textToSearch);
    }

    public List<T> getDocumentsByMatchField(String index, String field, String textToSearch) throws IOException {
        Query query = BoolQuery.of(bool->
            bool.must(must->
                        must.match(matchQ->{
                                matchQ.field(field);
                                matchQ.query(textToSearch);
                                return matchQ;}
                )
            )
        )._toQuery();
        return searchAll(index, query, "{} of documents with text {} retrieved successfully", textToSearch);
    }


    private List<T> searchAll(String index, Query query, String logMessage, Object... logParams) throws IOException {
        SearchResponse<T> search = elasticsearchClient.search(sr -> sr
                        .index(index)
                        .query(query),
                type);

        log.info(logMessage, search.hits().total(), logParams);
        return search.hits().hits().stream().map(h->h.source()).toList();
    }
}
