package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

@Getter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData){
        this.id = id;
        this.method = method;
        this.status = "PENDING";

        if (paymentData.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            this.paymentData = paymentData;
        }
    }

    public Payment(String id, String method, Map<String, String> paymentData, String status){
        this(id, method, paymentData);
        String[] statusList = {"PENDING", "SUCCESS", "REJECTED"};
        if(Arrays.stream(statusList).noneMatch(item -> (item.equals(status)))){
            throw new IllegalArgumentException();
        } else {
            this.status = status;
        }
    }

    public void setStatus(String status){
        String[] statusList = {"PENDING", "SUCCESS", "REJECTED"};
        if(Arrays.stream(statusList).noneMatch(item -> (item.equals(status)))){
            throw new IllegalArgumentException();
        } else {
            this.status = status;
        }
    }
}