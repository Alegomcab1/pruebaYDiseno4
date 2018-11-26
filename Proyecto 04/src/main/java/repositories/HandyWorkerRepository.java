
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;
import domain.Endorser;
import domain.Endorsment;
import domain.Finder;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Phase;
import domain.Tutorial;

@Repository
public interface HandyWorkerRepository extends JpaRepository<HandyWorker, Integer> {

	@Query("select a.applications from HandyWorker a where a.id = ?1 ")
	public List<Application> getAllApplicationsFromAHandyWorker(int id);

	@Query("select a from HandyWorker where a.id = ?1")
	public HandyWorker getHandyWorkerById(int id);

	@Query("select a.phases from FixUpTask a join a.application b where b.id = ?1")
	public Collection<Phase> getPhasesByApplication(int id);

	@Query("select a.finder from HandyWorker a where a.id = ?1 ")
	public Finder getFinderFromAHandyWorker(int id);

	@Query("select a.fixUpTasks from Finder a where a.id = ?1 ")
	public List<FixUpTask> getResultFinder(int id);

	@Query("select a.fixUpTask from Application a where a.id = ?1 ")
	public FixUpTask getFixUpTaskFromApplication(int id);

	@Query("select distinct a.fixUpTask from Application a join a.handyWorker b where b.id = ?1")
	public List<FixUpTask> getFixUpTasksFromHandyWorker(int id);

	/*
	 * @Query("select b.reports from Referee where b.id = (select a.referee from Complaint a where a.id = ?1).id ")
	 * public FixUpTask getReportFromComplaint(int id);
	 */

	@Query("select a.tutorials from HandyWorker a where a.id = ?1 ")
	public List<Tutorial> getAllTutorialsFromAHandyWorker(int id);

	@Query("select a from Endorser where a.id = ?1")
	public Endorser getEndorserById(int id);

	@Query("select a.endorsments from Endorser where a.id = ?1")
	public List<Endorsment> getEndorsmentsByEndorser(int id);

}
