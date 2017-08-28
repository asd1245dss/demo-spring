package com.wpg.demo.spring.springframework.utility;

import com.jayway.jsonpath.JsonPath;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author ChangWei Li
 * @version 2017-08-28 09:56
 */
public class JSONPathTest {

    private String JSON_SAMPLE_STRING =
            "{id:1,name:\"Joe\",friends:[{id:2,name:\"Pat\",pets:[\"dog\"]},{id:3,name:\"Sue\",pets:[\"bird\",\"fish\"]}],pets:[]}";

    @Test
    public void test_jsonpath_read_a_json_property() {
        String expectedFirstFriendFirstPet = "dog";

        List<String> actualFirstFriendFirstPet = JsonPath.read(JSON_SAMPLE_STRING, "$.friends[0].pets[0]");

        Assert.assertEquals(expectedFirstFriendFirstPet, actualFirstFriendFirstPet.toString());
    }

    @Test
    public void test_jsonpath_fuzzy_query() {
        String expectedAllFriendsPets = "[[\"dog\"],[\"bird\",\"fish\"]]";

        List<String> actualAllFriendsPets = JsonPath.read(JSON_SAMPLE_STRING, "$.friends..pets");

        Assert.assertEquals(expectedAllFriendsPets, actualAllFriendsPets.toString());
    }

}
