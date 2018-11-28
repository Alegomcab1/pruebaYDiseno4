
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;
import domain.Application;
import domain.Customer;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Phase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class HandyWorkerServiceTest extends AbstractTest {

	//Service under test

	@Autowired
	private HandyWorkerService	handyWorkerService;
	@Autowired
	private CustomerService		customerService;
	@Autowired
	private ActorService		actorService;
	@Autowired
	private FixUpTaskService	fixUpTaskService;
	@Autowired
	private PhaseService		phaseService;


	//11.1
	@Test
	public void testShowFixUpTask() {
		Actor h = new Actor();
		h = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		Collection<FixUpTask> fResults = this.handyWorkerService.showFixUpTasks();
		Collection<FixUpTask> f = this.fixUpTaskService.findAll();

		Assert.isTrue(f.equals(fResults));
	}

	//11.2

	@Test
	public void testFilterFixUpTasksByFinder() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		HandyWorker h = this.handyWorkerService.findOne(actor.getId());

		List<FixUpTask> fixUpTasksBeforeFinder = h.getFinder().getFixUpTasks();

		this.handyWorkerService.filterFixUpTasksByFinder();

		List<FixUpTask> fixUpTasksBeforeAfter = h.getFinder().getFixUpTasks();

		Assert.isTrue(!(fixUpTasksBeforeFinder.equals(fixUpTasksBeforeAfter)));

	}
	@Test
	public void testgetFixUpTaskPerCustomer() {
		Actor h = new Actor();
		h = this.actorService.getActorByUsername("PepeHW");
		Customer customer = new Customer();
		super.authenticate("PepeHW");

		customer = this.customerService.getCustomerByUserName("PacoCustomer");

		Map<Customer, Collection<FixUpTask>> m = this.handyWorkerService.getFixUpTaskPerCustomer(customer.getFixUpTasks().get(0).getId());

		Assert.isTrue(m.keySet().contains(customer) && m.get(customer).size() == customer.getFixUpTasks().size());
	}

	//11.3
	@Test
	public void testShowApplication() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		HandyWorker h = this.handyWorkerService.findOne(actor.getId());

		Collection<Application> aResults = this.handyWorkerService.showApplications();
		Collection<Application> a = h.getApplications();

		Assert.isTrue(aResults.equals(a));
	}

	@Test
	public void testCreateApplicationHandyWorker() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		HandyWorker h = this.handyWorkerService.findOne(actor.getId());

		List<String> comments = new ArrayList<String>();
		FixUpTask fixUpTask = this.fixUpTaskService.findOne(1382);
		Application application = this.handyWorkerService.createApplicationHandyWorker(4, comments, fixUpTask);

		Assert.isTrue(fixUpTask.getApplications().contains(application) && h.getApplications().contains(application));

	}

	//11.4
	@Test
	public void testShowPhasesForHandyWorker() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		HandyWorker h = this.handyWorkerService.findOne(actor.getId());

		FixUpTask fixUpTask = this.fixUpTaskService.findOne(1382);
		Collection<Phase> phasesF = fixUpTask.getPhases();

		Collection<Phase> phasesResult = this.handyWorkerService.showPhaseForHandyWorker(fixUpTask);

		Assert.isTrue(phasesF.containsAll(phasesResult));

	}

	@Test
	public void testPhasesForApplicaion() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		HandyWorker h = this.handyWorkerService.findOne(actor.getId());

		FixUpTask fixUpTask = this.fixUpTaskService.findOne(1382);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2018);
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 12);
		Date startDate = cal.getTime();

		cal.set(Calendar.YEAR, 2019);
		cal.set(Calendar.MONTH, Calendar.NOVEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 13);
		Date endDate = cal.getTime();

		Application app = h.getApplications().get(0);

		Phase newPhase = this.phaseService.create("Phase1", "Description", startDate, endDate);

		this.handyWorkerService.createPhaseForApplication(app, newPhase);

		List<Phase> phasesNew = (List<Phase>) fixUpTask.getPhases();
		Phase phaseAdded = phasesNew.get(phasesNew.size() - 1);

		Assert.isTrue(fixUpTask.getPhases().contains(phaseAdded));

	}
}
