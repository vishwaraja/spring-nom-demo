package com.nominum;

/**
 * Created by vpathi on 1/1/17.
 */
public class DockerCommandLineConstants {

    public static final String DOCKER = "/usr/local/bin/docker";
    public static final String HOSTNAME = "--host";
    public static final String SHELL = "/bin/bash";
    public static final String SHELL_PARAM = "-c";
    public static final String REGISTRY_USER_PARAM = "--username";
    public static final String REGISTRY_PASSWORD_PARAM = "--password";
    public static final String TLS = "--tls";
    public static final String TLSCERT = "--tlscert";
    public static final String TLSVERIFY = "--tlsverify";
    public static final String TLSCACERT = "--tlscacert";
    public static final String TLSKEY = "--tlskey";
    public static final String SPACE = " ";
    public static final String FORMAT = "--format";
    public static final String VMNAME = "{{.Name}}";
    public static final String URL = "{{.URL}}";
    public static final String ACTIVE = "{{.Active}}";
    public static final String STATE = "{{.State}}";
}
