package pptick.domain.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import pptick.domain.Tick;

public interface TickMongoRepo extends MongoRepository<Tick, String> {

	Page<Tick> findByPropertyId(String propertyId, Pageable pageable);
}
