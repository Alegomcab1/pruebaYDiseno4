
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import domain.PersonalRecord;

public interface PersonalRecordRepository extends JpaRepository<PersonalRecord, Integer> {

}
