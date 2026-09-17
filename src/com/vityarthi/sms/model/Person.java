package com.vityarthi.sms.model;

import com.vityarthi.sms.exception.InvalidDataException;
import com.vityarthi.sms.util.ValidationUtils;
import java.io.Serializable;

/**
 * Abstract Person base class demonstrating Abstraction and Encapsulation.
 */
public abstract class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String email;

    public Person(String id, String name, String email) throws InvalidDataException {
        ValidationUtils.validateName(name);
        ValidationUtils.validateEmail(email);
        this.id = id != null ? id.trim() : "";
        this.name = name.trim();
        this.email = email.trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidDataException {
        ValidationUtils.validateName(name);
        this.name = name.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws InvalidDataException {
        ValidationUtils.validateEmail(email);
        this.email = email.trim();
    }

    /**
     * Abstract method to get role or display title of the person.
     */
    public abstract String getRoleTitle();

    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", id, name, email);
    }
}
