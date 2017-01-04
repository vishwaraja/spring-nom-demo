package com.nominum;

/**
 * Created by vpathi on 12/28/16.
 */
public class Environment {

    private String driver;
    private String version;
    private String vmName;
    private String userName;


    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmname) {
        this.vmName = vmname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



}
