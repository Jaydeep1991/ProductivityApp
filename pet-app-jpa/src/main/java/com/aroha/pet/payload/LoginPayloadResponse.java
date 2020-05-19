package com.aroha.pet.payload;


/**
 *
 * @author Jaydeep  | Date: 15 May, 2020  11:29:46 AM
 */
public class LoginPayloadResponse {
    
   private String name;
   private String logedeInTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogedeInTime() {
        return logedeInTime;
    }

    public void setLogedeInTime(String logedeInTime) {
        this.logedeInTime = logedeInTime;
    }
   
   
}
