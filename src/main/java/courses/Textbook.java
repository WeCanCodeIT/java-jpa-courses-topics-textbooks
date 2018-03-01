package courses;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Textbook {

	@Id
	@GeneratedValue
	private long id;

	/**
	 * The owner is the Many side in a ManyToOne since in a database, it has the
	 * foreign key. It "owns" the relationship.
	 */
	@ManyToOne
	private Course course;

	public long getId() {
		return id;
	}

	public Course getCourse() {
		return course;
	}

	private Textbook() {}
	
	public Textbook(Course course) {
		this.course = course;
		// if we were cascading
		// course.addTextbook(this);
	}

	@Override
	public int hashCode() {
		return ((Long) id).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		Textbook other = (Textbook) obj;
		return id == other.id;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
}
