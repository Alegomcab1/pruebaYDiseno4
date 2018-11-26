
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.HandyWorkerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Complaint;
import domain.CreditCard;
import domain.Customer;
import domain.Endorsment;
import domain.Finder;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Note;
import domain.Phase;
import domain.Report;
import domain.Status;
import domain.Tutorial;

@Service
@Transactional
public class HandyWorkerService {

	// Managed repository ---------------------------------------------------------------------------------------------

	@Autowired
	private HandyWorkerRepository	handyWorkerRepository;

	// Supporting Services --------------------------------------------------------------------------------------------

	@Autowired
	private TutorialService			tutorialService;
	@Autowired
	private EndorsmentService		endorsmentService;
	@Autowired
	private CurriculumService		curriculumService;
	@Autowired
	private NoteService				noteService;
	@Autowired
	private ReportService			reportService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private PhaseService			phaseService;
	@Autowired
	private ApplicationService		applicationService;
	@Autowired
	private FinderService			finderService;
	@Autowired
	private FixUpTaskService		fixUpTaskService;
	@Autowired
	private ComplaintService		complaintService;
	@Autowired
	private EndorserService			endorserSevice;


	// Simple CRUD methods --------------------------------------------------------------------------------------------

	public HandyWorker createHandyWorker(String name, String middleName, String surname, String photo, String email, String phoneNumber, String address, String userName, String password, Integer score) {

		HandyWorker handyWorker = new HandyWorker();
		handyWorker = (HandyWorker) this.endorserSevice.createEndorser(name, middleName, surname, photo, email, phoneNumber, address, userName, password, score);

		handyWorker.setMake(handyWorker.getName() + "" + handyWorker.getMiddleName() + "" + handyWorker.getSurname());

		List<FixUpTask> f = new ArrayList<FixUpTask>();

		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime());
		Date afterMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() + 1);
		Finder finder = this.finderService.createFinder("", "", "", 0.0, 0.0, thisMoment, afterMoment, f);
		handyWorker.setFinder(finder);

		List<Authority> authorities = new ArrayList<Authority>();
		handyWorker.getUserAccount().setAuthorities(authorities);

		UserAccount userAccountAdmin = new UserAccount();
		Authority authority = new Authority();
		authority.setAuthority(Authority.HANDYWORKER);
		authorities.add(authority);

		return handyWorker;

	}

	public void editHandyWorker(HandyWorker handyworker) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getId() == handyworker.getId());

		this.handyWorkerRepository.save(handyworker);

	}
	public Collection<HandyWorker> findAll() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		return this.handyWorkerRepository.findAll();
	}

	public HandyWorker findOne(int id) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		return this.handyWorkerRepository.findOne(id);
	}

	public HandyWorker save(HandyWorker handyWorker) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		Assert.isTrue(handyWorker.getId() == 0 || userAccount.equals(handyWorker.getUserAccount()));
		return this.handyWorkerRepository.save(handyWorker);
	}

	public void delete(HandyWorker handyWorker) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		Assert.isTrue(handyWorker.getId() == 0 || userAccount.equals(handyWorker.getUserAccount()));
		this.curriculumService.delete(handyWorker.getCurriculum());
		this.handyWorkerRepository.delete(handyWorker);
	}
	private HandyWorker securityAndHandy() {
		UserAccount userAccount = LoginService.getPrincipal();
		String username = userAccount.getUsername();
		HandyWorker loggedHandy = this.handyWorkerRepository.getHandyByUsername(username);

		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		return loggedHandy;
	}

	public Report showReport(Report report) {
		HandyWorker loggedHandy = this.securityAndHandy();
		Assert.isTrue(report.getFinalMode());
		return this.reportService.findOne(report.getId());
	}

	public List<Report> listReports() {
		HandyWorker loggedHandy = this.securityAndHandy();
		List<Report> lr = this.reportService.findAll();
		for (Report report : lr)
			Assert.isTrue(report.getFinalMode());
		return lr;
	}

	// Other business methods -------------------------------------------------------------------------------------------

	//11.1 ------------------------------------------------------------------------------------------------------------------

	public Collection<FixUpTask> showFixUpTasks() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		return this.fixUpTaskService.findAll();
	}

	public Map<Customer, Collection<FixUpTask>> getFixUpTaskPerCustomer(int idFixUpTask) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		Map<Customer, Collection<FixUpTask>> res = new HashMap<Customer, Collection<FixUpTask>>();

		Customer customer = this.handyWorkerRepository.getCustomerByFixUpTask(idFixUpTask);

		res.put(customer, customer.getFixUpTasks());

		return res;

	}
	//11.2 ------------------------------------------------------------------------------------------------------------------

	//TODO REVISAR
	public Collection<FixUpTask> getFilterFixUpTasks() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.findOne(userAccount.getId());

		Finder finder = logguedHandyWorker.getFinder();
		Assert.isTrue(logguedHandyWorker.getFinder().getId() == finder.getId());

		List<FixUpTask> result = new ArrayList<FixUpTask>();
		result = this.fixUpTaskService.findAll();

		Collection<FixUpTask> filter = new ArrayList<FixUpTask>();
		filter = this.handyWorkerRepository.getFixUpTaskByKeyWord(finder.getKeyWord());
		result.retainAll(filter);

		if (finder.getCategory() != null) {
			filter = this.handyWorkerRepository.getFixUpTaskByCategory(finder.getCategory());
			result.retainAll(filter);
		}
		if (finder.getWarranty() != null) {
			this.handyWorkerRepository.getFixUpTasksByWarranty(finder.getWarranty());
			result.retainAll(filter);
		}

		filter = this.handyWorkerRepository.getFixUpTasksByMinPrice(finder.getMinPrice());
		filter.retainAll(this.handyWorkerRepository.getFixUpTasksByMaxPrice(finder.getMaxPrice()));
		result.retainAll(filter);

		filter.clear();
		for (FixUpTask f : result)
			if ((finder.getStartDate().before(f.getRealizationTime()) || finder.getStartDate().equals(f.getRealizationTime())) && (finder.getEndDate().after(f.getRealizationTime()) || finder.getEndDate().equals(f.getRealizationTime())))
				filter.add(f);

		result.retainAll(filter);
		Finder finderResult = new Finder();
		finderResult = finder;
		finderResult.setFixUpTasks(result);
		this.finderService.save(finderResult);

		return result;
	}
	//11.3 ------------------------------------------------------------------------------------------------------------------

	public List<Application> showApplications() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		List<Application> listApplications = new ArrayList<Application>();

		listApplications = this.handyWorkerRepository.getAllApplicationsFromAHandyWorker(userAccount.getId());
		return listApplications;
	}

	public Application createApplicationHandyWorker(Integer offeredPrice, List<String> comments, FixUpTask fixUpTask, CreditCard creditCard) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.findOne(userAccount.getId());

		Application application = this.applicationService.createApplication(offeredPrice, fixUpTask, logguedHandyWorker, creditCard);

		//Revisar
		application = this.applicationService.updateApplication(application.getId(), comments, fixUpTask, logguedHandyWorker, offeredPrice, application.getStatus());

		return application;
	}

	//11.4 ------------------------------------------------------------------------------------------------------------------
	public Collection<Phase> showPhaseForHandyWorker(FixUpTask fixUpTask) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		Assert.isTrue(this.handyWorkerRepository.getFixUpTasksFromHandyWorker(userAccount.getId()).contains(fixUpTask));

		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.findOne(userAccount.getId());

		Collection<Phase> phases = this.handyWorkerRepository.getPhasesByFixUpTask(fixUpTask.getId());

		return phases;
	}

	public void createPhaseForApplication(Application app, Phase newPhase) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.findOne(userAccount.getId());

		Application application = this.applicationService.findOne(app.getId());
		Assert.isTrue(application.getHandyWorker().equals(logguedHandyWorker));
		Assert.isTrue(application.getStatus().equals(Status.ACCEPTED));

		Collection<Phase> newPhases = this.handyWorkerRepository.getPhasesByApplication(application.getId());
		newPhases.add(newPhase);

		FixUpTask newFixUpTask = application.getFixUpTask();
		Assert.notNull(newFixUpTask);
		newFixUpTask.setPhases(newPhases);

		this.fixUpTaskService.save(newFixUpTask);

	}

	public void deletePhaseForApplication(int idPhase) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.findOne(userAccount.getId());

		Phase phase = new Phase();
		phase = this.phaseService.findOne(idPhase);
		Collection<Phase> phases = this.handyWorkerRepository.getPhasesByHandyWorker(userAccount.getId());
		Assert.isTrue(phases.contains(phase));

		this.phaseService.delete(phase);
	}

	public void updatePhaseForHandyWorker(Phase phase) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.findOne(userAccount.getId());

		Collection<Phase> phases = this.handyWorkerRepository.getPhasesByHandyWorker(userAccount.getId());
		Assert.isTrue(phases.contains(phase));

		this.phaseService.save(phase);
	}
	//REQUISITO FUNCIONAL 37 *******************************************************************************************************************

	//37.1 --------------------------------------------------------------------------------------------------------------------------------------

	public void updateFinderFromHandyWorker(Finder finder) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.findOne(userAccount.getId());

		Assert.isTrue(logguedHandyWorker.getFinder().getId() == finder.getId());

		this.finderService.save(finder);
	}

	//37.2 --------------------------------------------------------------------------------------------------------------------------------------
	public List<FixUpTask> showFinderResult() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		List<FixUpTask> resultFixUpTask = new ArrayList<FixUpTask>();

		Finder finder = new Finder();

		finder = this.handyWorkerRepository.getFinderFromAHandyWorker(userAccount.getId());
		resultFixUpTask = this.handyWorkerRepository.getResultFinder(finder.getId());
		return resultFixUpTask;
	}

	//37.3 --------------------------------------------------------------------------------------------------------------------------------------
	public List<Complaint> showComplaintsFromHandyWorker() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		List<Complaint> complaints = new ArrayList<Complaint>();

		complaints = this.handyWorkerRepository.getComplaintsFromHandyWorker(userAccount.getId());

		return complaints;
	}

	//37.4 --------------------------------------------------------------------------------------------------------------------------------------
	public void createNoteFromHandyWorker(int idComplaint, Note note, int idReport) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.findOne(userAccount.getId());

		Assert.isTrue(!note.getMandatoryComment().equals(""));
		Assert.notNull(note.getMandatoryComment());
		Assert.notNull(note.getMoment());

		List<Complaint> complaints = new ArrayList<Complaint>();
		complaints = this.handyWorkerRepository.getComplaintsFromHandyWorker(userAccount.getId());
		Complaint complaint = new Complaint();
		complaint = this.complaintService.findOne(idComplaint);

		Assert.isTrue(complaints.contains(complaint));

		List<Report> reports = new ArrayList<Report>();
		reports = complaint.getReports();
		Report report = new Report();
		report = this.reportService.findOne(idReport);

		Assert.isTrue(reports.contains(report));

		//	this.noteService.save(note);

		List<Note> notes = new ArrayList<Note>();
		notes = report.getNotes();
		notes.add(note);
		report.setNotes(notes);

		this.reportService.save(report);

	}
	//37.5 --------------------------------------------------------------------------------------------------------------------------------------
	//TODO REVISAR
	public void writeCommentFromHandyWorker(int idComplaint, String comment, int idReport, int idNote) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.findOne(userAccount.getId());

		List<Complaint> complaints = new ArrayList<Complaint>();
		complaints = this.handyWorkerRepository.getComplaintsFromHandyWorker(userAccount.getId());
		Complaint complaint = new Complaint();
		complaint = this.complaintService.findOne(idComplaint);

		Assert.isTrue(complaints.contains(complaint));

		List<Report> reports = new ArrayList<Report>();
		reports = complaint.getReports();
		Report report = new Report();
		report = this.reportService.findOne(idReport);

		Assert.isTrue(reports.contains(report));
		Note note = new Note();
		note = this.noteService.findOne(idNote);

		Assert.isTrue(report.getNotes().contains(note));
		note.getOptionalComments().add(comment);

		this.noteService.save(note);

	}
	//REQUISITO FUNCIONAL 49--------------------------------------------------------------------------------------------------------------------

	//49.1
	public List<Tutorial> showTutorials() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		List<Tutorial> tutorials = new ArrayList<Tutorial>();

		tutorials = this.handyWorkerRepository.getAllTutorialsFromAHandyWorker(userAccount.getId());

		return tutorials;
	}

	public void deleteTutorial(Tutorial tutorial) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.findOne(userAccount.getId());

		Assert.isTrue(logguedHandyWorker.getTutorials().contains(tutorial));

		this.tutorialService.delete(tutorial);
	}

	public void updateTutorial(Tutorial tutorial) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.findOne(userAccount.getId());

		Assert.isTrue(logguedHandyWorker.getTutorials().contains(tutorial));

		this.tutorialService.save(tutorial);
	}

	public void createTutorial(Tutorial newTutorial) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.findOne(userAccount.getId());
		Assert.isTrue(!logguedHandyWorker.getTutorials().contains(newTutorial));

		this.tutorialService.save(newTutorial);

	}

	//49.2
	public void deleteEndorsment(Endorsment endorsment) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.findOne(userAccount.getId());

		Assert.isTrue(logguedHandyWorker.getEndorsments().contains(endorsment));

		this.endorsmentService.delete(endorsment);
	}

	public void updateEndorsment(Endorsment endorsment) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.findOne(userAccount.getId());

		Assert.isTrue(logguedHandyWorker.getEndorsments().contains(endorsment));

		this.endorsmentService.save(endorsment);
	}

	//TODO COMPROBAR
	public void createEndorsment(Endorsment endorsment) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		Assert.isTrue(endorsment.getWrittenBy().getUserAccount().equals(userAccount));

		Assert.isTrue(endorsment.getWrittenTo().getUserAccount().getAuthorities().contains("CUSTOMER"));
		List<Integer> ids = new ArrayList<Integer>();
		ids = this.handyWorkerRepository.getCustomersFromHandyWorker(userAccount.getId());

		Assert.isTrue(ids.contains(endorsment.getWrittenTo().getUserAccount().getId()));
		this.endorsmentService.save(endorsment);

	}

	public List<Endorsment> showEndorsments() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		List<Endorsment> endorsments = new ArrayList<Endorsment>();

		endorsments = this.handyWorkerRepository.getEndorsmentsByEndorser(userAccount.getId());

		return endorsments;
	}
}
