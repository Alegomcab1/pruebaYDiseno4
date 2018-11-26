
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CurriculumRepository;
import security.LoginService;
import security.UserAccount;
import domain.Curriculum;

@Service
@Transactional
public class CurriculumService {

	// Manged Repository

	@Autowired
	private CurriculumRepository	curriculumRepository;


	// Simple CRUD methods

	public Curriculum create(Curriculum curriculum) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		this.curriculumRepository.save(curriculum);

		return curriculum;

	}

	public Collection<Curriculum> findAll() {
		Collection<Curriculum> result;

		result = this.curriculumRepository.findAll();

		return result;
	}

	public Curriculum findOne(Integer id) {

		Curriculum result = this.curriculumRepository.findOne(id);
		return result;

	}

	public void save(Curriculum curriculum) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		this.curriculumRepository.save(curriculum);

	}

	public void delete(Curriculum curriculum) {
		this.curriculumRepository.delete(curriculum);

	}


	// Supporting service

	@Autowired
	private EndorserRecordService		endorserRecordService;

	@Autowired
	private MiscellaneousRecordService	miscellandeousRecordService;

	@Autowired
	private EducationRecordService		educationalRecordService;

	@Autowired
	private ProfessionalRecordService	profesionalRecordService;

	@Autowired
	private PersonalRecordService		personalRecordService;

}
