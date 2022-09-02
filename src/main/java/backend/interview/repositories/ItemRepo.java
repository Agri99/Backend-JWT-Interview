package backend.interview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.interview.models.Item;

public interface ItemRepo extends JpaRepository<Item, Long> {

    Item findByName(String itemName);
    
}
