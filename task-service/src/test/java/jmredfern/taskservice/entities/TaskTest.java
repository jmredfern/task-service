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
package jmredfern.taskservice.entities;

import java.util.Random;
import java.util.UUID;
import mockit.Injectable;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Jim Redfern
 */
public class TaskTest {
    private static final long serialVersionUID = 1109023542628949923L;
        
    Task testTask;
    
    @Test 
    public void emptyConstructor() {
        testTask = new Task();
    }
    
    @Test 
    public void constructor() {
        Long id = new Random().nextLong();
        testTask = new Task(id);
        Long returnedId = testTask.getId();
        Assert.assertEquals(id, returnedId);
    }
    
    @Test
    public void setGetUser() {        
        testTask = new Task();        
        User user = new User();
        testTask.setUser(user);
        User returnedUser = testTask.getUser();
        Assert.assertEquals(user, returnedUser);
    }      
    
    @Test
    public void getType() {
        testTask = new Task();
        Assert.assertEquals("Task", testTask.getType());
    }
    
}
