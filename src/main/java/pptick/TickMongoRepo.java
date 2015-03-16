package pptick;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TickMongoRepo extends MongoRepository<Tick, String> {

}
