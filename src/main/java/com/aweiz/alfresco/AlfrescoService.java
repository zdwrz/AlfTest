package com.aweiz.alfresco;

import java.io.File;
import java.util.List;

/**
 * Created by daweizhuang on 3/3/17.
 */
public interface AlfrescoService {

    String login(String userName, String pwd);
    Boolean validateTicket(String ticket);
    String addPerson();
    String findPerson(String userName, String ticket);
    List<String> getFileListByPerson();
    String findFile(String ticket);
    File getFile();
    String uploadFile();
}
