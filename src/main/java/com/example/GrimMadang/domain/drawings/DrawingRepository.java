package com.example.GrimMadang.domain.drawings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DrawingRepository extends JpaRepository<Drawing, Integer> {
    Optional<Drawing> findByUser_PhoneNumber(String phoneNumber);
}