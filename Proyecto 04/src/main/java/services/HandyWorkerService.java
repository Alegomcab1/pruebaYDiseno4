
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.HandyWorkerRepository;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Endorser;
import domain.Finder;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Tutorial;

@Service
@Transactional
public class HandyWorkerService {

	// Managed repository ------------------------------------------

	@Autowired
	private HandyWorkerRepository	handyWorkerRepository;

	// Supporting Services ------------------------------------------

	@Autowired
	private TutorialService			tutorialService;
	@Autowired
	private FinderService			finderService;
	@Autowired
	private EndorserService			endorserService;
	@Autowired
	private CurriculumService		curriculumService;
	@Autowired
	private ApplicationService		applicationService;


	// Simple CRUD methods ------------------------------------------

	public HandyWorker create(final Endorser a, final String make) {
		final HandyWorker result = new HandyWorker();
		final List<Application> applications = new ArrayList<Application>();
		final List<Finder> finders = new ArrayList<Finder>();
		final List<Tutorial> tutorials = new ArrayList<Tutorial>();

		result.setAddress(a.getAddress());
		result.setApplications(applications);
		result.setBoxes(a.getBoxes());
		result.setCurriculum(null);
		result.setEmail(a.getEmail());
		result.setEndorsments(a.getEndorsments());
		result.setFinders(finders);
		result.setMake(make);
		result.setMiddleName(a.getMiddleName());
		result.setName(a.getName());
		result.setPhoneNumber(a.getPhoneNumber());
		result.setPhoto(a.getPhoto());
		result.setScore(a.getScore());
		result.setSocialProfiles(a.getSocialProfiles());
		result.setSurname(a.getSurname());
		result.setTutorials(tutorials);
		result.setVersion(a.getVersion());

		return result;

	}

	public List<Application> showApplications() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		List<Application> listApplications = new ArrayList<Application>();

		HandyWorker logguedHandyWorker = new HandyWorker();

		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerByUserName(userAccount.getUsername());
		listApplications = this.handyWorkerRepository.getAllApplicationsFromAHandyWorker(logguedHandyWorker);

		return listApplications;
	}

	public Application createApplicationHandyWorker(final Integer offeredPrice, final List<String> comentarios, final FixUpTask fixUpTask) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		HandyWorker logguedHandyWorker = new HandyWorker();
		logguedHandyWorker = this.handyWorkerRepository.getHandyWorkerByUserName(userAccount.getUsername());

		final Application application = ApplicationService.createApplication(offeredPrice, comentarios, fixUpTask, logguedHandyWorker);

		return application;
	}

	public HandyWorker saveHandyWorker(final HandyWorker handyWorker) {
		return this.handyWorkerRepository.save(handyWorker);
	}
}
