
package services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.EndorserRecordRepository;
import security.LoginService;
import security.UserAccount;
import domain.EndorserRecord;

@Service
@Transactional
public class EndorserRecordService {

	// Manged Repository

	@Autowired
	private EndorserRecordRepository	endorserRecordRepository;

	@Autowired
	private HandyWorkerService			handyWorkerService;
	@Autowired
	private CurriculumService			curriculumService;


	// Simple CRUD methods

	public EndorserRecord create(String fullName, String email, String phoneNumber, String linkLinkedInProfile, List<String> comments) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		EndorserRecord endorserRecord = new EndorserRecord();
		endorserRecord.setFullName(fullName);
		endorserRecord.setEmail(email);
		endorserRecord.setPhoneNumber(phoneNumber);
		endorserRecord.setLinkLinkedInProfile(linkLinkedInProfile);
		endorserRecord.setComments(comments);

		return endorserRecord;

	}

	public Collection<EndorserRecord> findAll() {
		Collection<EndorserRecord> result;

		result = this.endorserRecordRepository.findAll();

		return result;
	}
	public EndorserRecord findOne(Integer id) {
		EndorserRecord result = this.endorserRecordRepository.findOne(id);
		return result;
	}

	public void save(EndorserRecord endorserRecord) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		this.endorserRecordRepository.save(endorserRecord);
	}

	public void delete(EndorserRecord endorserRecord) {
		this.endorserRecordRepository.delete(endorserRecord);
	}

	public void deleteAll(List<EndorserRecord> endorserRecord) {
		this.endorserRecordRepository.deleteInBatch(endorserRecord);
	}

}
