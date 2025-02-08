package com.patrones.back.db.jpa;

import com.patrones.back.db.orm.UserORM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJPA extends JpaRepository<UserORM, Long> {


    @Override
    List<UserORM> findAll();
}
