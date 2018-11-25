
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ProfessionalRecordRepository;
import security.LoginService;
import security.UserAccount;
import domain.ProfessionalRecord;

@Service
@Transactional
public class ProfessionalRecordService {

	// Manged Repository

	@Autowired
	private ProfessionalRecordRepository	professionalRecordRepository;


	// Simple CRUD methods

	public ProfessionalRecord create(final ProfessionalRecord professionalRecord) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		this.professionalRecordRepository.save(professionalRecord);

		return professionalRecord;

	}

	public Collection<ProfessionalRecord> findAll() {
		Collection<ProfessionalRecord> result;

		result = this.professionalRecordRepository.findAll();

		return result;
	}
	public ProfessionalRecord findOne(final Integer id) {
		final ProfessionalRecord result = this.professionalRecordRepository.findOne(id);
		return result;
	}

	public void save(final ProfessionalRecord professionalRecord) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		this.professionalRecordRepository.save(professionalRecord);
	}

	public void delete(final ProfessionalRecord professionalRecord) {
		this.professionalRecordRepository.delete(professionalRecord);
	}
}
