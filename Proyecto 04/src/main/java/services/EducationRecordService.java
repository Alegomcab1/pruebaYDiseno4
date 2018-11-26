
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.EducationRecordRepository;
import security.LoginService;
import security.UserAccount;
import domain.EducationRecord;

@Service
@Transactional
public class EducationRecordService {

	// Manged Repository

	@Autowired
	private EducationRecordRepository	educationRecordRepository;


	// Simple CRUD methods

	public EducationRecord create(EducationRecord educationRecord) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		this.educationRecordRepository.save(educationRecord);

		return educationRecord;

	}

	public Collection<EducationRecord> findAll() {
		Collection<EducationRecord> result;

		result = this.educationRecordRepository.findAll();

		return result;
	}
	public EducationRecord findOne(Integer id) {
		EducationRecord result = this.educationRecordRepository.findOne(id);
		return result;
	}

	public void save(EducationRecord educationRecord) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		this.educationRecordRepository.save(educationRecord);
	}

	public void delete(EducationRecord educationRecord) {
		this.educationRecordRepository.delete(educationRecord);
	}
}
