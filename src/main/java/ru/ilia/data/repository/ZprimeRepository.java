package ru.ilia.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ilia.data.model.ZprimeItem;

@Repository
public interface ZprimeRepository extends CrudRepository<ZprimeItem, String> {
}
