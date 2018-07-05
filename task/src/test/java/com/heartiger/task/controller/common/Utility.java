package com.heartiger.task.controller.common;

import com.google.gson.Gson;
import com.heartiger.task.dtos.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Slf4j
public class Utility {

    private static final Gson gson = new Gson();
    private static final String successMessage = "Success";

    public static void performDeleteTest(MockMvc mockMvc, String deleteUrl) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(deleteUrl)
                .accept(MediaType.APPLICATION_JSON);

        Object[] results = parseJsonResult(mockMvc, requestBuilder);
        MvcResult result = (MvcResult) results[0];
        ResultDTO resultDTO = (ResultDTO) results[1];

        Assert.assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        Assert.assertEquals(resultDTO.getMessage(),successMessage);
    }

    public static Object[] parseJsonResult(MockMvc mockMvc, RequestBuilder requestBuilder) throws Exception {
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        ResultDTO resultDTO = new ResultDTO();
        try{
            resultDTO = gson.fromJson(result.getResponse().getContentAsString(), ResultDTO.class);
        }catch (Exception e) {
            log.error("Failed to parse json", e.getMessage());
        }

        Object[] results = new Object[2];
        results[0] = result;
        results[1] = resultDTO;

        return results;
    }
}
