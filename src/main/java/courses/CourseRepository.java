package courses;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {

	List<Course> findByName(String name);

	Collection<Course> findByTopicsContains(Topic topic);

	Collection<Course> findByTopicsId(Long id);
}
