package ru.ilia.data.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import ru.ilia.data.model.ZprimeItem;

import java.util.List;

@EnableScan
public interface HotelRepository extends CrudRepository<ZprimeItem, String>, CustomHotelRepository {
    List<ZprimeItem> findAllByName(String name);
}
