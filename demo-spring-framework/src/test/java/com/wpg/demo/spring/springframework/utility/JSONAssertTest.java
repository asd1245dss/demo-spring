package com.wpg.demo.spring.springframework.utility;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * @author ChangWei Li
 * @version 2017-08-28 09:42
 */
public class JSONAssertTest {

    @Test(expected = AssertionError.class)
    public void test_jsonassert_with_error_message() throws JSONException {
        String expected =
                "{id:1,name:\"Joe\",friends:[{id:2,name:\"Pat\",pets:[\"dog\"]},{id:3,name:\"Sue\",pets:[\"bird\",\"fish\"]}],pets:[]}";
        String actual =
                "{id:1,name:\"Joe\",friends:[{id:2,name:\"Pat\",pets:[\"dog\"]},{id:3,name:\"Sue\",pets:[\"cat\",\"fish\"]}],pets:[]}";

        JSONAssert.assertEquals(expected, actual, false);
    }

}
