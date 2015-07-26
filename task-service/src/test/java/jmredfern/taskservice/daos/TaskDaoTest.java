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
package jmredfern.taskservice.daos;

import jmredfern.taskservice.entities.*;
import java.util.Random;
import javax.persistence.EntityManager;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Jim Redfern
 */
public class TaskDaoTest {
    private static final long serialVersionUID = 1109023542628949923L;
        
    TaskDao testTaskDao;
    
    @Injectable EntityManager entityManager;
    
    @Before
    public void init() {
        testTaskDao = new TaskDao();
        
        Deencapsulation.setField(testTaskDao, "entityManager", entityManager);
    }
    
    @Test
    public void get() {
        Task task = new Task();
        Long taskId = new Random().nextLong();
        new Expectations() {{
            entityManager.find(Task.class, taskId); result = task; times = 1;
        }};
        Task resultTask = testTaskDao.get(taskId);
        Assert.assertEquals(task, resultTask);
    }      
    
    @Test
    public void add() {
        Task task = new Task();
        new Expectations() {{
            entityManager.persist(task); times = 1;
        }};        
        testTaskDao.add(task);
    }
    
    @Test
    public void update() {
        Task task = new Task();
        new Expectations() {{
            entityManager.merge(task); times = 1;
        }};        
        testTaskDao.update(task);
    }
    
}
