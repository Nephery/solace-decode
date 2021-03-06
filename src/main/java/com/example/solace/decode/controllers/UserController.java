package com.example.solace.decode.controllers;

        import com.example.solace.decode.Services.UserService;
import com.example.solace.decode.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "users")
public class UserController {

           private final UserService userService;

            @Autowired
    public UserController(UserService userService) {
                this.userService = userService;
                    }

            @CrossOrigin
    @PostMapping("create")
    public void createUser(@RequestBody User user) {
                userService.createUser(user);
            }
}