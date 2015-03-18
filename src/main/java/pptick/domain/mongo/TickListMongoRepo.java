package pptick.domain.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import pptick.domain.TickList;

public interface TickListMongoRepo extends MongoRepository<TickList, String> {

	Page<TickList> findByPropertyId(String propertyId, Pageable pageable);
}
