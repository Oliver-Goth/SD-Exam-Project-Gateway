//package com.example;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class App {
//
//    private static final Logger log = LoggerFactory.getLogger(App.class);
//
//    public static void main(String[] args) {
//        //Customer
//        simulateRequest("c123", "A list of all customers", "GET", "/api/customers",201);
//        simulateRequest("c123", "Created a new customer succesfully", "POST", "/api/customers/{id}",201);
//        simulateRequest("c123", "Created a guest customer succesfully", "POST", "/api/customers/guest",201);
//        simulateRequest("c123", "test", "DELETE", "/api/customers/{id}",201);
//    }
//
//        private static void simulateRequest(String userId, String msg , String method, String path, int status) {
//        RequestLogger.log(userId, method, path, status, () -> {
//            TimeIt.info(log, msg, () -> {
//                try {
//                    Thread.sleep(120);
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            });
//
//            log.info(msg);
//            AuditLogger.log("CUSTOMER_CREATE", userId, "customer:b-42", "203.0.113.7");
//        });
//    }
//}
