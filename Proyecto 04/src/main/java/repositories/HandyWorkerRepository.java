
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;
import domain.Complaint;
import domain.Customer;
import domain.Endorser;
import domain.Endorsment;
import domain.Finder;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Phase;
import domain.Report;
import domain.Tutorial;

@Repository
public interface HandyWorkerRepository extends JpaRepository<HandyWorker, Integer> {

	@Query("select a.applications from HandyWorker a where a.id = ?1")
	public List<Application> getAllApplicationsFromAHandyWorker(int id);

	@Query("select a from HandyWorker a where a.id = ?1")
	public HandyWorker getHandyWorkerById(int id);

	@Query("select a from Customer a join a.fixUpTasks b where b.id = ?1")
	public Customer getCustomerByFixUpTask(int id);

	//Se utiliza en deletePhaseForApplication
	@Query("select a.phases from FixUpTask a join a.application b where b.id = ?1")
	public Collection<Phase> getPhasesByApplication(int id);

	//Se utiliza en deletePhaseForHandyWorker
	@Query("select a.phases from FixUpTask a join a.applications b join b.handyWorker c where c.id = ?1")
	public Collection<Phase> getPhasesByHandyWorker(int id);

	@Query("select a.finder from HandyWorker a where a.id = ?1")
	public Finder getFinderFromAHandyWorker(int id);

	@Query("select a.fixUpTasks from Finder a where a.id = ?1")
	public List<FixUpTask> getResultFinder(int id);

	@Query("select a.fixUpTask from Application a where a.id = ?1")
	public FixUpTask getFixUpTaskFromApplication(int id);

	@Query("select distinct a.fixUpTask from Application a join a.handyWorker b where b.id = ?1")
	public List<FixUpTask> getFixUpTasksFromHandyWorker(int id);

	@Query("select c.id from Customer c join c.fixUpTasks f join f.applications a join a.handyWorker b where b.id = ?1")
	public List<Integer> getCustomersFromHandyWorker(int id);

	@Query("select distinct c.complaints from FixUpTask c join c.applications a join a.handyWorker h where h.id = ?1")
	public List<Complaint> getComplaintsFromHandyWorker(int id);

	@Query("select a from Complaint a where a.id = ?1")
	public Complaint getComplaintById(int id);

	@Query("select a from Report a where a.id = ?1")
	public Report getReportById(int id);

	/*
	 * @Query("select b.reports from Referee where b.id = (select a.referee from Complaint a where a.id = ?1).id ")
	 * public FixUpTask getReportFromComplaint(int id);
	 */

	@Query("select a.tutorials from HandyWorker a where a.id = ?1")
	public List<Tutorial> getAllTutorialsFromAHandyWorker(int id);

	@Query("select a from Endorser where a.id = ?1")
	public Endorser getEndorserById(int id);

	@Query("select a.endorsments from Endorser where a.id = ?1")
	public List<Endorsment> getEndorsmentsByEndorser(int id);

	@Query("select a from Application a where a.id = ?1")
	public Application getApplicationById(int id);

	//Se utiliza en deletePhaseForHandyWorker
	@Query("select a from Phase a where a.id = ?1")
	public Phase getPhaseById(int id);

	//Querys del Filtro de Finder
	@Query("select c from FixUpTask c where c.ticker like '?1' or c.description like '?1' or c.address like '?1'")
	public List<FixUpTask> getFixUpTaskByKeyWord(String keyWord);

	@Query("select f from FixUpTask f join f.categories c where c.name='?1'")
	public List<FixUpTask> getFixUpTaskByCategory(String category);

	@Query("select a from FixUpTask a join a.warranties b where b.title = ?1")
	public Collection<FixUpTask> getFixUpTasksByWarranty(String warranty);

	@Query("select a from FixUpTask a where (a.maxPrice) >= ?1 order by a.maxPrice")
	public Collection<FixUpTask> getFixUpTasksByMinPrice(double minPrice);

	@Query("select a from FixUpTask a where (a.maxPrice) <= ?1 order by a.maxPrice")
	public Collection<FixUpTask> getFixUpTasksByMaxPrice(double maxPrice);

}
