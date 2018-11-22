
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.ReportRepository;

@Service
@Transactional
public class ReportService {

	@Autowired
	private ReportRepository	reportRepository;

	//Supporting services

	@Autowired
	private NoteService			noteService;
}
