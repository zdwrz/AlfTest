package com.aweiz.alfTest.test;

import com.aweiz.alfresco.AlfrescoService;
import com.aweiz.alfresco.AlfrescoServiceImpl;
import com.aweiz.alfreso.pojo.Ticket;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

/**
 * Created by daweizhuang on 2/27/17.
 */
public class AlfrescoServiceTest {


    static String ticket;

    static AlfrescoService service ;
    @BeforeClass
    public static void login(){
        service = new AlfrescoServiceImpl();
        ticket = service.login("zdwrz","1234");
    }
    @Before
    public void prepare() throws IOException {
       assertTrue(service.validateTicket(ticket));
    }

    @Test
    public void testLogin(){
        System.out.println(ticket);
        assertTrue(ticket!=null);
    }
    @Test
    public void testFindPerson(){
        String res = service.findPerson("zdwrz",ticket);
        System.out.println(res);
        assertTrue(res!=null);
    }
    @Test
    public void testFindFiles(){
        String res = service.findFile(ticket);
        System.out.println(res);
        assertTrue(res!=null);
    }
}
