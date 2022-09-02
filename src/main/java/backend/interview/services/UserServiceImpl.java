package backend.interview.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import backend.interview.models.Item;
import backend.interview.models.User;
import backend.interview.repositories.ItemRepo;
import backend.interview.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    
    private final UserRepo userRepo;
    private final ItemRepo itemRepo;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User  user = userRepo.findByUsername(username);
        return new User(user.getAuthorities(), user.getUsername(), user.getPassword(), true, true, true, user.isEnabled());
    }

    @Override
    public void addItemToUser(String username, String itemName) {
        log.info("Menambahkan item {} ke user {}", itemName, username);
        User user = userRepo.findByUsername(username);
        Item item = itemRepo.findByName(itemName);
        user.getItems().add(item);
    }

    @Override
    public User getUser(String username) {
        log.info("Memanggil user {}", username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("Memanggil semua user");
        return userRepo.findAll();
    }

    @Override
    public Item saveItem(Item item) {
        log.info("Menyimpan item {} ke database", item.getName());
        return itemRepo.save(item);
    }

    @Override
    public User saveUser(User user) {
        log.info("Menyimpan user {} ke database", user.getUsername());
        return userRepo.save(user);
    }
    
}
