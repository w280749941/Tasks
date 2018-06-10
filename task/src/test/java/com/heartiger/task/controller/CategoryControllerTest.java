package com.heartiger.task.controller;

import com.google.gson.Gson;
import com.heartiger.task.TaskApplicationTests;
import com.heartiger.task.dto.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;

import static com.heartiger.task.controller.common.Utility.parseJsonResult;
import static com.heartiger.task.controller.common.Utility.performDeleteTest;
import static org.junit.Assert.*;

@Slf4j
@AutoConfigureMockMvc
public class CategoryControllerTest extends TaskApplicationTests {

    private String createUrl;
    private String getUrl;
    private String searchUrl;
    private String editUrl;
    private String deleteUrl;

    @Before
    public void setup(){

        int categoryId = 1;
        int userId = 1;
        createUrl = "/api/categories/new";
        getUrl = "/api/categories/user/" + userId;
        searchUrl = String.format("/api/categories/search?cid=%s&oid=%s",categoryId,userId);
        editUrl = "/api/categories/edit/" + categoryId;
        deleteUrl = String.format("/api/categories/delete?cid=%s&oid=%s",categoryId,userId);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllCategoriesShouldReturnStatusCode200AndNotNull() throws Exception {
        performGetCategoryTest(getUrl);
    }


    @Test
    public void findCategoryByIdAndUserIdShouldReturnStatusCode200AndNotNull() throws Exception {
        performGetCategoryTest(searchUrl);
    }

    @Test
    @Transactional
    public void createCategoryShouldReturnStatusCode200AndNotNull() throws Exception {
        Object[] results = userEditOrCreateResult("Create");
        performTestValidation(results);
    }

    @Test
    @Transactional
    public void editCategoryByIdShouldReturnStatusCode200AndNotNull() throws Exception {
        Object[] results = userEditOrCreateResult("edit");
        performTestValidation(results);
    }

    @Test
    @Transactional
    public void deleteCategoryByCategoryIdAndUserIdShouldReturn200AndSuccessMessage() throws Exception {
        performDeleteTest(mockMvc, deleteUrl);
    }

    private void performGetCategoryTest(String urlToGet) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(urlToGet)
                .accept(MediaType.APPLICATION_JSON);

        Object[] results = parseJsonResult(mockMvc, requestBuilder);
        performTestValidation(results);
    }

    private void performTestValidation(Object[] results) {
        MvcResult result = (MvcResult) results[0];
        ResultDTO resultDTO = (ResultDTO) results[1];

        Assert.assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        Assert.assertNotNull(resultDTO.getData());
    }

    private Object[] userEditOrCreateResult(String method) throws Exception {
        MultiValueMap<String, String> formData = new HttpHeaders();
        formData.add("title", "test new title");
        formData.add("priority", "4");
        formData.add("isDeleted", "false");
        formData.add("ownerId", "1");

        String url = method.equalsIgnoreCase("create") ? createUrl : editUrl;

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(url).params(formData)
                .accept(MediaType.APPLICATION_JSON);

        return parseJsonResult(mockMvc, requestBuilder);
    }


}











