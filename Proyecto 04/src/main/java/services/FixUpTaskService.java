
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

import repositories.FixUpTaskRepository;
import domain.Category;
import domain.FixUpTask;
import domain.Phase;
import domain.Warranty;

public class FixUpTaskService {

	@Autowired
	private FixUpTaskRepository	fixUpTaskRepository;


	public FixUpTask create(String description, String address, Double maxPrice, Date realizationTime, Collection<Warranty> warranties, Collection<Phase> phases, Collection<Category> categories) {
		FixUpTask fixUpTask = new FixUpTask();
		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1);
		fixUpTask.setTicker(FixUpTaskService.generateTicker());
		fixUpTask.setMomentPublished(thisMoment);
		fixUpTask.setDescription(description);
		fixUpTask.setAddress(address);
		fixUpTask.setMaxPrice(maxPrice);
		fixUpTask.setRealizationTime(realizationTime);
		fixUpTask.setWarranties(warranties);
		fixUpTask.setPhases(phases);
		fixUpTask.setCategories(categories);
		return fixUpTask;

	}

	public FixUpTask save(FixUpTask fixUpTask) {
		return this.fixUpTaskRepository.save(fixUpTask);
	}

	public FixUpTask update(FixUpTask fixUpTask) {
		return this.save(fixUpTask);
	}

	public void delete(FixUpTask fixUpTask) {
		this.fixUpTaskRepository.delete(fixUpTask);
	}

	public List<FixUpTask> findAll() {
		return this.fixUpTaskRepository.findAll();
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
			res = res + FixUpTaskService.rndChar();
		return res;
	}

	private static char rndChar() {
		int rnd = (int) (Math.random() * 52); // or use Random or whatever
		char base = (rnd < 26) ? 'A' : 'a';
		return (char) (base + rnd % 26);

	}
}
