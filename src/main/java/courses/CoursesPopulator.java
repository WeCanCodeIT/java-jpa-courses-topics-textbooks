package courses;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CoursesPopulator implements CommandLineRunner {

	@Resource
	private CourseRepository courseRepo;
	
	@Resource
	private TextbookRepository textbookRepo;
	
	@Resource
	private TopicRepository topicRepo;
	
	@Override
	public void run(String... args) throws Exception {
		
		Topic java = new Topic("Java");
		java = topicRepo.save(java);
		Topic tdd = new Topic("TDD");
		tdd = topicRepo.save(tdd);
		// we save on the *one* side of the relationship first
		Course course = courseRepo.save(new Course("Java One", java, tdd));
		// then the things on the *many* side.
		textbookRepo.save(new Textbook("Head First Java", course));
		textbookRepo.save(new Textbook("Head First Design Patterns", course));
	}

}
