/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import org.apache.http.client.methods.HttpRequestBase;
import sk.stu.fiit.parsers.Requests.IRequest;

/**
 * Request represents abstract request that will be passed to API
 *
 * @author Adam Bublavý
 */
public abstract class Request implements IRequest {

    private HttpRequestBase request;

    /**
     * Returns Http request which was created after accepting visitor
     *
     * @return Returns Http request for API
     */
    public HttpRequestBase getRequest() {
        return request;
    }

    /**
     * Sets Http request. This method is used by visitors
     *
     * @param request Http request that will be set
     */
    public void setRequest(HttpRequestBase request) {
        this.request = request;
    }

}
