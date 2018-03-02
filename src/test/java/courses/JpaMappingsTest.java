package courses;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class JpaMappingsTest {

	@Resource
	private TestEntityManager entityManager;

	@Resource
	private CourseRepository courseRepo;

	@Resource
	private TextbookRepository textbookRepo;

	@Resource
	private TopicRepository topicRepo;
	
	@Test
	public void shouldSaveAndLoadCourse() {
		Course course = new Course("its name");
		course = courseRepo.save(course);
		// more concise to do:
		//	Course course = courseRepo.save(new Course("its name", "its description"));
		long courseId = course.getId();

		entityManager.flush(); // forces pending stuff to happen
		entityManager.clear(); // detaches all entities, forces jpa to hit the db when we try to find an entity

		course = courseRepo.findOne(courseId);

		assertThat(course.getName(), is("its name"));
	}

	@Test
	public void shouldSaveTextbookToCourseRelationship() {

		Course course = new Course("its name");
		// first we save the thing that does NOT own the relationship (so that we have
		// an id to be used as a foreign key)
		courseRepo.save(course);
		long courseId = course.getId();

		Textbook first = new Textbook("foo", course);
		textbookRepo.save(first);

		Textbook second = new Textbook("bar", course);
		textbookRepo.save(second);

		entityManager.flush(); // forces pending stuff to happen
		entityManager.clear(); // forces jpa to hit the db when we try to find it

		course = courseRepo.findOne(courseId);
		assertThat(course.getTextbooks(), containsInAnyOrder(first, second));
	}

	@Test
	public void shouldSaveAndLoadTopic() {
		Topic topic = topicRepo.save(new Topic("its name"));
		long topicId = topic.getId();

		entityManager.flush(); // forces jpa to hit the db when we try to find it
		entityManager.clear();

		topic = topicRepo.findOne(topicId);
		assertThat(topic.getName(), is("its name"));
	}
	
	@Test
	public void shouldGenerateTopicId() {
		Topic topic = topicRepo.save(new Topic("its name"));
		long topicId = topic.getId();

		entityManager.flush(); // forces jpa to hit the db when we try to find it
		
		assertThat(topicId, is(greaterThan(0L)));
	}
	
	@Test
	public void shouldEstablishCourseToTopicsRelationships() {
		// topic is not the owner, so we save these first
		Topic java = topicRepo.save(new Topic("Java"));
		Topic ruby = topicRepo.save(new Topic("Ruby"));
		
		Course course = new Course("OO Languages", java, ruby);
		course = courseRepo.save(course);
		long ooLanguagesId = course.getId();
		
		entityManager.flush();
		entityManager.clear();
		
		course = courseRepo.findOne(ooLanguagesId);
		assertThat(course.getTopics(), containsInAnyOrder(java, ruby));
		
	}
	
	@Test
	public void shouldEstablishTopicToCoursesRelationship() {
		Topic topic = topicRepo.save(new Topic("Ruby"));
		long topicId = topic.getId();

		Course ooLanguages = new Course("OO Languages", topic);
		ooLanguages = courseRepo.save(ooLanguages);
		
		Course scriptingLanguages = new Course("Scripting Languages", topic);
		scriptingLanguages = courseRepo.save(scriptingLanguages);
		
		entityManager.flush();
		entityManager.clear();
	
		topic = topicRepo.findOne(topicId);
		assertThat(topic.getCourses(), containsInAnyOrder(ooLanguages, scriptingLanguages));
	}
	
	@Test
	public void shouldFindCoursesForTopic() {
		Topic topic = topicRepo.save(new Topic("Ruby"));
		long topicId = topic.getId();

		Course ooLanguages = new Course("OO Languages", topic);
		ooLanguages = courseRepo.save(ooLanguages);
		
		Course scriptingLanguages = new Course("Scripting Languages", topic);
		scriptingLanguages = courseRepo.save(scriptingLanguages);
		
		entityManager.flush();
		entityManager.clear();
		
		topic = topicRepo.findOne(topicId);
		Collection<Course> coursesForTopic = courseRepo.findByTopicsContains(topic);
		assertThat(coursesForTopic, containsInAnyOrder(ooLanguages, scriptingLanguages));
	}
	
	@Test
	public void shouldFindCoursesForTopicId() {
		Topic topic = topicRepo.save(new Topic("Ruby"));
		long topicId = topic.getId();

		Course ooLanguages = new Course("OO Languages", topic);
		ooLanguages = courseRepo.save(ooLanguages);
		
		Course scriptingLanguages = new Course("Scripting Languages", topic);
		scriptingLanguages = courseRepo.save(scriptingLanguages);
		
		entityManager.flush();
		entityManager.clear();
		
		Collection<Course> coursesForTopic = courseRepo.findByTopicsId(topicId);
		assertThat(coursesForTopic, containsInAnyOrder(ooLanguages, scriptingLanguages));
	}

}
