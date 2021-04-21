/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Requests;

import sk.stu.fiit.parsers.Requests.dto.EditRequest;
import sk.stu.fiit.parsers.Requests.dto.GuideToursRequest;
import sk.stu.fiit.parsers.Requests.dto.LoginRequest;
import sk.stu.fiit.parsers.Requests.dto.RegisterRequest;
import sk.stu.fiit.parsers.Requests.dto.SearchRequest;
import sk.stu.fiit.parsers.Requests.dto.TicketsRequest;
import sk.stu.fiit.parsers.Requests.dto.TourDatesRequest;

/**
 *
 * @author Adam Bublavý
 */
public interface IRequestVisitor {
    
    public void constructRegisterRequest(RegisterRequest request);
    public void constructLoginRequest(LoginRequest request);
    public void constructEditRequest(EditRequest request);
    public void constructGuideToursRequest(GuideToursRequest request);
    public void constructSearchRequest(SearchRequest request);
    public void constructTourDatesRequest(TourDatesRequest request);
    public void constructTourTicketsRequest(TicketsRequest request);
    
}
