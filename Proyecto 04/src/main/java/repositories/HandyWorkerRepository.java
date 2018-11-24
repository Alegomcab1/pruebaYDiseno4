
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;
import domain.Customer;
import domain.FixUpTask;
import domain.HandyWorker;

@Repository
public interface HandyWorkerRepository extends JpaRepository<HandyWorker, Integer> {

	@Query("select a.applications from HandyWorker a where a= ?1 ")
	public List<Application> getAllApplicationsFromAHandyWorker(HandyWorker a);

	@Query("select a from HandyWorker join a.userAccount b where b.userName = ?1")
	public HandyWorker getHandyWorkerByUserName(String a);

	@Query("select f from Application a join a.fixUpTask f")
	public Collection<FixUpTask> getFixUpTasks();

	@Query("select c from Customer c join c.fixUpTasks f where f = ?1")
	public Customer getCustomerFromFixUpTask(FixUpTask f);

}
