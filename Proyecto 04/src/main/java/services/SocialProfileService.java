
package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import repositories.SocialProfileRepository;
import domain.SocialProfile;

public class SocialProfileService {

	@Autowired
	private SocialProfileRepository	socialProfileRepository;


	public SocialProfile save(SocialProfile socialProfile) {
		return this.socialProfileRepository.save(socialProfile);
	}

	public SocialProfile create(String nick, String name, String profileLink) {
		SocialProfile socialProfile = new SocialProfile();

		socialProfile.setName(name);
		socialProfile.setNick(nick);
		socialProfile.setProfileLink(profileLink);

		return socialProfile;
	}

	public List<SocialProfile> socialProfiles() {
		return this.socialProfileRepository.findAll();
	}

	public void delete(SocialProfile socialProfile) {
		this.socialProfileRepository.delete(socialProfile);
	}

}
