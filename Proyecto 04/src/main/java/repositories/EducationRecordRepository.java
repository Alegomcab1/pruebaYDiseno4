
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import domain.EducationRecord;

public interface EducationRecordRepository extends JpaRepository<EducationRecord, Integer> {

}
