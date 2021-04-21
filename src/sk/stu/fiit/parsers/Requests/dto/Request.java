/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import org.apache.http.client.methods.HttpRequestBase;
import sk.stu.fiit.parsers.Requests.IRequest;

/**
 *
 * @author Adam Bublavý
 */
public abstract class Request implements IRequest {
    
    private HttpRequestBase request;

    public HttpRequestBase getRequest() {
        return request;
    }

    public void setRequest(HttpRequestBase request) {
        this.request = request;
    }
    
}
