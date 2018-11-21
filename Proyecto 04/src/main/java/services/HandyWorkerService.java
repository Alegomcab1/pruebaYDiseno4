
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.HandyWorkerRepository;

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

	/*
	 * public HandyWorker create(final Endorser a, final String make) {
	 * final HandyWorker result;
	 * 
	 * result.setAddress(a.getAddress());
	 * result.setApplications(new ArrayList<Application>());
	 * result.setBoxes(a.getBoxes());
	 * result.setCurriculum(null);
	 * result.setEmail(a.getEmail());
	 * result.setEndorsments(a.getEndorsments());
	 * result.setFinders(new ArrayList<Finder>());
	 * result.setId(a.getId());
	 * result.setMake(make);
	 * result.setMiddleName(a.getMiddleName());
	 * result.setName(a.getName());
	 * result.setPhoneNumber(a.getPhoneNumber());
	 * result.setPhoto(a.getPhoto());
	 * result.setScore(a.getScore());
	 * result.setSocialProfiles(a.getSocialProfiles());
	 * result.setSurname(a.getSurname());
	 * result.setTutorials(new ArrayList<Tutorial>());
	 * result.setVersion(a.getVersion());
	 * 
	 * return result;
	 * 
	 * }
	 */

}
