
package services;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.ReportRepository;
import domain.Report;

@Service
@Transactional
public class ReportService {

	@Autowired
	private ReportRepository	reportRepository;


	public Report create(String description, List<String> attachments, boolean isFinalMode) {

		Report report = new Report();
		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1);
		report.setMoment(thisMoment);
		report.setDescription(description);
		report.setAttachments(attachments);
		report.setFinalMode(isFinalMode);

		return report;
	}

	public Report save(Report report) {
		return this.reportRepository.save(report);
	}

	public Report findOne(int reportId) {
		return this.reportRepository.findOne(reportId);
	}

	public void delete(Report report) {
		if (report.getNotes().size() == 0)
			this.reportRepository.delete(report);
	}

	public List<Report> findAll() {
		return this.reportRepository.findAll();
	}
}
