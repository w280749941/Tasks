package com.heartiger.task.controller;

import com.google.gson.Gson;
import com.heartiger.task.TaskApplicationTests;
import com.heartiger.task.controller.common.Utility;
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

@Slf4j
@AutoConfigureMockMvc
public class TaskControllerTest extends TaskApplicationTests {

    private String createUrl;
    private String getUrl;
    private String searchUrl;
    private String editUrl;
    private String deleteUrl;
    private String completeUrl;

    @Before
    public void setup(){
        int taskId = 6;
        int userId = 1;
        createUrl = "/api/tasks/new";
        getUrl = "/api/tasks/user/" + userId;
        searchUrl = String.format("/api/tasks/search?tid=%s&oid=%s",taskId,userId);
        editUrl = "/api/tasks/edit/" + taskId;
        deleteUrl = String.format("/api/tasks/delete?tid=%s&oid=%s",taskId,userId);
        completeUrl = String.format("/api/tasks/complete?tid=%s&oid=%s",taskId,userId);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllTasksShouldReturnStatusCode200AndNotNull() throws Exception {
        performGetTaskTest(getUrl);
    }


    @Test
    public void findTaskByIdAndUserIdShouldReturnStatusCode200AndNotNull() throws Exception {
        performGetTaskTest(searchUrl);
    }

    @Test
    @Transactional
    public void createTaskShouldReturnStatusCode200AndNotNull() throws Exception {
        Object[] results = userEditOrCreateResult("Create");
        performTestValidation(results);
    }

    @Test
    @Transactional
    public void editTaskByIdShouldReturnStatusCode200AndNotNull() throws Exception {
        Object[] results = userEditOrCreateResult("edit");
        performTestValidation(results);
    }

    @Test
    @Transactional
    public void deleteTaskByCategoryIdAndUserIdShouldReturn200AndSuccessMessage() throws Exception {
        Utility.performDeleteTest(mockMvc, deleteUrl);
    }

    @Test
    @Transactional
    public void completeTaskWithUserIdShouldReturn200AndSuccessMessage() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(completeUrl)
                .accept(MediaType.APPLICATION_JSON);
        Object[] results = parseJsonResult(mockMvc, requestBuilder);
        performTestValidation(results);
    }

    private void performGetTaskTest(String urlToGet) throws Exception {
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
        formData.add("title", "some title");
        formData.add("description", "some description");
        formData.add("priority", "2");
        formData.add("isDeleted", "false");
        formData.add("isCompleted", "false");
        formData.add("ownerId", "1");
        formData.add("categoryId", "5");
        formData.add("dueTime", "1591715575");
        formData.add("reminderTime", "1591715500");

        String url = method.equalsIgnoreCase("create") ? createUrl : editUrl;

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(url).params(formData)
                .accept(MediaType.APPLICATION_JSON);

        return parseJsonResult(mockMvc, requestBuilder);
    }
}