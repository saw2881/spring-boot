package com.subsaw.ilearn.security.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.subsaw.ilearn.security.entity.User;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
    
    List<User> findByUsername(String username);
}
