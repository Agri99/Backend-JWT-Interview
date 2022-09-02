package backend.interview.services;

import java.util.List;

import backend.interview.models.Item;
import backend.interview.models.User;

public interface UserService {

    User saveUser(User user);

    Item saveItem(Item item);
    
    void addItemToUser(String username, String itemName);

    User getUser(String username);

    List<User> getUsers();
    
}
