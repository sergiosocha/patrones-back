package com.patrones.back.controller;


import com.patrones.back.controller.dto.UserDTO;
import com.patrones.back.db.orm.UserORM;
import com.patrones.back.logica.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class UserController {

    private UserService userService;
    List<UserDTO> users = new ArrayList<>();

    @PostMapping(path = "/user")
    public String guardarUser(@RequestBody UserDTO userDTO){
        userService.guardarUser(userDTO.nombre());
        return "El usuario fue guardado";
    }

    @GetMapping(path = "/user/todos")
    public List<UserORM> obtenerUsers(){
        return userService.listUser();
    }

}
