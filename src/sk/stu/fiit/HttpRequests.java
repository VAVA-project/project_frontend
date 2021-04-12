/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author adamf
 */
public class HttpRequests {
    
    static void sendPost() throws Exception {

        HttpPost httpPost = new HttpPost("http://localhost:8080/api/v1/register");
        
        httpPost.setHeader("Content-Type", "application/xml;charset=UTF-8");
        StringBuilder postXmlRequestBody = new StringBuilder();
        postXmlRequestBody.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        postXmlRequestBody.append("<RegisterRequest>");
        postXmlRequestBody.append("<dateOfBirth>").append(Singleton.getInstance().getUser().getDateOfBirth()).append("</dateOfBirth>");
        postXmlRequestBody.append("<email>").append(Singleton.getInstance().getUser().getEmail()).append("</email>");
        postXmlRequestBody.append("<firstName>").append(Singleton.getInstance().getUser().getFirstName()).append("</firstName>");
        postXmlRequestBody.append("<lastName>").append(Singleton.getInstance().getUser().getLastName()).append("</lastName>");
        postXmlRequestBody.append("<password>").append(Singleton.getInstance().getUser().getPassword()).append("</password>");
        postXmlRequestBody.append("<photo>").append(Singleton.getInstance().getUser().getPhotoString()).append("</photo>");
        postXmlRequestBody.append("<type>").append(Singleton.getInstance().getUser().getUserType()).append("</type>");
        postXmlRequestBody.append("</RegisterRequest>");

        httpPost.setEntity(new StringEntity(postXmlRequestBody.toString()));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(httpPost)) {
            Singleton.getInstance().setJwtToken(EntityUtils.toString(response.getEntity()));
        }
    }
}
