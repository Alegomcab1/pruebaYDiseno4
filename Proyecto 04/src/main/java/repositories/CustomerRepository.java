
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Application;
import domain.Complaint;
import domain.Customer;
import domain.FixUpTask;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	@Query("select c from Customer c join c.userAccount u where u.username = ?1")
	Customer getCustomerByUsername(String username);

	@Query("select c.fixUpTasks from Customer c where c.id = ?1")
	Collection<FixUpTask> findFixUpTasksById(int customerId);

	@Query("select f.complaints from Customer c join c.fixUpTasks f where c.id = ?1")
	Collection<Complaint> findComplaintsById(int customerId);

	@Query("select f.applications from Customer c join c.fixUpTasks f where c.id = ?1")
	Collection<Application> findApplicationsById(int customerId);
}
