package services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import repositories.ConfigurationRepository;
import domain.Actor;
import domain.Application;
import domain.Box;
import domain.Customer;
import domain.EducationRecord;
import domain.Endorser;
import domain.EndorserRecord;
import domain.Endorsment;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Message;
import domain.MiscellaneousRecord;
import domain.Note;
import domain.Phase;
import domain.ProfessionalRecord;
import domain.Referee;
import domain.Report;
import domain.Section;
import domain.SocialProfile;
import domain.Tutorial;

public class ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    public List<String> getSpamWords() {
	return this.configurationRepository.spamWords();
    }

    public Boolean isStringSpam(String s, List<String> spamWords) {
	Boolean result = false;

	for (String g : spamWords)
	    if (s.contains(g)) {
		result = true;
		break;
	    }
	return result;
    }

    public Boolean isActorSuspicious(Actor a) {
	Boolean result = false;
	List<String> spamWords = new ArrayList<String>();
	spamWords = this.getSpamWords();

	// COMPROBANDO PARAMETROS DEL PERFIL DE LOS ACTORES
	result = this.isStringSpam(a.getName(), spamWords);
	if (!result)
	    result = this.isStringSpam(a.getMiddleName(), spamWords);
	if (!result)
	    result = this.isStringSpam(a.getSurname(), spamWords);
	if (!result)
	    result = this.isStringSpam(a.getAddress(), spamWords);
	if (!result)
	    result = this.isStringSpam(a.getEmail(), spamWords);
	if (!result)
	    result = this.isStringSpam(a.getPhoneNumber(), spamWords);
	if (!result)
	    result = this.isStringSpam(a.getPhoto(), spamWords);
	if (!result)
	    result = this.isStringSpam(a.getUserAccount().getUsername(),
		    spamWords);
	if (!result)
	    // COMPROBANDO LAS REDES SOCIALES
	    for (SocialProfile s : a.getSocialProfiles())
		if (this.isStringSpam(s.getName(), spamWords)
			|| this.isStringSpam(s.getNick(), spamWords)
			|| this.isStringSpam(s.getProfileLink(), spamWords)) {
		    result = true;
		    break;
		}
	if (!result)
	    // COMPROBANDO LAS CAJAS DEL ACTOR
	    for (Box b : a.getBoxes())
		if (this.isStringSpam(b.getName(), spamWords)) {
		    result = true;
		    break;
		} else
		    // COMPROBANDO LOS MENSAJES DEL ACTOR
		    for (Message g : b.getMessages())
			if (g.getSender().equals(a)
				&& (this.isStringSpam(g.getBody(), spamWords) || this
					.isStringSpam(g.getSubject(), spamWords))) {
			    result = true;
			    break;
			}
	if (!result)

	    if (a instanceof HandyWorker) {
		HandyWorker h = (HandyWorker) a;
		// COMPROBANDO TUTORIALES DEL HANDY WORKER
		for (Tutorial t : h.getTutorials())
		    if (this.isStringSpam(t.getTitle(), spamWords)
			    || this.isStringSpam(t.getSumary(), spamWords)) {
			result = true;
			break;
		    } else
			for (Section s : t.getSections())
			    if (this.isStringSpam(s.getSectionTitle(),
				    spamWords)
				    || this.isStringSpam(s.getText(), spamWords)) {
				result = true;
				break;
			    }

		// COMPROBANDO LOS COMENTARIOS DE LAS APLICATIONS
		for (Application ap : h.getApplications())
		    for (String s : ap.getComments())
			if (this.isStringSpam(s, spamWords)) {
			    result = true;
			    break;
			}
		// COMPROBANDO EL CURRICULUM DEL HANDY WORKER
		for (EndorserRecord b : h.getCurriculum().getEndorserRecords())
		    if (this.isStringSpam(b.getEmail(), spamWords)
			    || this.isStringSpam(b.getFullName(), spamWords)
			    || this.isStringSpam(b.getLinkLinkedInProfile(),
				    spamWords)
			    || this.isStringSpam(b.getPhoneNumber(), spamWords)) {
			result = true;
			break;
		    } else
			for (String str : b.getComments())
			    if (this.isStringSpam(str, spamWords)) {
				result = true;
				break;
			    }
		if (!result)
		    for (MiscellaneousRecord m : h.getCurriculum()
			    .getMiscellaneousRecords())
			if (this.isStringSpam(m.getTitle(), spamWords)) {
			    result = true;
			    break;
			} else
			    for (String str : m.getComments())
				if (this.isStringSpam(str, spamWords)) {
				    result = true;
				    break;
				}
		if (!result)
		    for (EducationRecord m : h.getCurriculum()
			    .getEducationRecords())
			if (this.isStringSpam(m.getTitle(), spamWords)
				|| this.isStringSpam(m.getInstitution(),
					spamWords)) {
			    result = true;
			    break;
			} else
			    for (String str : m.getComments())
				if (this.isStringSpam(str, spamWords)) {
				    result = true;
				    break;
				}
		if (!result)
		    for (ProfessionalRecord m : h.getCurriculum()
			    .getProfessionalRecords())
			if (this.isStringSpam(m.getNameCompany(), spamWords)
				|| this.isStringSpam(m.getRole(), spamWords)) {
			    result = true;
			    break;
			} else
			    for (String str : m.getComments())
				if (this.isStringSpam(str, spamWords)) {
				    result = true;
				    break;
				}
		if (!result)
		    if (this.isStringSpam(h.getCurriculum().getPersonalRecord()
			    .getFullName(), spamWords)
			    || this.isStringSpam(h.getCurriculum()
				    .getPersonalRecord().getEmail(), spamWords)
			    || this.isStringSpam(h.getCurriculum()
				    .getPersonalRecord().getPhoto(), spamWords)
			    || this.isStringSpam(h.getCurriculum()
				    .getPersonalRecord().getPhoneNumber(),
				    spamWords)) {
		    }

	    }
	// COMRPOBACION COMPLETA REFEREE
	if (!result)
	    if (a instanceof Referee) {
		Referee h = (Referee) a;
		for (Report b : h.getReports())
		    if (this.isStringSpam(b.getDescription(), spamWords)) {
			result = true;
			break;
		    } else if (!result)
			for (String str : b.getAttachments())
			    if (this.isStringSpam(str, spamWords)) {
				result = true;
				break;

			    } else if (!result)
				for (Note n : b.getNotes())
				    if (this.isStringSpam(
					    n.getMandatoryComment(), spamWords)) {
					result = true;
					break;
				    } else
					for (String string : n
						.getOptionalComments())
					    if (this.isStringSpam(string,
						    spamWords)) {
						result = true;
						break;
					    }
	    }
	if (!result)
	    if (a instanceof Customer) {
		Customer h = (Customer) a;

		for (FixUpTask t : h.getFixUpTasks())
		    if (this.isStringSpam(t.getAddress(), spamWords)
			    || this.isStringSpam(t.getDescription(), spamWords)) {
			result = true;
			break;
		    } else
			for (Phase s : t.getPhases())
			    if (this.isStringSpam(s.getTitle(), spamWords)
				    || this.isStringSpam(s.getDescription(),
					    spamWords)) {
				result = true;
				break;
			    }
	    }
	if (!result)

	    if (a instanceof Endorser) {
		Endorser h = (Endorser) a;
		for (Endorsment t : h.getEndorsments())
		    for (String s : t.getComments())
			if (this.isStringSpam(s, spamWords)) {
			    result = true;
			    break;
			}
	    }
	return result;
    }
}
