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

import java.util.List;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import jmredfern.taskservice.entities.Task;
import jmredfern.taskservice.daos.TaskDao;
import jmredfern.taskservice.daos.UserDao;
import jmredfern.taskservice.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@code TaskServiceRestController} defines the Spring MVC Rest Controller within the task service project.
 * 
 * @author Jim Redfern
 */
@RestController
@RequestMapping(value="/user/{userId}/task", produces="application/json")
public class TaskServiceRestController {

    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceRestController.class);
    
    private static final String METHOD_ADD_USER_TO_MODEL = "addUserToModel";
    private static final String METHOD_UPDATE = "update";
    private static final String METHOD_DELETE = "delete";
    private static final String METHOD_ADD = "add";
    private static final String METHOD_GET_TASKS_FOR_USER = "getTasksForUser";
    private static final String METHOD_GET_TASK = "getTask";
    
    @Resource
    private TaskDao taskDao;   
    
    @Resource
    private UserDao userDao;
        
    /**
     * Adds {@code User} with id {@code userId} to the model.
     * 
     * @param userId
     * @return {@code User}
     */
    @ModelAttribute
    public User addUserToModel(@PathVariable Long userId) {
        LOG.trace(METHOD_ADD_USER_TO_MODEL + "(" + userId + ")");
        
        return userDao.get(userId);
    }
        
    /**
     * Updates an existing {@code Task}.
     * 
     * @param user {@code User} - the {@code User} who's task will be updated.
     * @param task {@code Task} - the {@code Task} that will be updated. 
     */
    @RequestMapping(method = RequestMethod.PUT)
    @Transactional
    public void update(@ModelAttribute User user, @RequestBody Task task) {
        LOG.info(METHOD_UPDATE + "(" + task + ")");
        
        task.setUser(user);
        taskDao.update(task);
    }  
    
    /**
     * Deletes an existing {@code Task}.
     * 
     * @param task {@code Task} - the {@code Task} that will be updated. 
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @Transactional
    public void delete(@RequestBody Task task) {
        LOG.info(METHOD_DELETE + "(" + task + ")");
        
        taskDao.delete(task);
    }  
        
    /**
     * Adds a {@code Task} for a {@code User}.
     * 
     * @param user {@code User} - the {@code User} whose task will be added.
     * @param task {@code Task} - the {@code Task} that will be added. 
     */
    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public void add(@ModelAttribute User user, @RequestBody Task task) {
        LOG.info(METHOD_ADD + "(" + user + ", " + task + ")");
        
        task.setUser(user);
        taskDao.add(task);
    }    
   
    /**
     * Returns all {@code Task}s for {@code User}.
     * 
     * @param user {@code User} - the {@code User} whose tasks will be returned.
     * @return {@code List<Task>}
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public List<Task> getTasksForUser(@ModelAttribute User user) {
        LOG.info(METHOD_GET_TASKS_FOR_USER + "(" + user + ")");
        
        return taskDao.getAllForUser(user);
    }    
        
    /**
     * Returns a {@code Task}.
     * 
     * @param taskId {@code Long} - the id of the {@code Task} which will be returned.
     * @return {@code Task}
     */
    @RequestMapping(value="/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public Task getTask(@PathVariable Long taskId) {
        LOG.info(METHOD_GET_TASK + "(" + taskId + ")");
        
        return taskDao.get(taskId);
    }    
    
}
