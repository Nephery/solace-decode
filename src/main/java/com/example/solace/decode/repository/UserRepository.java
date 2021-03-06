package com.example.solace.decode.repository;

        import com.example.solace.decode.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

        import java.util.List;

        public interface UserRepository extends JpaRepository<User, String> {
    List<User> findAllByName(String name);
}