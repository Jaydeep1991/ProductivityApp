package com.aroha.pet.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.aroha.pet.model.LoginLogoutTime;
import com.aroha.pet.model.User;
import com.aroha.pet.payload.ForgetPasswordPayload;
import com.aroha.pet.payload.LoginPayloadResponse;
import com.aroha.pet.payload.LoginResponse;
import com.aroha.pet.repository.LoginLogoutTimeRepository;
import com.aroha.pet.security.UserPrincipal;
import java.util.ArrayList;

@Service
public class LoginLogoutService {

    @Autowired
    private LoginLogoutTimeRepository loginRepo;

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(LoginLogoutService.class);

    public void saveLoginTime(long userId) {
        Optional<LoginLogoutTime> loginObj = loginRepo.findById(userId);
        if (loginObj.isPresent()) {
            LoginLogoutTime obj = loginObj.get();
            Date date = new Date();
            obj.setLoginDateTime(date.toInstant());
            loginRepo.save(obj);
        } else {
            LoginLogoutTime login = new LoginLogoutTime();
            login.setUserId(userId);
            loginRepo.save(login);
        }
    }

    public ForgetPasswordPayload logout(UserPrincipal user) {
        Long roleId = userService.findUserRole(user.getId());
        if (roleId == 1) {
            Optional<LoginLogoutTime> loginObj = loginRepo.findById(user.getId());
            if (loginObj.isPresent()) {
                LoginLogoutTime obj = loginObj.get();
                Date date = new Date();
                Instant a = obj.getLoginDateTime();
                Instant b = date.toInstant();
                long seconds = a.until(b, ChronoUnit.SECONDS);
                Integer hours = (int) seconds / 3600;
                Integer rem = (int) seconds - hours * 3600;
                Integer min = rem / 60;
                rem = rem - min * 60;
                Integer sec = rem;
                String loggedInTime = "";
                if (hours < 10) {
                    loggedInTime += 0 + hours.toString() + ":";
                } else {
                    loggedInTime += hours.toString() + ":";
                }
                if (min < 10) {
                    loggedInTime += 0 + min.toString() + ":";
                } else {
                    loggedInTime += min.toString() + ":";
                }
                if (sec < 10) {
                    loggedInTime += 0 + sec.toString();
                } else {
                    loggedInTime += sec.toString();
                }
                obj.setLoggedInTime(loggedInTime);
                obj.setLogoutDateTime(date.toInstant());
                loginRepo.save(obj);
                return new ForgetPasswordPayload(HttpStatus.OK.value(), Boolean.TRUE, "Logged out successfully");
            }
        }
        return new ForgetPasswordPayload(HttpStatus.OK.value(), Boolean.TRUE, "Logged out successfully");
    }

    public LoginResponse showLoginDetails() {
        List<LoginLogoutTime> list = loginRepo.findAll();
        Iterator<LoginLogoutTime> itr = list.iterator();
        List<LoginPayloadResponse> listObj = new ArrayList<>();

        while (itr.hasNext()) {
            LoginLogoutTime obj = itr.next();
            LoginPayloadResponse loginObj = new LoginPayloadResponse();
            Optional<User> userObj = userService.findByLearnerId(obj.getUserId());
            if (!userObj.isPresent()) {
                throw new RuntimeException("User is not found");
            }
            User user = userObj.get();
            String getTime = "";
            String[] time = obj.getLoggedInTime().split(":");
            if (!time[0].equals("00")) {
                getTime += time[0] + " hours ";
            }
            if (!time[1].equals("00")) {
                getTime += time[1] + " minutes ";
            }
            if (!time[2].equals("00")) {
                getTime += time[2] + " seconds";
            }
            loginObj.setName(user.getName());
            loginObj.setLogedeInTime(getTime);
            listObj.add(loginObj);
        }
        if (listObj.isEmpty()) {
            return new LoginResponse(HttpStatus.BAD_REQUEST.value(), "Failed");
        }
        return new LoginResponse(HttpStatus.OK.value(), "Success", listObj);
    }

}