
package services;

import java.util.Collection;
import java.util.List;

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

	@Autowired
	private HandyWorkerService				handyWorkerService;
	@Autowired
	private CurriculumService				curriculumService;


	// Simple CRUD methods

	public MiscellaneousRecord create(String title, String linkAttachment, List<String> comments) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		MiscellaneousRecord miscellaneousRecord = new MiscellaneousRecord();
		miscellaneousRecord.setTitle(title);
		miscellaneousRecord.setLinkAttachment(linkAttachment);
		miscellaneousRecord.setComments(comments);

		return miscellaneousRecord;

	}
	public Collection<MiscellaneousRecord> findAll() {
		Collection<MiscellaneousRecord> result;

		result = this.miscellaneousRecordRepository.findAll();

		return result;
	}
	public MiscellaneousRecord findOne(Integer id) {
		MiscellaneousRecord result = this.miscellaneousRecordRepository.findOne(id);
		return result;
	}

	public void save(MiscellaneousRecord niscellaneousRecord) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		this.miscellaneousRecordRepository.save(niscellaneousRecord);
	}

	public void delete(MiscellaneousRecord miscellaneousRecord) {
		this.miscellaneousRecordRepository.delete(miscellaneousRecord);
	}

	public void deleteAll(List<MiscellaneousRecord> miscellaneousRecord) {
		this.miscellaneousRecordRepository.deleteInBatch(miscellaneousRecord);
	}
}
