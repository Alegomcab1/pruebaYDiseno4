
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;
import domain.HandyWorker;

@Repository
public interface HandyWorkerRepository extends JpaRepository<HandyWorker, Integer> {

	@Query("select a.applications from HandyWorker a where a= ?1 ")
	public List<Application> getAllApplicationsFromAHandyWorker(HandyWorker a);

	@Query("select a from HandyWorker join a.userAccount b where b.userName = ?1")
	public HandyWorker getHandyWorkerByUserName(String a);

}
