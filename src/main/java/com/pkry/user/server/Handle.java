package com.pkry.user.server;

/**
 * Handle interface
 */
public interface Handle {
    /**
     * Abstract function to handle messages from client
     * @param data message
     * @return return message to client
     */
    String handle(String data);
}