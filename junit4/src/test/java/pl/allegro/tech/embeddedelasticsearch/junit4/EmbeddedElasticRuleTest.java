package pl.allegro.tech.embeddedelasticsearch.junit4;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.ClassRule;
import org.junit.Test;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static pl.allegro.tech.embeddedelasticsearch.PopularProperties.HTTP_PORT;

public class EmbeddedElasticRuleTest {

    private static final String ELASTIC_VERSION = "6.3.0";
    private static final int HTTP_PORT_VALUE = EmbeddedElasticRule.getAvailablePort();
    @ClassRule
    public static EmbeddedElasticRule elasticRule = new EmbeddedElasticRule(
            EmbeddedElastic.builder()
                    .withElasticVersion(ELASTIC_VERSION)
                    .withSetting(HTTP_PORT, HTTP_PORT_VALUE)
    );

    @Test
    public void verifyEmbeddedElasticIsRunning() throws IOException {
        EmbeddedElastic elastic = elasticRule.getElastic();
        String indexName = "test";
        elastic.index(indexName, "test_type", "{\"message\": \"Hello World\"}");
        RestHighLevelClient client = createClient();
        SearchRequest searchRequest = new SearchRequest(indexName)
                .source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));
        SearchResponse search = client.search(searchRequest);
        assertEquals(1L, search.getHits().getTotalHits());
    }

    private static RestHighLevelClient createClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", HTTP_PORT_VALUE)));
    }
}