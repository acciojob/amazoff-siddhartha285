package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
        // your code here
        orderMap.put(order.getId(),order);
    }

    public void savePartner(String partnerId){
        // your code here
        // create a new partner with given partnerId and save it
        partnerMap.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            // your code here
            //add order to given partner's order list
            //increase order count of partner
            //assign partner to this order
            HashSet<String> orders=partnerToOrderMap.getOrDefault(partnerId,new HashSet<>());
            orders.add(orderId);
            partnerToOrderMap.put(partnerId,orders);
            DeliveryPartner partner =partnerMap.get(partnerId);
            partner.setNumberOfOrders(partner.getNumberOfOrders()+1);
            orderToPartnerMap.put(orderId,partnerId);
        }
    }

    public Order findOrderById(String orderId){
        // your code here
        return orderMap.get(orderId);
    }

    public DeliveryPartner findPartnerById(String partnerId){
        // your code here
        return partnerMap.get(partnerId);
    }

    public Integer findOrderCountByPartnerId(String partnerId){
        // your code here
        return partnerMap.get(partnerId).getNumberOfOrders();
    }

    public List<String> findOrdersByPartnerId(String partnerId){
        // your code here
        return new ArrayList<>(partnerToOrderMap.getOrDefault(partnerId,new HashSet<>()));
    }

    public List<String> findAllOrders(){
        // your code here
        // return list of all orders
        return new ArrayList<>(orderMap.keySet());
    }

    public void deletePartner(String partnerId){
        // your code here
        // delete partner by ID
        partnerMap.remove(partnerId);
        HashSet<String>orders=partnerToOrderMap.getOrDefault(partnerId,new HashSet<>());
        for(String orderId:orders)
        {
            orderToPartnerMap.remove(orderId);
        }
        partnerToOrderMap.remove(partnerId);
    }

    public void deleteOrder(String orderId){
        // your code here
        // delete order by ID
        orderMap.remove(orderId);
        String partnerId = orderToPartnerMap.get(orderId);
        if (partnerId != null) {
            HashSet<String> orders = partnerToOrderMap.get(partnerId);
            if (orders != null) {
                orders.remove(orderId);
                DeliveryPartner partner = partnerMap.get(partnerId);
                partner.setNumberOfOrders(partner.getNumberOfOrders() - 1);
            }
            orderToPartnerMap.remove(orderId);
        }
    }

    public Integer findCountOfUnassignedOrders(){
        // your code here
        return orderMap.size()-orderToPartnerMap.size();
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId){
        // your code here
        int time=Integer.parseInt(timeString.replace(":",""));
        HashSet<String> orders=partnerToOrderMap.getOrDefault(partnerId,new HashSet<>());
        int count=0;
        for(String orderId:orders)
        {
            Order order=orderMap.get(orderId);
            if(order.getDeliveryTime()>time){
                count++;
            }
        }
        return count;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId){
        // your code here
        // code should return string in format HH:MM
        HashSet<String> orders = partnerToOrderMap.getOrDefault(partnerId, new HashSet<>());
        int maxTime = 0;
        for (String orderId : orders) {
            Order order = orderMap.get(orderId);
            if (order.getDeliveryTime() > maxTime) {
                maxTime = order.getDeliveryTime();
            }
        }
        int hours = maxTime / 60;
        int minutes = maxTime % 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}