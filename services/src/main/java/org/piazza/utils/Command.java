package org.piazza.utils;

import java.util.HashMap;

public interface Command {

    void init(Receiver receiver) throws Exception;

    HashMap<String, Object> execute(HashMap<String, Object> request) throws Exception;
}