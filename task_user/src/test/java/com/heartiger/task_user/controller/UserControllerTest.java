package com.heartiger.task_user.controller;

import com.google.gson.Gson;
import com.heartiger.task_user.TaskUserApplicationTests;
import com.heartiger.task_user.datamodel.UserInfo;
import com.heartiger.task_user.dto.ResultDTO;
import com.heartiger.task_user.service.UserService;
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

@Slf4j
@AutoConfigureMockMvc
public class UserControllerTest extends TaskUserApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    private String successMessage;
    private String createUrl;
    private String getUrl;
    private String editUrl;
    private String deleteUrl;
    private Gson gson;

    @Before
    public void setup(){
        gson = new Gson();
        int userId = 1;
        successMessage = "Success";
        createUrl = "/api/users/new";
        getUrl = "/api/users/search/" + userId;
        editUrl = "/api/users/edit/" + userId;
        deleteUrl = "/api/users/delete/" + userId;
    }

    @Test
    public void findUserByIdShouldReturnStatusCode200AndNotNull() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(getUrl)
                .accept(MediaType.APPLICATION_JSON);

        Object[] results = parseJsonResult(requestBuilder);
        MvcResult result = (MvcResult) results[0];
        ResultDTO resultDTO = (ResultDTO) results[1];

        Assert.assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        Assert.assertNotNull(resultDTO.getData());
    }

    @Test
    @Transactional
    public void deleteUserByIdShouldReturn200AndSuccessMessage() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(deleteUrl)
                .accept(MediaType.APPLICATION_JSON);

        Object[] results = parseJsonResult(requestBuilder);
        MvcResult result = (MvcResult) results[0];
        ResultDTO resultDTO = (ResultDTO) results[1];

        Assert.assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        Assert.assertEquals(resultDTO.getMessage(),successMessage);
    }

    @Test
    @Transactional
    public void createUserShouldReturn200AndNotNull() throws Exception {

        Object[] results = userEditOrCreateResult("Create");
        MvcResult result = (MvcResult) results[0];
        ResultDTO resultDTO = (ResultDTO) results[1];

        Assert.assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        Assert.assertNotNull(resultDTO.getData());
    }

    @Test
    @Transactional
    public void editUserShouldReturn200AndNotNull() throws Exception {

        Object[] results = userEditOrCreateResult("Edit");
        MvcResult result = (MvcResult) results[0];
        ResultDTO resultDTO = (ResultDTO) results[1];

        Assert.assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        Assert.assertNotNull(resultDTO.getData());
    }

    private Object[] userEditOrCreateResult(String method) throws Exception {
        MultiValueMap<String, String> formData = new HttpHeaders();
        formData.add("email", "new@email.com");
        formData.add("passcode", "something");
        formData.add("firstName", "frists");
        formData.add("lastName", "lasts");

        String url = method.equalsIgnoreCase("create") ? createUrl : editUrl;

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(url).params(formData)
                .accept(MediaType.APPLICATION_JSON);

        return parseJsonResult(requestBuilder);
    }

    private Object[] parseJsonResult(RequestBuilder requestBuilder) throws Exception {
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