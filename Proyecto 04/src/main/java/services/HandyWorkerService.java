
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.HandyWorkerRepository;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Customer;
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


	// Simple CRUD methods ------------------------------------------

	public HandyWorker createHandyWorker(final Endorser a, final String make) {
		final HandyWorker result = new HandyWorker();
		//final Actor b = createActor();
		//final Endorser a = createEndorser();
		//TODO Eliminar los parametros de entrada? Llamar solo al create de Endorser? Realizar el make dentro del create?
		final List<Application> applications = new ArrayList<Application>();
		final List<Finder> finders = new ArrayList<Finder>();
		final List<Tutorial> tutorials = new ArrayList<Tutorial>();
		//TODO Toda lista vacia podria pasarse como parametro de entrada en vez de forzosamente inicializarlo vacio?

		result.setAddress(a.getAddress());
		result.setApplications(applications);
		result.setBoxes(a.getBoxes());
		//TODO setCurriculum no veo que se deje a null, averiguar si hay que usar createCurriculum, pasarlo como parametro de entrada o dejarlo asi

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

	public List<FixUpTask> listFixUpTask() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		List<FixUpTask> res = new ArrayList<FixUpTask>();
		res = (List<FixUpTask>) this.handyWorkerRepository.getFixUpTasks();

		return res;

	}

	public Customer getCustomerProfile(FixUpTask f) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		Customer res = this.handyWorkerRepository.getCustomerFromFixUpTask(f);

		return res;
	}
}
