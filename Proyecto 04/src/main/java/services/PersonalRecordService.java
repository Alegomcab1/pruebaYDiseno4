
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PersonalRecordRepository;
import security.LoginService;
import security.UserAccount;
import domain.PersonalRecord;

@Service
@Transactional
public class PersonalRecordService {

	// Manged Repository

	@Autowired
	private PersonalRecordRepository	personalRecordRepository;


	// Simple CRUD methods

	public PersonalRecord create(final PersonalRecord personalRecord) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		this.personalRecordRepository.save(personalRecord);

		return personalRecord;

	}

	public Collection<PersonalRecord> findAll() {
		Collection<PersonalRecord> result;

		result = this.personalRecordRepository.findAll();

		return result;
	}
	public PersonalRecord findOne(final Integer id) {
		final PersonalRecord result = this.personalRecordRepository.findOne(id);
		return result;
	}

	public void save(final PersonalRecord personalRecord) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		this.personalRecordRepository.save(personalRecord);
	}

	public void delete(final PersonalRecord personalRecord) {
		this.personalRecordRepository.delete(personalRecord);
	}

}
