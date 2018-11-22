
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MiscellaneousRecordRepository;
import security.LoginService;
import security.UserAccount;
import domain.MiscellaneousRecord;

@Service
@Transactional
public class MiscellaneousRecordService {

	// Manged Repository

	@Autowired
	private MiscellaneousRecordRepository	miscellaneousRecordRepository;


	// Simple CRUD methods

	public MiscellaneousRecord create(final MiscellaneousRecord miscellaneousRecord) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		this.miscellaneousRecordRepository.save(miscellaneousRecord);

		return miscellaneousRecord;

	}

	public Collection<MiscellaneousRecord> findAll() {
		Collection<MiscellaneousRecord> result;

		result = this.miscellaneousRecordRepository.findAll();

		return result;
	}
	public MiscellaneousRecord findOne(final Integer id) {
		final MiscellaneousRecord result = this.miscellaneousRecordRepository.findOne(id);
		return result;
	}

	public void save(final MiscellaneousRecord niscellaneousRecord) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		this.miscellaneousRecordRepository.save(niscellaneousRecord);
	}

	public void delete(final MiscellaneousRecord miscellaneousRecord) {
		this.miscellaneousRecordRepository.delete(miscellaneousRecord);
	}
}
