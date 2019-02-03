/**
 * Profile.java
 *
 * Copyright 2019.01.29 Vasiliy Petukhov
 * 
 * @version 1.0
 */
package me.nyanguymf.console.client.data;

/**
 * @author nyanguymf
 */
public interface Profile {

    /** Gets Socket host. */
    String getHost();

    /** Gets user's password connection. */
    String getPassword();

    /** Gets user name. */
    String getUserName();

    /** Gets Socket port. */
    int getPort();

    /** Gets profile name. */
    String getName();
}
