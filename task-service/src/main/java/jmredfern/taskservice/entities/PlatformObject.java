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

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import jmredfern.taskservice.util.ToStringUtil;

/**
 * Base entity for Platform Objects. Platform Objects have an id.
 * 
 * @author Jim Redfern
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//@JsonPropertyOrder will place "type" field at the top of the JSON object
@JsonPropertyOrder({ "type" }) 
public class PlatformObject implements Serializable {
    
    private static final long serialVersionUID = 2189363272352653738L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    public PlatformObject() {
    }
    
    /**
     * Creates a PlatformObject with id {@code id}
     * 
     * @param id 
     */
    public PlatformObject(Long id) {
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getType() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
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
        PlatformObject other = (PlatformObject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        if (this.id == null && other.id == null) {
            return this == other;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder("PlatformObject[");
        ToStringUtil.addAttribute(text, " id=", getId());
        text.append("]");

        return text.toString();        
    }
        
}
