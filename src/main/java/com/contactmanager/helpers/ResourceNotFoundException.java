package com.contactmanager.helpers;

public class ResourceNotFoundException extends RuntimeException
{

    public ResourceNotFoundException(String message)
    {
        super(message);
    }



    public ResourceNotFoundException()
    {
        super("Resource not found");
    }


}
