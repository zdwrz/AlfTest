package com.aweiz.alfresco;

import com.aweiz.alfreso.pojo.Ticket;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.List;

/**
 * Created by daweizhuang on 3/3/17.
 */
public class AlfrescoServiceImpl implements AlfrescoService{

    private static final String BASE_PUBLIC_URL = "http://localhost:8080/alfresco/api/-default-/public";
    private static final String BASE_WEB_SCRIPT_URL = "http://localhost:8080/alfresco/s/api";


    private static final String LOGIN_URL = BASE_PUBLIC_URL + "/authentication/versions/1/tickets";
    private static final String NODE_URL= BASE_PUBLIC_URL + "/alfresco/versions/1/queries/nodes";

    private static final String PERSON_URL = BASE_WEB_SCRIPT_URL + "/people";

    @Override
    public String login(String userName, String pwd) {
        String ticket = null;
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(LOGIN_URL);
        request.addHeader("content-type", "application/json");
        HttpEntity entity = new StringEntity("{\"userId\":\"" + userName+ "\",\"password\":\""+pwd+"\"}", ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        request.addHeader("Accept", "application/json");
        HttpResponse response = null;
        try {
            response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            Ticket o = (Ticket) new ObjectMapper().readValue(result.toString(), Ticket.class);
            ticket = o.getData().getId();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ticket;
    }

    @Override
    public Boolean validateTicket(String ticket) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(LOGIN_URL+"/-me-");
        request.addHeader("Authorization", "Basic " + new String(Base64.getEncoder().encode(ticket.getBytes())));
        boolean res = false;
        try {
            HttpResponse response = client.execute(request);
            int status = response.getStatusLine().getStatusCode();
            res = (status == 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public String addPerson() {
        return null;
    }

    @Override
    public String findPerson(String userName, String ticket) {
        String url = PERSON_URL+"/"+userName ;
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet();
        URI uri = null;
        String res = null;
        try {
            uri = new URIBuilder(url)
                    .addParameter("alf_ticket",ticket)
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
            res = result.toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<String> getFileListByPerson() {
        return null;
    }

    @Override
    public String findFile(String ticket) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet();
        String res = null;
        try {
            URI uri = new URIBuilder(NODE_URL)
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
            res = result.toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public String uploadFile() {
        return null;
    }
}
