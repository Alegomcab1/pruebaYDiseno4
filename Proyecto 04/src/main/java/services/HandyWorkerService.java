
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.HandyWorkerRepository;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Complaint;
import domain.Endorser;
import domain.Endorsment;
import domain.Finder;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Note;
import domain.Phase;
import domain.Status;
import domain.Tutorial;

@Service
@Transactional
public class HandyWorkerService {

	// Managed repository ---------------------------------------------------------------------------------------------

	@Autowired
	private HandyWorkerRepository	handyWorkerRepository;

	// Supporting Services --------------------------------------------------------------------------------------------

	//TODO Consultar quien tiene este Autowired
	@Autowired
	private TutorialService			tutorialService;
	@Autowired
	private FinderService			finderService;
	@Autowired
	private EndorserService			endorserService;
	//TODO Consultar quien tiene este Autowired
	@Autowired
	private CurriculumService		curriculumService;
	@Autowired
	private ApplicationService		applicationService;
	@Autowired
	private EndorsmentService		endorsmentService;


	// Simple CRUD methods --------------------------------------------------------------------------------------------

	public HandyWorker createHandyWorker() {

		HandyWorker result = new HandyWorker();
		List<Application> applications = new ArrayList<Application>();
		List<Tutorial> tutorials = new ArrayList<Tutorial>();
		Endorser a = EndorserService.createEndorser();

		result.setAddress(a.getAddress());
		result.setApplications(applications);
		result.setBoxes(a.getBoxes());
		result.setCurriculum(null);
		result.setEmail(a.getEmail());
		result.setEndorsments(a.getEndorsments());
		result.setFinder(null);
		result.setMake("make");
		result.setMiddleName(a.getMiddleName());
		result.setName(a.getName());
		result.setPhoneNumber(a.getPhoneNumber());
		result.setPhoto(a.getPhoto());
		result.setScore(a.getScore());
		result.setSocialProfiles(a.getSocialProfiles());
		result.setSurname(a.getSurname());
		result.setTutorials(tutorials);

		return result;

	}

	public Collection<HandyWorker> findAll() {
		//TODO Es necesario un Assert por si esto solo lo puede hacer un Admin?
		return this.handyWorkerRepository.findAll();
	}

	public HandyWorker findOne(int id) {
		//TODO Es necesario un Assert por si esto solo lo puede hacer un Admin?
		return this.handyWorkerRepository.findOne(id);
	}

	public HandyWorker save(HandyWorker handyWorker) {
		return this.handyWorkerRepository.save(handyWorker);
	}

	public void delete(HandyWorker handyWorker) {
		//TODO Bastante seguro de que esto solo lo deberia de poder hacer un ADMIN, además mirar si hay restricciones a la hora de eliminarlo
		this.handyWorkerRepository.delete(handyWorker);
	}

	// Other business methods -------------------------------------------------------------------------------------------

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
		//TODO Comprobar que el Assert es correcto
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

		Application application = ApplicationService.createApplication();

		ApplicationService.updateApplication(application.getId(), comments, fixUpTask, logguedHandyWorker, offeredPrice, application.getStatus());
		Assert.isTrue(ApplicationService.findOne(application.getId()).getHandyWorker().equals(logguedHandyWorker));

		return application;
	}
	public Phase createPhaseForApplication(Application a) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		//TODO Comprobar que el Assert es correcto
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		Assert.isTrue(a.getStatus().equals(Status.ACCEPTED));
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());
		Assert.isTrue(a.getHandyWorker().equals(logguedHandyWorker));

		Application application = ApplicationService.findOne(a.getId());
		Phase phase = PhaseService.createPhase();
		Collection<Phase> newPhases = this.handyWorkerRepository.getPhasesByApplication(application.getId());
		newPhases.add(phase);

		FixUpTask newFixUpTask = application.getFixUpTask();
		Assert.notNull(newFixUpTask);
		newFixUpTask.setPhases(newPhases);

		FixUpTaskService.save(newFixUpTask);

		return phase;
	}

	public void updateFinderFromHandyWorker(Finder finder) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		//TODO Comprobar que el Assert es correcto
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		Finder newFinder = new Finder();
		newFinder = this.handyWorkerRepository.getFinderFromAHandyWorker(userAccount.getId());
		newFinder.setCategory(finder.getCategory());
		newFinder.setEndDate(finder.getEndDate());
		newFinder.setKeyWord(finder.getKeyWord());
		newFinder.setMaxPrice(finder.getMaxPrice());
		newFinder.setMinPrice(finder.getMinPrice());
		newFinder.setStartDate(finder.getStartDate());
		newFinder.setWarranty(finder.getWarranty());

		FinderService.save(newFinder);
	}

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

	public List<Complaint> showComplaintsFromHandyWorker() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		List<Complaint> complaints = new ArrayList<Complaint>();

		complaints = this.handyWorkerRepository.getComplaintsFromHandyWorker(userAccount.getId());

		return complaints;
	}

	public void createNoteFromHandyWorker(int idComplaint, Note note) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

		Assert.isTrue(!note.getMandatoryComment().equals(""));
		Assert.notNull(note.getMandatoryComment());
		Assert.notNull(note.getMoment());

		NoteService.save(note);
	}

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

	public void createEndorsment(Endorsment newEndorsment) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		Endorser logguedEndorser = new Endorser();
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

		this.endorsmentService.save(newEndorsment);

	}

	public List<Endorsment> showEndorsments() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerById(userAccount.getId());

		List<Endorsment> endorsments = new ArrayList<Endorsment>();

		endorsments = this.handyWorkerRepository.getEndorsmentsByEndorser(userAccount.getId());

		return endorsments;
	}

	public List<HandyWorker> findAll() {
		return this.handyWorkerRepository.findAll();
	}
}
