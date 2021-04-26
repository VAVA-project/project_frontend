/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Requests;


import sk.stu.fiit.parsers.Requests.dto.AddTicketToCartRequest;
import sk.stu.fiit.parsers.Requests.dto.CheckoutTicketsInCartRequest;
import sk.stu.fiit.parsers.Requests.dto.CreateTourDateRequest;
import sk.stu.fiit.parsers.Requests.dto.CreateTourOfferRequest;
import sk.stu.fiit.parsers.Requests.dto.DeleteCartRequest;
import sk.stu.fiit.parsers.Requests.dto.DeleteTicketFromCartRequest;
import sk.stu.fiit.parsers.Requests.dto.DeleteTourDateRequest;
import sk.stu.fiit.parsers.Requests.dto.DeleteTourOfferRequest;
import sk.stu.fiit.parsers.Requests.dto.EditRequest;
import sk.stu.fiit.parsers.Requests.dto.EditTourOfferRequest;
import sk.stu.fiit.parsers.Requests.dto.GuideToursRequest;
import sk.stu.fiit.parsers.Requests.dto.LoginRequest;
import sk.stu.fiit.parsers.Requests.dto.RatingRequest;
import sk.stu.fiit.parsers.Requests.dto.RegisterRequest;
import sk.stu.fiit.parsers.Requests.dto.SearchRequest;
import sk.stu.fiit.parsers.Requests.dto.TicketsRequest;
import sk.stu.fiit.parsers.Requests.dto.TourDatesRequest;
import sk.stu.fiit.parsers.Requests.dto.UserBookingsRequest;
import sk.stu.fiit.parsers.Requests.dto.UserCompletedBookingsRequest;

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
    public void constructCreateTourOfferRequest(CreateTourOfferRequest request);
    public void constructEditTourOfferRequest(EditTourOfferRequest request);
    public void constructDeleteTourOfferRequest(DeleteTourOfferRequest request);
    public void constructCreateTourDateRequest(CreateTourDateRequest request);
    public void constructDeleteTourDateRequest(DeleteTourDateRequest request);
    public void constructTourTicketsRequest(TicketsRequest request);
    public void constructAddTicketToCartRequest(AddTicketToCartRequest request);
    public void constructDeleteTicketFromCartRequest(DeleteTicketFromCartRequest request);
    public void constructCheckoutTicketsInCartRequest(CheckoutTicketsInCartRequest request);
    public void constructDeleteCartRequest(DeleteCartRequest request);
    public void constructUserBookingsRequest(UserBookingsRequest request);
    public void constructUserCompletedBookingsRequest(UserCompletedBookingsRequest request);
    public void constructRatingRequest(RatingRequest request);
    
}
