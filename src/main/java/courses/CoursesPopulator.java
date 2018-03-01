package courses;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CoursesPopulator implements CommandLineRunner {

	@Resource
	private CourseRepository courseRepo;
	
	@Resource
	private TextbookRepository textbookRepo;
	
	@Override
	public void run(String... args) throws Exception {
		// we save on the *one* side of the relationship first
		Course course = courseRepo.save(new Course("its name"));
		// then the things on the *many* side.
		textbookRepo.save(new Textbook(course));
		textbookRepo.save(new Textbook(course));
	}

}
