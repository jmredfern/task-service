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

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import jmredfern.taskservice.entities.Task;
import jmredfern.taskservice.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Transactional Dao for {@code Task} objects which uses JPA entityManager.
 * 
 * @author Jim Redfern
 */
@Service
public class TaskDao {

    private static final Logger LOG = LoggerFactory.getLogger(TaskDao.class);
    
    private static final String METHOD_GET_ALL_FOR_USER = "getAllForUser";
    private static final String METHOD_GET_ALL = "getAll";
    private static final String METHOD_GET = "get";
    private static final String METHOD_ADD = "add";
    private static final String METHOD_UPDATE = "update";
    private static final String METHOD_DELETE = "delete";
    
    @PersistenceContext(type=PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;
        
    /**
     * Returns all {@code Task}s for {@code User} in the data source.
     * 
     * @param user
     * @return {@code List<Task>}
     */
    @Transactional
    public List<Task> getAllForUser(User user) {
        LOG.trace(METHOD_GET_ALL_FOR_USER + "(" + user + ")");
        
        TypedQuery<Task> typedQuery = 
                entityManager.createQuery("SELECT t FROM Task t WHERE t.user = :user", Task.class);
        typedQuery.setParameter("user", user);
        return typedQuery.getResultList();
    }
    
    /**
     * Returns all {@code Task}s in the data source.
     * 
     * @return {@code List<Task>}
     */
    public List<Task> getAll() {
        LOG.trace(METHOD_GET_ALL + "()");
        
        TypedQuery<Task> typedQuery = entityManager.createQuery("SELECT t FROM Task t", Task.class);
        return typedQuery.getResultList();
    }
        
    /**
     * Returns the {@code Task} with {@code taskId} from in the data source.
     * 
     * @param taskId
     * @return {@code Task}
     */
    public Task get(Long taskId) {
        LOG.trace(METHOD_GET + "(" + taskId + ")");
        
        return entityManager.find(Task.class, taskId);
    }    
        
    /**
     * Adds the {@code Task} to the data source.
     * 
     * @param task
     */
    @Transactional
    public void add(Task task) {
        LOG.trace(METHOD_ADD + "(" + task + ")");
        
        entityManager.persist(task);
        entityManager.flush();
    }
    
    /**
     * Updates the {@code Task} in the data source.
     * 
     * @param task 
     */
    @Transactional
    public void update(Task task) {
        LOG.trace(METHOD_UPDATE + "(" + task + ")");
        
        entityManager.merge(task);
        entityManager.flush();
    }
    
    /**
     * Deletes the {@code Task} from the data source.
     * 
     * @param task 
     */
    @Transactional
    public void delete(Task task) {
        LOG.trace(METHOD_DELETE + "(" + task + ")");

        Query query = entityManager.createQuery("DELETE FROM Task WHERE id = :id");
        query.setParameter("id", task.getId());
        query.executeUpdate();
                        
        entityManager.flush();
    }
    
}
