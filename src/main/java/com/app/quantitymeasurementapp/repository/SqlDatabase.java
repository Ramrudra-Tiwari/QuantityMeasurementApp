package com.app.quantitymeasurementapp.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.quantitymeasurementapp.model.User;

@Repository
public interface  SqlDatabase extends JpaRepository<User,Long> {
     public  User findByEmail(String email);
}
