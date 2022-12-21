package com.ms.demo.elastic.index.client.util;

import co.elastic.clients.elasticsearch.core.BulkRequest;
import com.ms.demo.elastic.model.IndexModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ElasticIndexUtil<T extends IndexModel> {


    public BulkRequest getBulkIndexQueryFromDocuments(String indexName, List<T> documents){
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (T document : documents) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .id(document.getId())
                            .document(document)
                    )
            );
        }
        return br.build();
    }
}
