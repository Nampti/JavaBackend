package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.dto.OrderInfo;
import com.example.demo.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
