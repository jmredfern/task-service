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
import javax.persistence.Entity;
import javax.persistence.Table;
import jmredfern.taskservice.util.ToStringUtil;

/**
 * Entity for User, which is a Platform Object. Users have an id and a name.
 * 
 * @author Jim Redfern
 */
@Entity
@Table(name = "\"User\"")
public class User extends PlatformObject implements Serializable {    
    private static final long serialVersionUID = 1903874854958363834L;

    private String name;

    public User() {
    }   
    
    /**
     * Creates an User with PlatformObject id {@code id}
     * 
     * @param id 
     */
    public User(Long id) {
        super(id);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        User other = (User) object;
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
        StringBuilder text = new StringBuilder("User[");
        ToStringUtil.addAttribute(text, " id=", getId());
        ToStringUtil.addAttribute(text, " n=", getName());
        text.append("]");

        return text.toString();        
    }
    
}
