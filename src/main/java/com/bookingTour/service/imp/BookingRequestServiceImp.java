package com.bookingTour.service.imp;

import com.bookingTour.dao.BookingRequestDAO;
import com.bookingTour.dao.TourDetailDAO;
import com.bookingTour.dao.UserDAO;
import com.bookingTour.entity.*;
import com.bookingTour.model.*;
import com.bookingTour.model.enu.Status;
import com.bookingTour.service.BookingRequestService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingRequestServiceImp implements BookingRequestService {

    private static final Logger logger = LoggerFactory.getLogger(BookingRequestServiceImp.class);

    private BookingRequestDAO bookingRequestDAO;
    private TourDetailDAO tourDetailDAO;
    private UserDAO userDAO;

    public static Logger getLogger() {
        return logger;
    }

    public BookingRequestDAO getBookingRequestDAO() {
        return bookingRequestDAO;
    }

    public void setBookingRequestDAO(BookingRequestDAO bookingRequestDAO) {
        this.bookingRequestDAO = bookingRequestDAO;
    }

    public TourDetailDAO getTourDetailDAO() {
        return tourDetailDAO;
    }

    public void setTourDetailDAO(TourDetailDAO tourDetailDAO) {
        this.tourDetailDAO = tourDetailDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public BookingRequestInfo findBookingRequest(Long id) {
        logger.info("Checking the BookingRequest in the database");
        try {
            BookingRequest bookingRequest = bookingRequestDAO.find(id);
            BookingRequestInfo bookingRequestInfo = null;
            if (bookingRequest != null) {
                bookingRequestInfo = convertToInfo(bookingRequest);
            }
            return bookingRequestInfo;
        } catch (Exception e) {
            logger.error("An error occurred while fetching the BookingRequest details from the database", e);
            return null;
        }
    }

    @Override
    @Transactional
    public BookingRequestInfo addBookingRequest(BookingRequestInfo bookingRequestInfo) throws Exception {
        logger.info("Adding the BookingRequest in the database");
        try {
            TourDetail tourDetail = tourDetailDAO.find(bookingRequestInfo.getTourDetailId());
            if (tourDetail == null)
                return null;
            User user = userDAO.find(bookingRequestInfo.getUserId());
            if (user == null)
                return null;
            BookingRequest bookingRequest = new BookingRequest();
            bookingRequest.setStartDate(new Timestamp(bookingRequestInfo.getStartDate().getTime()));
            bookingRequest.setStatus(Status.PENDING.value);
            bookingRequest.setTourDetail(tourDetail);
            bookingRequest.setUser(user);
            bookingRequest = bookingRequestDAO.makePersistent(bookingRequest);
            bookingRequestInfo = convertToInfo(bookingRequest);
            return bookingRequestInfo;
        } catch (Exception e) {
            logger.error("An error occurred while adding the BookingRequest to the database", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public BookingRequestInfo editBookingRequest(BookingRequestInfo bookingRequestInfo) throws Exception {
        logger.info("Updating the bookingRequest in the database");
        try {
            BookingRequest bookingRequest = bookingRequestDAO.find(bookingRequestInfo.getId(), true);
            if (bookingRequestInfo.getStatus() != null) {
                bookingRequest.setStatus(bookingRequestInfo.getStatus().value);
            }
            if (bookingRequestInfo.getStartDate() != null) {
                bookingRequest.setStartDate(new Timestamp(bookingRequestInfo.getStartDate().getTime()));
            }
            bookingRequest = bookingRequestDAO.makePersistent(bookingRequest);
            bookingRequestInfo = convertToInfo(bookingRequest);
            return bookingRequestInfo;
        } catch (Exception e) {
            logger.error("An error occurred while updating the bookingRequest to the database", e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = false)
    public boolean deleteBookingRequest(BookingRequestInfo bookingRequestInfo) throws Exception {
        logger.info("Deleting the bookingRequest in the database");
        try {
            BookingRequest bookingRequest = bookingRequestDAO.find(bookingRequestInfo.getId(), true);
            bookingRequestDAO.makeTransient(bookingRequest);
            return true;
        } catch (Exception e) {
            logger.error("An error occurred while deleting the bookingRequest to the database", e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingRequestInfo> paginate(BookingRequestInfo bookingRequestInfo) {
        try {
            String query = buildQuery(bookingRequestInfo);
            Map<String, Object> params = buildParams(bookingRequestInfo);
            Page<BookingRequest> bookingRequests = bookingRequestDAO.paginate(bookingRequestInfo, query, params);
            logger.info("bookingRequests: " + bookingRequests.getTotalElements());
            return bookingRequests.map(bookingRequest -> {
                BookingRequestInfo model = new BookingRequestInfo();
                model = convertToInfo(bookingRequest);
                return model;
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    @Transactional(readOnly = false)
    public boolean deleteBookingRequestByTourDetail(TourDetail tourDetail) throws Exception {
        logger.info("Deleting the bookingRequest by tour-detail in the database");
        try {
            Criterion tourDetailId = Restrictions.eq("tourDetail.id", tourDetail.getId());
            Criterion status = Restrictions.eq("status", Status.CANCELLED.value);
            List<BookingRequest> bookingRequests = bookingRequestDAO.findByCriteria(tourDetailId);
            for (BookingRequest bookingRequestDel : bookingRequests) {
                bookingRequestDAO.makeTransient(bookingRequestDel);
            }
            return true;
        } catch (Exception e) {
            logger.error("An error occurred while deleting the bookingRequest by tour-detail to the database", e);
            throw e;
        }
    }

    private HashMap<String, Object> buildParams(BookingRequestInfo condition) {
        HashMap<String, Object> params = new HashMap<>();
        if (condition.getStatus() != null)
            params.put("status", condition.getStatus().value);
        if (condition.getUserId() != null)
            params.put("userId", condition.getUserId());
        if (condition.getUserName() != null && StringUtils.isNotEmpty(condition.getUserName()))
            params.put("userName", "%" + condition.getUserName() + "%");
        if (condition.getTourName() != null && StringUtils.isNotEmpty(condition.getTourName()))
            params.put("tourName", "%" + condition.getTourName() + "%");
        return params;
    }

    private String buildQuery(BookingRequestInfo condition) {
        String query = " from BookingRequest b where 1=1 ";
        if (condition.getStatus() != null)
            query += " and b.status = :status ";
        if (condition.getUserId() != null)
            query += " and b.user.id = :userId ";
        if (condition.getTourName() != null && StringUtils.isNotEmpty(condition.getTourName()))
            query += " and b.tourDetail.tour.name like :tourName ";
        if (condition.getUserName() != null && StringUtils.isNotEmpty(condition.getUserName()))
            query += " and b.user.userName like :userName ";
        return query;
    }

    private BookingRequestInfo convertToInfo(BookingRequest bookingRequest) {
        BookingRequestInfo bookingRequestInfo = new BookingRequestInfo();
        TourDetailInfo tourDetailInfo = new TourDetailInfo();
        TourInfo tourInfo = new TourInfo();
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(bookingRequest, bookingRequestInfo);
        BeanUtils.copyProperties(bookingRequest.getTourDetail().getTour(), tourInfo);
        BeanUtils.copyProperties(bookingRequest.getTourDetail(), tourDetailInfo);
        BeanUtils.copyProperties(bookingRequest.getUser(), userModel);
        bookingRequestInfo.setTourDetailId(tourDetailInfo.getId());
        bookingRequestInfo.setUserId(userModel.getId());
        tourDetailInfo.setTour(tourInfo);
        bookingRequestInfo.setTourDetail(tourDetailInfo);
        bookingRequestInfo.setUser(userModel);
        bookingRequestInfo.setStartDate(new Date(bookingRequest.getStartDate().getTime()));
        bookingRequestInfo.setStatus(Status.getStatus(bookingRequest.getStatus()));
        return bookingRequestInfo;
    }

}
