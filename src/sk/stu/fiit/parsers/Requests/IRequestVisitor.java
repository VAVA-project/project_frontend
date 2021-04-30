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
import sk.stu.fiit.parsers.Requests.dto.UserRequest;

/**
 * IRequestVisitor defines methods that every visitor must implement.
 *
 * @author Adam Bublavý
 *
 * @see IRequest
 */
public interface IRequestVisitor {

    /**
     * Constructs registration request for API
     *
     * @param request Registration data that will be passed in request to API
     *
     * @see RegisterRequest
     */
    public void constructRegisterRequest(RegisterRequest request);

    /**
     * Constructs login request for API
     *
     * @param request Login data that will be passed in request to API
     *
     * @see LoginRequest
     */
    public void constructLoginRequest(LoginRequest request);

    /**
     * Constructs edit user's profile request for API
     *
     * @param request EditRequest data that will be passed in request to API
     *
     * @see EditRequest
     */
    public void constructEditRequest(EditRequest request);

    /**
     * Constructs GuideToursRequest which fetch all tours created by given user
     *
     * @param request GuideToursRequest data that will be passed in request to
     * API
     *
     * @see GuideToursRequest
     */
    public void constructGuideToursRequest(GuideToursRequest request);

    /**
     * Constructs search request which is used to search tour offers which
     * contains given expression
     *
     * @param request SearchRequest data that will be passed in request to API
     *
     * @see SearchRequest
     */
    public void constructSearchRequest(SearchRequest request);

    /**
     * Constructs TourDatesRequest for API
     *
     * @param request TourDatesRequest data that will be passed in request to
     * API
     *
     * @see TourDatesRequest
     */
    public void constructTourDatesRequest(TourDatesRequest request);

    /**
     * Constructs request to create new tour offer
     *
     * @param request CreateTourOfferRequest data that will be passed in request
     * to API
     *
     * @see CreateTourOfferRequest
     */
    public void constructCreateTourOfferRequest(CreateTourOfferRequest request);

    /**
     * Constructs request to edit given tour offer
     *
     * @param request EditTourOfferRequest data that will be passed in request
     * to API
     *
     * @see EditTourOfferRequest
     */
    public void constructEditTourOfferRequest(EditTourOfferRequest request);

    /**
     * Constructs delete tour offer request for API
     *
     * @param request DeleteTourOfferRequest data that will be passed in request
     * to API
     *
     * @see DeleteTourOfferRequest
     */
    public void constructDeleteTourOfferRequest(DeleteTourOfferRequest request);

    /**
     * Constructs request which is used to create new tour date for given tour
     * offer
     *
     * @param request CreateTourDateRequest data that will be passed in request
     * to API
     *
     * @see CreateTourDateRequest
     */
    public void constructCreateTourDateRequest(CreateTourDateRequest request);

    /**
     * Constructs delete tour date request
     *
     * @param request DeleteTourDateRequest data that will be passed in request
     * to API
     *
     * @see DeleteTourDateRequest
     */
    public void constructDeleteTourDateRequest(DeleteTourDateRequest request);

    /**
     * Constructs request which is used to fetch available tickets for given
     * tour date
     *
     * @param request TicketsRequest data that will be passed in request to API
     *
     * @see TicketsRequest
     */
    public void constructTourTicketsRequest(TicketsRequest request);

    /**
     * Constructs add ticket to cart request
     *
     * @param request AddTicketToCartRequest data that will be passed in request
     * to API
     *
     * @see AddTicketToCartRequest
     */
    public void constructAddTicketToCartRequest(AddTicketToCartRequest request);

    /**
     * Constructs delete ticket from the user's cart request
     *
     * @param request DeleteTicketFromCartRequest data that will be passed in
     * request to API
     *
     * @see DeleteTicketFromCartRequest
     */
    public void constructDeleteTicketFromCartRequest(
            DeleteTicketFromCartRequest request);

    /**
     * Constructs checkout cart request
     *
     * @param request CheckoutTicketsInCartRequest data that will be passed in
     * request to API
     *
     * @see CheckoutTicketsInCartRequest
     */
    public void constructCheckoutTicketsInCartRequest(
            CheckoutTicketsInCartRequest request);

    /**
     * Constructs delete cart request
     *
     * @param request DeleteCartRequest data that will be passed in request to
     * API
     *
     * @see DeleteCartRequest
     */
    public void constructDeleteCartRequest(DeleteCartRequest request);

    /**
     * Constructs request which is used to fetch all user's booked tours
     *
     * @param request UserBookingsRequest data that will be passed in request to
     * API
     *
     * @see UserBookingsRequest
     */
    public void constructUserBookingsRequest(UserBookingsRequest request);

    /**
     * Constructs request which is used to fetch all user's completed tours
     *
     * @param request UserCompletedBookingsRequest data that will be passed in
     * request to API
     *
     * @see UserCompletedBookingsRequest
     */
    public void constructUserCompletedBookingsRequest(
            UserCompletedBookingsRequest request);

    /**
     * Constructs tour rating request
     *
     * @param request RatingRequest data that will be passed in request to API
     *
     * @see RatingRequest
     */
    public void constructRatingRequest(RatingRequest request);

    /**
     * Constructs user request which is used to fetch data about given user
     *
     * @param request UserRequest data that will be passed in request to API
     *
     * @see UserRequest
     */
    public void constructUserRequest(UserRequest request);

}
