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

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Entity;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import jmredfern.taskservice.util.ToStringUtil;

/**
 * Entity for Task, which is a Platform Object. Tasks have an id, a name, a description and a state and can be 
 * associated to any other Platform Object.
 * 
 * @author Jim Redfern
 */
@Entity
public class Task extends PlatformObject implements Serializable {    
    private static final long serialVersionUID = 1934824920028283545L;
   
    @ManyToOne
    private User user;
    
    //'fetch=EAGER' required to prepopulate collection from DB for JSON conversion
    @ManyToMany(fetch=EAGER)
    private Collection<PlatformObject> associatedPlatformObjects;    
    
    //Lob is used to allow a name of any length
    @Lob
    private String name;
    
    //Lob is used to allow a description of any length
    @Lob
    private String description;    
    
    @Basic(optional=false)
    private TaskState state;

    public Task() {
    }   
    
    /**
     * Creates an Task with PlatformObject id {@code id}
     * 
     * @param id 
     */
    public Task(Long id) {
        super(id);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection<PlatformObject> getAssociatedPlatformObjects() {
        return associatedPlatformObjects;
    }

    public void setAssociatedPlatformObjects(Collection<PlatformObject> associatedPlatformObjects) {
        this.associatedPlatformObjects = associatedPlatformObjects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }     
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }        
        if (!(this.getClass() == object.getClass())) {
            return false;
        }
        Task other = (Task) object;
        if ((this.getId() == null && other.getId() != null) 
                || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        if (this.getId() == null && other.getId() == null) {
            return this == other;
        }
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder text = new StringBuilder("Task[");
        ToStringUtil.addAttribute(text, " id=", getId());
        ToStringUtil.addAttribute(text, " n=", getName());
        ToStringUtil.addAttribute(text, " s=", getState());
        if (associatedPlatformObjects != null) {
            for (PlatformObject platformObject : associatedPlatformObjects) {
                ToStringUtil.addAttribute(text, " apo=", platformObject.toString());
            }
        }
        text.append("]");

        return text.toString();        
    }
    
}
