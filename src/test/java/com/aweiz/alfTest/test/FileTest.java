package com.aweiz.alfTest.test;

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
import java.util.stream.Collectors;

/**
 * Created by daweizhuang on 2/27/17.
 */
public class FileTest {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String parse(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    public static Object parse(String jsonInString, Class clazz) throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(jsonInString, clazz);
    }

    static String ticket;

    @BeforeClass
    public static void testLogin() throws IOException {
        String url = "http://localhost:8080/alfresco/api/-default-/public/authentication/versions/1/tickets";

        HttpClient client = HttpClientBuilder.create().build();
        //     HttpGet request = new HttpGet(url);
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        HttpEntity entity = new StringEntity("{\"userId\":\"zdwrz\",\"password\":\"1234\"}", ContentType.APPLICATION_JSON);
        request.setEntity(entity);
// add request header
//        request.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36");
        request.addHeader("Accept", "application/json");
        HttpResponse response = client.execute(request);
//        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
//            System.out.println(buffer.lines().collect(Collectors.joining("\n")));
//        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());

        Ticket o = (Ticket) parse(result.toString(), Ticket.class);
        System.out.println("Ticket is :" + o.getData().getId());
        ticket = o.getData().getId();
    }

    @Test
    public void testFindPerson() throws IOException {
        String url = "http://localhost:8080/alfresco/s/api/people/zdwrz" + "?alf_ticket=" + ticket;

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        request.addHeader("content-type", "application/json");
        request.addHeader("Accept", "application/json");
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());

        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

    }

    //test folder e693e6d7-9384-419a-8b4c-edbf7f9fac97
    @Test
    public void testFindChilldren() throws IOException {
//        String url = "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/nodes/-my-/children" + "?alf_ticket=" + ticket;
        String url = "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/nodes/e693e6d7-9384-419a-8b4c-edbf7f9fac97/children" + "?alf_ticket=" + ticket;

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        request.addHeader("content-type", "application/json");
        request.addHeader("Accept", "application/json");
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
    }
    @Test
    public void testFindFileAndFolder() throws IOException, URISyntaxException {
        String url = "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/queries/nodes";
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet();
        URI uri = new URIBuilder("http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/queries/nodes")
                .addParameter("alf_ticket",ticket)
                .addParameter("term","Dawei Zhuang")
//                .addParameter("include","path")
//                .addParameter("fields","name,id,isFile")
                .build();
        request.setURI(uri);

        request.addHeader("Accept", "application/json");
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
    }

    //test file a pic aa814a5e-7a7f-4f60-b053-2c8c1bebe467
    @Test
    public void testDownloadFile() throws IOException {
        String url = "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/nodes/aa814a5e-7a7f-4f60-b053-2c8c1bebe467/content" + "?alf_ticket=" + ticket;

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        request.addHeader("content-type", "application/json");
        request.addHeader("Accept", "application/json");
        HttpResponse response = client.execute(request);
        File theFile = new File("/Users/daweizhuang/Desktop/thefile.jpg");

        try (BufferedInputStream input = new BufferedInputStream(response.getEntity().getContent());
             BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(theFile))) {
            int data;
            while ((data = input.read()) != -1) {
                out.write(data);
            }
        }
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());
    }

    @Test
    public void testUploadFile()throws IOException {
        String url = "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/nodes/e693e6d7-9384-419a-8b4c-edbf7f9fac97/children" + "?alf_ticket=" + ticket;

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);

        File payload = new File("/Users/daweizhuang/Desktop/SEP-Calendar.pdf");

        HttpEntity entity = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addBinaryBody("filedata", payload)
                .addTextBody("name", "fileName123.pdf")
                .addTextBody("nodeType", "cm:content")
                .addTextBody("overwrite","true")
                .build();
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
    }

}
