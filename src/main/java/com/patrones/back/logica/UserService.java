package com.patrones.back.logica;


import com.patrones.back.db.jpa.UserJPA;
import com.patrones.back.db.orm.UserORM;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserJPA userJPA;


    public boolean guardarUser(String nombre){
        UserORM nuevoUser = new UserORM();
        nuevoUser.setNombre(nombre);
        userJPA.save(nuevoUser);
        return  true;
    }

    public List<UserORM> listUser(){
        return userJPA.findAll();
    }





}
