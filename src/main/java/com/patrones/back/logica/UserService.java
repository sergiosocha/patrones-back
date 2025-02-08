package com.patrones.back.logica;


import com.patrones.back.db.jpa.UserJPA;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserJPA userJPA;



}
