
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Complaint;
import domain.Referee;

@Repository
public interface RefereeRepository extends JpaRepository<Referee, Integer> {

	@Query("select c from Complaint c where c.referee is null")
	Collection<Complaint> findUnassignedComplaints();

	@Query("select c from Complaint c join c.referee r where r.id == ?1")
	Collection<Complaint> findSelfAssignedComplaints(int refereeId);
	//977
}
