package com.example.firebasechatapp.model;

public class UserData
{
    private String displayName;
    private String status;
    private String image;

    public UserData()
    {
    }

    public UserData(String displayName, String status, String image)
    {
        this.displayName = displayName;
        this.status = status;
        this.image = image;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    @Override
    public String toString()
    {
        return "UserData{" +
                "displayName='" + displayName + '\'' +
                ", status='" + status + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
