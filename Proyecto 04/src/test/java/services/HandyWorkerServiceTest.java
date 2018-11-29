
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
import domain.Complaint;
import domain.Customer;
import domain.Endorser;
import domain.Endorsment;
import domain.Finder;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Note;
import domain.Phase;
import domain.Report;
import domain.Tutorial;

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
	@Autowired
	private NoteService			noteServce;
	@Autowired
	private ReportService		reportService;
	@Autowired
	private ComplaintService	complaintService;
	@Autowired
	private TutorialService		tutoralService;
	@Autowired
	private EndorsmentService	endorsmentService;
	@Autowired
	private EndorserService		endorserService;


	//11.1
	@Test
	public void testShowFixUpTask() {
		Actor h = new Actor();
		h = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		Collection<FixUpTask> fResults = this.handyWorkerService.showFixUpTasks();
		Collection<FixUpTask> f = this.fixUpTaskService.findAll();

		Assert.isTrue(f.equals(fResults));
		super.unauthenticate();
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
		super.unauthenticate();
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
		super.unauthenticate();
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
		super.unauthenticate();
	}

	@Test
	public void testCreateApplicationHandyWorker() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		HandyWorker h = this.handyWorkerService.findOne(actor.getId());

		List<String> comments = new ArrayList<String>();
		FixUpTask fixUpTask = this.fixUpTaskService.findOne(1382);
		Application application = this.handyWorkerService.createApplicationHandyWorker(4.0, comments, fixUpTask);

		Assert.isTrue(fixUpTask.getApplications().contains(application) && h.getApplications().contains(application));
		super.unauthenticate();
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
		super.unauthenticate();
	}

	@Test
	public void testCreatePhasesForApplicaion() {
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
		super.unauthenticate();
	}

	@Test
	public void testDeletePhaseForApplication() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		FixUpTask fixUpTaskBeforeDelePhase = this.fixUpTaskService.findOne(1382);

		List<Phase> phases = (List<Phase>) fixUpTaskBeforeDelePhase.getPhases();
		Phase phase = phases.get(0);

		this.handyWorkerService.deletePhaseForApplication(phase.getId());

		FixUpTask fixUpTaskAfterDelePhase = this.fixUpTaskService.findOne(1382);

		Assert.isTrue(!(fixUpTaskAfterDelePhase.getPhases().contains(phase)));
		super.unauthenticate();
	}

	@Test
	public void testUpdatePhaseForHandyWorker() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		FixUpTask fixUpTask = this.fixUpTaskService.findOne(1382);

		List<Phase> phases = (List<Phase>) fixUpTask.getPhases();
		Phase phaseBeforeUpdate = phases.get(0);

		Phase phaseAfterUpdate = phaseBeforeUpdate;

		phaseAfterUpdate.setTitle("Cambio");
		this.handyWorkerService.updatePhaseForHandyWorker(phaseAfterUpdate);

		List<Phase> newPhases = (List<Phase>) fixUpTask.getPhases();
		Assert.isTrue(newPhases.get(0).getTitle().equals(phaseAfterUpdate.getTitle()));
		super.unauthenticate();
	}

	//37.1

	@Test
	public void testShowFinderFromHandyWorker() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		HandyWorker h = this.handyWorkerService.findOne(actor.getId());
		Finder finder = h.getFinder();
		finder.setKeyWord("Cambio");

		this.handyWorkerService.updateFinderFromHandyWorker(finder);

		Assert.isTrue(h.getFinder().getKeyWord().equals(finder.getKeyWord()));
		super.unauthenticate();
	}

	//37.2

	@Test
	public void testShowFinderResult() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		HandyWorker h = this.handyWorkerService.findOne(actor.getId());

		List<FixUpTask> f = this.handyWorkerService.showFinderResult();

		Assert.isTrue(f.size() == h.getFinder().getFixUpTasks().size());
		super.unauthenticate();
	}

	//37.3

	@Test
	public void testShowComplaintsFromHandyWorker() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		HandyWorker h = this.handyWorkerService.findOne(actor.getId());

		List<Complaint> complaints = this.handyWorkerService.showComplaintsFromHandyWorker();

		Assert.isTrue(complaints.size() == 6);
		super.unauthenticate();

	}

	//37.4
	@Test
	public void testCreateNoteFromHandyWorker() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		List<String> optionalComments = new ArrayList<String>();
		Note newNote = this.noteServce.create("Prueba", optionalComments);

		Complaint complaint = this.complaintService.findOne(1383);

		Report report = complaint.getReports().get(0);
		Integer numNotesBeforeCreate = report.getNotes().size();
		this.handyWorkerService.createNoteFromHandyWorker(complaint.getId(), newNote, report.getId());

		List<Note> notesFromReport = this.reportService.findOne(report.getId()).getNotes();
		Integer numNotesAfterCreate = notesFromReport.size();
		Assert.isTrue(numNotesBeforeCreate + 1 == numNotesAfterCreate);
		super.unauthenticate();

	}

	//37.5
	@Test
	public void testWriteCommentFromHandyWorker() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		Complaint complaint = this.complaintService.findOne(1383);
		Report report = complaint.getReports().get(0);
		Note note = report.getNotes().get(0);

		Integer numCommentsBefore = note.getOptionalComments().size();

		this.handyWorkerService.writeCommentFromHandyWorker(complaint.getId(), "Prueba", report.getId(), note.getId());

		Integer numCommentsAfter = this.noteServce.findOne(note.getId()).getOptionalComments().size();

		Assert.isTrue(numCommentsBefore + 1 == numCommentsAfter);

		super.unauthenticate();
	}

	//49.1
	@Test
	public void testShowTutorials() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		HandyWorker h = this.handyWorkerService.findOne(actor.getId());

		List<Tutorial> tutorials = h.getTutorials();
		List<Tutorial> result = this.handyWorkerService.showTutorials();

		Assert.isTrue(tutorials.containsAll(result));
		super.unauthenticate();

	}

	@Test
	public void TestDeleteTutorial() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		HandyWorker h = this.handyWorkerService.findOne(actor.getId());

		Tutorial tutorial = h.getTutorials().get(0);

		this.handyWorkerService.deleteTutorial(tutorial);

		HandyWorker h2 = this.handyWorkerService.findOne(actor.getId());

		Assert.isTrue(!(h2.getTutorials().contains(tutorial)));
		super.unauthenticate();

	}

	@Test
	public void TestUpdateTutorial() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		HandyWorker h = this.handyWorkerService.findOne(actor.getId());

		Tutorial tutorial = h.getTutorials().get(0);
		String oldTitle = tutorial.getTitle();
		tutorial.setTitle("Prueba2");

		this.handyWorkerService.updateTutorial(tutorial);

		Tutorial newTutorial = this.tutoralService.findOne(tutorial.getId());

		String newTitle = newTutorial.getTitle();

		Assert.isTrue(!(oldTitle.equals(newTitle)));
		super.unauthenticate();
	}

	@Test
	public void testCreateTutorial() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		HandyWorker h = this.handyWorkerService.findOne(actor.getId());

		Integer numTutorialsBefore = h.getTutorials().size();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 12);
		Date lastUpdate = cal.getTime();

		Tutorial newTutorial = this.tutoralService.create("Prueba", lastUpdate, "Summary");

		this.handyWorkerService.createTutorial(newTutorial);

		Integer numTutorialsAfter = h.getTutorials().size();
		Assert.isTrue(numTutorialsBefore + 1 == numTutorialsAfter);
		super.unauthenticate();

	}
	//49.2

	@Test
	public void testDeleteEndorsment() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		HandyWorker h = this.handyWorkerService.findOne(actor.getId());
		Endorsment endorsment = h.getEndorsments().get(0);

		this.handyWorkerService.deleteEndorsment(endorsment);

		Assert.isTrue(!(h.getEndorsments().contains(endorsment)));
		super.unauthenticate();

	}

	@Test
	public void testUpdateEndorsment() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		HandyWorker h = this.handyWorkerService.findOne(actor.getId());
		Endorsment endorsment = h.getEndorsments().get(0);
		List<String> oldComments = (List<String>) endorsment.getComments();
		List<String> newComments = new ArrayList<>();
		newComments.add("Ejemplo");
		endorsment.setComments(newComments);

		this.handyWorkerService.updateEndorsment(endorsment);

		Endorsment endorsmentN = this.endorsmentService.findOne(endorsment.getId());
		Assert.isTrue(endorsmentN.getComments().contains("Ejemplo") && !(newComments.containsAll(oldComments)));

		super.unauthenticate();

	}

	@Test
	//TODO
	public void testCreateEndorsment() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		List<String> comments = new ArrayList<>();
		comments.add("A");
		comments.add("B");

		Endorser endorser = this.customerService.findOne(1490);
		System.out.println(endorser);
		//Peta el createEndorsment
		Endorsment endorsment = this.endorsmentService.createEndorsment(comments, endorser);

		System.out.println("p");
		this.handyWorkerService.createEndorsment(endorsment);

		Endorser endorser2 = this.customerService.findOne(1490);

		Assert.isTrue(endorser2.getEndorsments().contains(endorser));
	}

	@Test
	public void testShowEndorsments() {
		Actor actor = new Actor();
		actor = this.actorService.getActorByUsername("PepeHW");
		super.authenticate("PepeHW");

		HandyWorker h = this.handyWorkerService.findOne(actor.getId());

		System.out.println(h.getId());
		List<Endorsment> endorsments = h.getEndorsments();

		List<Endorsment> result = this.handyWorkerService.showEndorsments();

		System.out.println(result);
		System.out.println(endorsments);
		Assert.isTrue(endorsments.equals(result));

	}
}
