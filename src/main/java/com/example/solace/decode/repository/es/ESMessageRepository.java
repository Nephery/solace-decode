package com.example.solace.decode.repository.es;

import com.example.solace.decode.model.es.ESMessage;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ESMessageRepository extends ElasticsearchRepository<ESMessage, String> {
    @Query("{\n" +
                        "\t\"bool\": {\n" +
                        "\t\t\"must\": {\n" +
                        "\t\t\t\"multi_match\": {\n" +
                        "\t\t\t\t\"fields\": [\n" +
                        "\t\t\t\t\t\"payload\"\n" +
                        "\t\t\t\t],\n" +
                        "\t\t\t\t\"query\": \"?0\",\n" +
                        "\t\t\t\t\"fuzziness\": \"AUTO\"\n" +
                        "\t\t\t}\n" +
                        "\t\t},\n" +
                        "\t\t\"should\": [{\n" +
                        "\t\t\t\t\"rank_feature\": {\n" +
                        "\t\t\t\t\t\"field\": \"search_clicks\"\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"term\": {\n" +
                        "\t\t\t\t\t\"sendingUserId\": \"?1\"\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t}\n" +
                        "\t\t]\n" +
                        "\t}\n" +
            "}")
    List<ESMessage> customFind(String payload, String sendingUserId);
}
