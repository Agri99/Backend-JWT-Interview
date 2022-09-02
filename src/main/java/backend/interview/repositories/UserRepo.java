package backend.interview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.interview.models.User;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);
    
}
