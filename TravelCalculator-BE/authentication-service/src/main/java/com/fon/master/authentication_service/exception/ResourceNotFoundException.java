package com.fon.master.authentication_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private String resourcesName;
    private String fieldName;
    private String fieldValue;


    public ResourceNotFoundException(String resourcesName, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s: '%s'",resourcesName,fieldName,fieldValue)); // USER NOT FOUND WITH ID : 1
        this.resourcesName = resourcesName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(String resourcesName, String fieldName, Long fieldValue) {
        super(String.format("%s not found with %s: '%s'",resourcesName,fieldName,fieldValue)); // USER NOT FOUND WITH ID : 1
        this.resourcesName = resourcesName;
        this.fieldName = fieldName;
        this.fieldValue = String.valueOf(fieldValue);
    }

    public String getResourcesName() {
        return resourcesName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
