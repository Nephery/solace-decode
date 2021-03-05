package com.example.solace.decode.Services;

import com.example.solace.decode.model.User;
import com.example.solace.decode.repository.UserRepository;
import org.elasticsearch.common.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@Service
public class UserService {

    private UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user){ this.userRepository.save(user);}


}


