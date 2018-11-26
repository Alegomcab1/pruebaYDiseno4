
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.ComplaintRepository;
import domain.Complaint;

@Service
@Transactional
public class ComplaintService {

	@Autowired
	private ComplaintRepository	complaintRepository;


	//Consultar lo del ticker
	public Complaint create(String description) {

		Complaint complaint = new Complaint();
		List<String> attachments = new ArrayList<String>();
		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1);
		complaint.setTicker(ComplaintService.generateTicker());
		complaint.setMoment(thisMoment);
		complaint.setDescription(description);
		complaint.setAttachments(attachments);

		return complaint;
	}

	//Método auxiliar para generar el ticker-------------------------------
	private static String generateTicker() {
		String res = "";
		Date date = null;
		String date1;
		String date2 = LocalDate.now().toString();
		SimpleDateFormat df_in = new SimpleDateFormat("yyMMdd");
		SimpleDateFormat df_output = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = df_output.parse(date2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		date1 = df_in.format(date);
		res = res + date1 + "-";
		for (int i = 0; i < 6; i++)
			res = res + ComplaintService.rndChar();
		return res;
	}

	private static char rndChar() {
		int rnd = (int) (Math.random() * 52); // or use Random or whatever
		char base = (rnd < 26) ? 'A' : 'a';
		return (char) (base + rnd % 26);

	}
	//-----------------------------------------------------------------------

	public Complaint save(Complaint complaint) {
		return this.complaintRepository.save(complaint);
	}

	public Complaint update(Complaint complaint) {
		return this.save(complaint);
	}

	public void delete(Complaint complaint) {
		this.complaintRepository.delete(complaint);
	}

	public List<Complaint> findAll() {
		return this.complaintRepository.findAll();
	}
}
