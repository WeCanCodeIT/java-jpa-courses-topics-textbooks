package courses;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

public interface TopicRepository extends CrudRepository<Topic, Long>{

}
