
package services;

import java.util.ArrayList;
import java.util.Collection;
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
import domain.Customer;
import domain.Endorser;
import domain.Endorsment;
import domain.Finder;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Note;
import domain.Phase;
import domain.Report;
import domain.SocialProfile;
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


	// Simple CRUD methods --------------------------------------------------------------------------------------------

	public HandyWorker createHandyWorker(Endorser endorser) {

		HandyWorker handyWorker = new HandyWorker();
		handyWorker = (HandyWorker) this.actorService.createActor(endorser.getName(), endorser.getMiddleName(), endorser.getSurname(), endorser.getPhoto(), endorser.getEmail(), endorser.getPhoneNumber(), endorser.getAddress(), endorser.getUserAccount()
			.getUsername(), endorser.getUserAccount().getPassword());

		handyWorker.setScore(endorser.getScore());
		handyWorker.setMake(endorser.getName() + "" + endorser.getMiddleName() + "" + endorser.getSurname());

		List<Authority> authorities = new ArrayList<Authority>();
		handyWorker.getUserAccount().setAuthorities(authorities);

		UserAccount userAccountAdmin = new UserAccount();
		Authority authority = new Authority();
		authority.setAuthority(Authority.HANDYWORKER);
		authorities.add(authority);

		return handyWorker;

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

	// Other business methods -------------------------------------------------------------------------------------------

	//11.1 ------------------------------------------------------------------------------------------------------------------

	public Collection<FixUpTask> showFixUpTasks() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		return this.fixUpTaskService.findAll();
	}

	//TODO REVISAR

	public Map<List<String>, Collection<FixUpTask>> getFixUpTaskPerCustomer(int idFixUpTask) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		Map<List<String>, Collection<FixUpTask>> res = new HashMap<List<String>, Collection<FixUpTask>>();

		Customer customer = this.handyWorkerRepository.getCustomerByFixUpTask(idFixUpTask);

		List<SocialProfile> socialProfiles = new ArrayList<SocialProfile>();
		socialProfiles = customer.getSocialProfiles();

		List<String> socialData = new ArrayList<String>();

		for (SocialProfile s : socialProfiles)
			socialData.add("Social Network nickname: " + s.getNick() + "\nSocial NetWork link: " + s.getProfileLink() + "\n");

		List<String> personalData = new ArrayList<String>();
		personalData.add("Full name of the customer: " + customer.getSurname() + "" + customer.getName() + "" + customer.getMiddleName() + ".\n" + "Email: " + customer.getEmail() + ".\n" + "Social Profiles: \n" + socialData);

		res.put(personalData, customer.getFixUpTasks());

		return res;

	}
	//11.2 ------------------------------------------------------------------------------------------------------------------

	//TODO REVISAR
	public Collection<FixUpTask> getFilterFixUpTasks(Finder finder) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

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

	public Application createApplicationHandyWorker(Integer offeredPrice, List<String> comments, FixUpTask fixUpTask) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

		Application application = this.applicationService.createApplication();

		application = this.applicationService.updateApplication(application.getId(), comments, fixUpTask, logguedHandyWorker, offeredPrice, application.getStatus());

		return application;
	}

	//11.4 ------------------------------------------------------------------------------------------------------------------
	public Collection<Phase> showPhaseForHandyWorker(int idPhase) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

		Phase phase = new Phase();
		phase = this.handyWorkerRepository.getPhaseById(idPhase);
		Collection<Phase> phases = this.handyWorkerRepository.getPhasesByHandyWorker(userAccount.getId());
		Assert.isTrue(phases.contains(phase));

		return phases;
	}

	public void createPhaseForApplication(int applicationId, Phase newPhase) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

		Application application = this.handyWorkerRepository.getApplicationById(applicationId);
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
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

		Phase phase = new Phase();
		phase = this.handyWorkerRepository.getPhaseById(idPhase);
		Collection<Phase> phases = this.handyWorkerRepository.getPhasesByHandyWorker(userAccount.getId());
		Assert.isTrue(phases.contains(phase));

		this.phaseService.delete(phase);
	}

	public void updatePhaseForHandyWorker(Phase phase) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

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
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

		Assert.isTrue(logguedHandyWorker.getFinder().getId() == finder.getId());

		/*
		 * Finder newFinder = new Finder();
		 * newFinder = this.handyWorkerRepository.getFinderFromAHandyWorker(userAccount.getId());
		 * newFinder.setCategory(finder.getCategory());
		 * newFinder.setEndDate(finder.getEndDate());
		 * newFinder.setKeyWord(finder.getKeyWord());
		 * newFinder.setMaxPrice(finder.getMaxPrice());
		 * newFinder.setMinPrice(finder.getMinPrice());
		 * newFinder.setStartDate(finder.getStartDate());
		 * newFinder.setWarranty(finder.getWarranty());
		 */

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
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

		Assert.isTrue(!note.getMandatoryComment().equals(""));
		Assert.notNull(note.getMandatoryComment());
		Assert.notNull(note.getMoment());

		List<Complaint> complaints = new ArrayList<Complaint>();
		complaints = this.handyWorkerRepository.getComplaintsFromHandyWorker(userAccount.getId());
		Complaint complaint = new Complaint();
		complaint = this.handyWorkerRepository.getComplaintById(idComplaint);

		Assert.isTrue(complaints.contains(complaint));

		List<Report> reports = new ArrayList<Report>();
		reports = complaint.getReports();
		Report report = new Report();
		report = this.handyWorkerRepository.getReportById(idReport);

		Assert.isTrue(reports.contains(report));

		this.noteService.save(note);

		List<Notes> notes = new ArrayList<Notes>();
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
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

		List<Complaint> complaints = new ArrayList<Complaint>();
		complaints = this.handyWorkerRepository.getComplaintsFromHandyWorker(userAccount.getId());
		Complaint complaint = new Complaint();
		complaint = this.handyWorkerRepository.getComplaintById(idComplaint);

		Assert.isTrue(complaints.contains(complaint));

		List<Report> reports = new ArrayList<Report>();
		reports = complaint.getReports();
		Report report = new Report();
		report = this.handyWorkerRepository.getReportById(idReport);

		Assert.isTrue(reports.contains(report));
		Note note = new Note();
		note = this.noteService.finOne(idNote);

		Assert.isTrue(report.getNotes().contains(note));
		note.getOptionalComments().add(comment);

		this.noteService.save(note);

	}
	//REQUISITO FUNCIONAL 49--------------------------------------------------------------------------------------------------------------------

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
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

		Assert.isTrue(logguedHandyWorker.getTutorials().contains(tutorial));

		this.tutorialService.delete(tutorial);
	}

	public void updateTutorial(Tutorial tutorial) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

		Assert.isTrue(logguedHandyWorker.getTutorials().contains(tutorial));

		this.tutorialService.save(tutorial);
	}

	public void createTutorial(Tutorial newTutorial) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());
		Assert.isTrue(!logguedHandyWorker.getTutorials().contains(newTutorial));

		this.tutorialService.save(newTutorial);

	}

	public void deleteEndorsment(Endorsment endorsment) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

		Assert.isTrue(logguedHandyWorker.getEndorsments().contains(endorsment));

		this.endorsmentService.delete(endorsment);
	}

	public void updateEndorsment(Endorsment endorsment) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

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

		this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

		if (ids.contains(endorsment.getWrittenTo().getUserAccount().getId()))
			this.endorsmentService.save(endorsment);

	}

	public List<Endorsment> showEndorsments() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

		List<Endorsment> endorsments = new ArrayList<Endorsment>();

		endorsments = this.handyWorkerRepository.getEndorsmentsByEndorser(userAccount.getId());

		return endorsments;
	}
}
