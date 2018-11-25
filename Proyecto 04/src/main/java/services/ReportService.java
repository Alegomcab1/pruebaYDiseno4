
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.ReportRepository;
import domain.Report;

@Service
@Transactional
public class ReportService {

	@Autowired
	private ReportRepository	reportRepository;


	public Report create(String description) {

		Report report = new Report();
		List<String> attachments = new ArrayList<String>();
		report.setMoment(LocalDate.now());
		report.setDescription(description);
		report.setAttachments(attachments);

		return report;
	}

	public Report save(Report report) {
		return this.reportRepository.save(report);
	}

	public Report update(Report report) {
		return this.save(report);
	}

	public void delete(Report report) {
		if (report.getNotes().size() == 0)
			this.reportRepository.delete(report);
	}

	public List<Report> findAll() {
		return this.reportRepository.findAll();
	}
}
