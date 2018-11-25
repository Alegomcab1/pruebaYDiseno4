
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import domain.Curriculum;

public interface CurriculumRepository extends JpaRepository<Curriculum, Integer> {

}
