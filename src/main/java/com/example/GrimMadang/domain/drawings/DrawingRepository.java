package com.example.GrimMadang.domain.drawings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DrawingRepository extends JpaRepository<Drawing, UUID> {
    List<Drawing> findAllByUser_PhoneNumber(String phoneNumber);
}