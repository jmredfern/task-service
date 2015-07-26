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
package jmredfern.taskservice;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import jmredfern.taskservice.daos.InvoiceDao;
import jmredfern.taskservice.daos.TaskDao;
import jmredfern.taskservice.daos.UserDao;
import jmredfern.taskservice.daos.W9Dao;
import jmredfern.taskservice.entities.Invoice;
import jmredfern.taskservice.entities.PlatformObject;
import jmredfern.taskservice.entities.Task;
import jmredfern.taskservice.entities.TaskState;
import jmredfern.taskservice.entities.User;
import jmredfern.taskservice.entities.W9;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code DatabaseProvisioner} will provision some entities in the database.
 * 
 * 2 x {@code User} (Jim, John), 1 x {@code Invoice}, 1 x {@code W9} and 3 x {@code Task}.
 * 
 * @author Jim Redfern
 */
public class DatabaseProvisioner {
    
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseProvisioner.class);
    
    private static final String METHOD_ADD_ENTITIES = "addEntities";
    
    @Resource
    private TaskDao taskDao;
    
    @Resource
    private InvoiceDao invoiceDao;

    @Resource
    private W9Dao w9Dao;
    
    @Resource
    private UserDao userDao;
    
    private Boolean enabled;
    
    /**
     * Triggers the {@code DatabaseProvisioner} to add all entities.
     */
    @PostConstruct
    public void init() {
        if (enabled) {
            addEntities(true, true, true);
        }
    }

    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * If {@code enabled} is true the entities will be provisioned on @PostConstruct
     * 
     * @param enabled 
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }        
    
    /**
     * Store some entities in the database. Note: addTasks requires that addUsers and 
     * addPlatformObjects are set.
     * 
     * @param addUsers boolean - true to create {@code User}
     * @param addPlatformObjects boolean - true to create {@code Invoice} and {@code W9}
     * @param addTasks boolean - true to create {@code Task}
     */
    public void addEntities(boolean addUsers, boolean addPlatformObjects, boolean addTasks) {
        LOG.trace(METHOD_ADD_ENTITIES + "()");
        
        boolean bAddUsers = addUsers;
        boolean bAddPlatformObjects = addPlatformObjects;
        boolean bAddTasks = addTasks;
        
        if (bAddTasks) {
            bAddUsers = true;
            bAddPlatformObjects = true;
        }
        
        User user1 = null;
        User user2 = null;
        Invoice invoice = null;
        W9 w9 = null;
        
        if (bAddUsers) {
            user1 = new User();
            user1.setName("Jim");
            userDao.add(user1);

            user2 = new User();
            user2.setName("John");
            userDao.add(user2);
        }
        
        if (bAddPlatformObjects) {
            invoice = new Invoice();
            invoice.setPurchaseOrder("PO1");
            invoiceDao.add(invoice);

            w9 = new W9();
            w9.setTaxPayerIdentificationNumber("234987239");
            w9Dao.add(w9);        
        }
        
        if (bAddTasks) {
            Task task1 = new Task();
            task1.setUser(user1);
            List<PlatformObject> platformObjects = new ArrayList<>();
            platformObjects.add(invoice);
            platformObjects.add(w9);
            task1.setName("Task 1");
            task1.setDescription("Task 1 description");
            task1.setAssociatedPlatformObjects(platformObjects);
            task1.setState(TaskState.OPEN);
            taskDao.add(task1);

            Task task2 = new Task();
            task2.setName("Task 2");
            task1.setDescription("Task 2 description");
            task2.setUser(user1);
            task2.setState(TaskState.COMPLETE);
            taskDao.add(task2);

            Task task3 = new Task();
            task3.setName("Task 3");
            task1.setDescription("Task 3 description");
            task3.setUser(user2);
            task3.setState(TaskState.OPEN);
            taskDao.add(task3);
        }
    }    
}
