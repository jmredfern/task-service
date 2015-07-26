/*
 * Copyright 2015 Jim Redfern.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jmredfern.taskservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import jmredfern.taskservice.DatabaseProvisioner;
import jmredfern.taskservice.util.StringUtil;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Jim Redfern
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/taskservice-servlet.xml", 
                                 "classpath:taskservice-servlet-test.xml"})
@TransactionConfiguration(defaultRollback = true)
public class TaskServiceRestControllerIntegrationTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceRestControllerIntegrationTest.class);
    
    @Autowired
    private WebApplicationContext wac;
    
    private MockMvc mockMvc;

    @Autowired
    private DatabaseProvisioner databaseProvisioner;
    
    private JdbcTemplate jdbcTemplate;

    private static final String METHOD_ADD_AND_GET_TASK = "addAndGetTask";
    private static final String METHOD_ADD_AND_GET_TWO_TASKS_FOR_SAME_USER = "addAndGetTwoTasksForSameUser";
    private static final String METHOD_ADD_AND_GET_TWO_TASKS_FOR_DIFFERENT_USERS = "addAndGetTwoTasksForDifferentUsers";
    private static final String METHOD_ADD_UPDATE_AND_GET_TASK = "addUpdateAndGetTask";
    private static final String ADD_AND_DELETE_TASK = "addAndDeleteTask";
    
    private static boolean beforeClass = true;
    
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
        
    @Before
    @BeforeTransaction
    public void setup() {                
        if (beforeClass) {
            //Add entities once only during this test suite, this means that the PKIDs will be fixed and can be used
            //in tests below.
            beforeClass = false;            
            databaseProvisioner.addEntities(true, true, false);
        }
        
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Value("classpath:json/task1.json")
    private Resource task1json;
    
    @Value("classpath:json/task1UpdateTemplate.json")
    private Resource task1UpdateJsonTemplate;
    
    @Value("classpath:json/task2.json")
    private Resource task2json;    
    
    @Test
    @Transactional
    public void addAndGetTask() throws Exception {
        
        //check there is only 2 users in the db
        Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "\"user\""));
        
        //send a request to store a task for user 1
        this.mockMvc.perform(post("/user/1/task")
            .content(StringUtil.resourceToString(task1json))
            .contentType(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk());
        
        //check there's 1 task stored in the db
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "task"));
        
        //request a list of all tasks for this user, validate length = 1
        MvcResult mvcResult = this.mockMvc.perform(get("/user/1/task").accept(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", Matchers.hasSize(1)))
            .andReturn();
        
        //find out the generated id of the stored task
        ReadContext jsonCtx = JsonPath.parse(mvcResult.getResponse().getContentAsString());         
        Integer taskId = jsonCtx.read("$[0].id");
             
        //request the task by id and validate fields
        mvcResult = this.mockMvc.perform(get("/user/1/task/" + taskId).accept(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.type").value("Task"))
            .andExpect(jsonPath("$.name").value("Task 1"))
            .andExpect(jsonPath("$.description").value("Task 1 description"))
            .andExpect(jsonPath("$.state").value("OPEN"))
            .andExpect(jsonPath("$.user.id").value(1))
            .andReturn();
        
        LOG.trace(METHOD_ADD_AND_GET_TASK + "() " + mvcResult.getResponse().getContentAsString());
    }    
    
    @Test
    @Transactional
    public void addAndGetTwoTasksForSameUser() throws Exception {
        
        //check there is only 2 users in the db
        Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "\"user\""));
        
        //send a request to store a task 1 for user 1
        this.mockMvc.perform(post("/user/1/task")
            .content(StringUtil.resourceToString(task1json))
            .contentType(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk());
        
        //send a request to store a task 2 for user 1
        this.mockMvc.perform(post("/user/1/task")
            .content(StringUtil.resourceToString(task2json))
            .contentType(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk());
        
        //check there's 2 tasks stored in the db
        Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "task"));
        
        //request a list of all tasks for this user, validate length = 2
        MvcResult mvcResult = this.mockMvc.perform(get("/user/1/task").accept(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", Matchers.hasSize(2)))
            .andReturn();
        
        //find out the generated ids of the stored task
        ReadContext jsonCtx = JsonPath.parse(mvcResult.getResponse().getContentAsString());         
        Integer task1Id = jsonCtx.read("$[0].id");
        Integer task2Id = jsonCtx.read("$[1].id");
             
        //request task 1 by id and validate fields
        mvcResult = this.mockMvc.perform(get("/user/1/task/" + task1Id).accept(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.type").value("Task"))
            .andExpect(jsonPath("$.name").value("Task 1"))
            .andExpect(jsonPath("$.description").value("Task 1 description"))
            .andExpect(jsonPath("$.state").value("OPEN"))
            .andExpect(jsonPath("$.user.id").value(1))
            .andReturn();
        
        LOG.trace(METHOD_ADD_AND_GET_TWO_TASKS_FOR_SAME_USER + "() " + mvcResult.getResponse().getContentAsString());
        
        //request task 2 by id and validate fields
        mvcResult = this.mockMvc.perform(get("/user/1/task/" + task2Id).accept(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.type").value("Task"))
            .andExpect(jsonPath("$.name").value("Task 2"))
            .andExpect(jsonPath("$.description").value("Task 2 description"))
            .andExpect(jsonPath("$.state").value("COMPLETE"))
            .andExpect(jsonPath("$.user.id").value(1))
            .andReturn();
        
        LOG.trace(METHOD_ADD_AND_GET_TWO_TASKS_FOR_SAME_USER + "() " + mvcResult.getResponse().getContentAsString());
    }    
    
    @Test
    @Transactional
    public void addAndGetTwoTasksForDifferentUsers() throws Exception {
        
        //check there is only 2 users in the db
        Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "\"user\""));
        
        //send a request to store a task 1 for user 1
        this.mockMvc.perform(post("/user/1/task")
            .content(StringUtil.resourceToString(task1json))
            .contentType(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk());
        
        //send a request to store a task 2 for user 2
        this.mockMvc.perform(post("/user/2/task")
            .content(StringUtil.resourceToString(task2json))
            .contentType(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk());
        
        //check there's 2 tasks stored in the db
        Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "task"));
        
        //request a list of all tasks for user 1, validate length = 1
        MvcResult mvcResult = this.mockMvc.perform(get("/user/1/task").accept(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", Matchers.hasSize(1)))
            .andReturn();
        
        //find out the generated ids of the stored task
        ReadContext jsonCtx = JsonPath.parse(mvcResult.getResponse().getContentAsString());         
        Integer task1Id = jsonCtx.read("$[0].id");
             
        //request task 1 by id and validate fields
        mvcResult = this.mockMvc.perform(get("/user/1/task/" + task1Id).accept(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.type").value("Task"))
            .andExpect(jsonPath("$.name").value("Task 1"))
            .andExpect(jsonPath("$.description").value("Task 1 description"))
            .andExpect(jsonPath("$.state").value("OPEN"))
            .andExpect(jsonPath("$.user.id").value(1))
            .andReturn();
        
        LOG.trace(METHOD_ADD_AND_GET_TWO_TASKS_FOR_DIFFERENT_USERS + "() " + mvcResult.getResponse().getContentAsString());
        
        //request a list of all tasks for user 2, validate length = 1
        mvcResult = this.mockMvc.perform(get("/user/2/task").accept(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", Matchers.hasSize(1)))
            .andReturn();
        
        //find out the generated ids of the stored task
        jsonCtx = JsonPath.parse(mvcResult.getResponse().getContentAsString());         
        task1Id = jsonCtx.read("$[0].id");
             
        //request task 1 by id and validate fields
        mvcResult = this.mockMvc.perform(get("/user/2/task/" + task1Id).accept(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.type").value("Task"))
            .andExpect(jsonPath("$.name").value("Task 2"))
            .andExpect(jsonPath("$.description").value("Task 2 description"))
            .andExpect(jsonPath("$.state").value("COMPLETE"))
            .andExpect(jsonPath("$.user.id").value(2))
            .andReturn();
        
        LOG.trace(METHOD_ADD_AND_GET_TWO_TASKS_FOR_DIFFERENT_USERS + "() " + mvcResult.getResponse().getContentAsString());
    }    
    
    @Test
    @Transactional
    public void addUpdateAndGetTask() throws Exception {
        
        //check there is only 2 users in the db
        Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "\"user\""));
        
        //send a request to store a task for user 1
        this.mockMvc.perform(post("/user/1/task")
            .content(StringUtil.resourceToString(task1json))
            .contentType(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk());
        
        //check there's 1 task stored in the db
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "task"));
        
        //request a list of all tasks for this user, validate length = 1
        MvcResult mvcResult = this.mockMvc.perform(get("/user/1/task").accept(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", Matchers.hasSize(1)))
            .andReturn();
        
        LOG.trace(METHOD_ADD_UPDATE_AND_GET_TASK + "() " + mvcResult.getResponse().getContentAsString());
        
        //find out the generated id of the stored task
        ReadContext jsonReadCtx = JsonPath.parse(mvcResult.getResponse().getContentAsString());         
        Integer taskId = jsonReadCtx.read("$[0].id");
        
        //update the json template with taskId
        DocumentContext jsonDocumentCtx = JsonPath.parse(StringUtil.resourceToString(task1UpdateJsonTemplate));    
        String task1UpdateJson = jsonDocumentCtx.set("$.id", taskId).jsonString();
        
        //update task adding link to Platform Objects
        this.mockMvc.perform(put("/user/1/task")
            .content(task1UpdateJson)
            .contentType(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk());
                                
        //request the task by id and validate fields
        mvcResult = this.mockMvc.perform(get("/user/1/task/" + taskId).accept(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.type").value("Task"))
            .andExpect(jsonPath("$.name").value("Task 1"))
            .andExpect(jsonPath("$.description").value("Task 1 description"))
            .andExpect(jsonPath("$.state").value("OPEN"))
            .andExpect(jsonPath("$.user.id").value(1))
            .andExpect(jsonPath("$.associatedPlatformObjects", Matchers.hasSize(2)))
            .andExpect(jsonPath("$.associatedPlatformObjects[0].id").value(3))
            .andExpect(jsonPath("$.associatedPlatformObjects[1].id").value(4))
            .andReturn();
        
        LOG.trace(METHOD_ADD_UPDATE_AND_GET_TASK + "() " + mvcResult.getResponse().getContentAsString());
    }       
    
    @Test
    @Transactional
    public void addAndDeleteTask() throws Exception {
        
        //check there is only 2 users in the db
        Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "\"user\""));
        
        //send a request to store a task for user 1
        this.mockMvc.perform(post("/user/1/task")
            .content(StringUtil.resourceToString(task1json))
            .contentType(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk());
        
        //check there's 1 task stored in the db
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "task"));
        
        //request a list of all tasks for this user, validate length = 1
        MvcResult mvcResult = this.mockMvc.perform(get("/user/1/task").accept(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", Matchers.hasSize(1)))
            .andReturn();
        
        //find out the generated id of the stored task
        ReadContext jsonCtx = JsonPath.parse(mvcResult.getResponse().getContentAsString());         
        Integer taskId = jsonCtx.read("$[0].id");
             
        //update the json template with taskId
        DocumentContext jsonDocumentCtx = JsonPath.parse(StringUtil.resourceToString(task1UpdateJsonTemplate));    
        String task1UpdateJson = jsonDocumentCtx.set("$.id", taskId).jsonString();
        
        //delete the task
        this.mockMvc.perform(delete("/user/1/task")
            .content(task1UpdateJson)
            .contentType(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk());
        
        //request a list of all tasks for this user, validate length = 0
        mvcResult = this.mockMvc.perform(get("/user/1/task").accept(MediaType.parseMediaType("application/json")))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", Matchers.hasSize(0)))
            .andReturn();       
        
        LOG.trace(ADD_AND_DELETE_TASK + "() " + mvcResult.getResponse().getContentAsString());
    }        

}