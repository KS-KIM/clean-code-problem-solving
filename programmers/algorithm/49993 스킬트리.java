import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * See https://programmers.co.kr/learn/courses/30/lessons/49993
 * 
 * @author KSKIM
 * @since 2019-12-13
 */
class Skill {
	private final char skill;

	public Skill(char skill) {
		validateSkillName(skill);
		this.skill = skill;
	}

	private void validateSkillName(char skill) {
		if (!Character.isUpperCase(skill)) {
			throw new IllegalArgumentException("스킬은 A-Z 사이의 문자여야 합니다.");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Skill skill1 = (Skill)o;
		return Objects.equals(skill, skill1.skill);
	}

	@Override
	public int hashCode() {
		return Objects.hash(skill);
	}
}

class SkillFactory {
	public static List<Skill> createSkills (String input) {
		List<Skill> skills = new ArrayList<>();
		for (char skill: input.toCharArray()) {
			skills.add(new Skill(skill));
		}
		return skills;
	}
}

// @TODO Refactor List<Skill> to HashMap<Skill, Integer> for efficiency
class PrerequisiteSkill {
	private final List<Skill> prerequisiteSkills;

	public PrerequisiteSkill(List<Skill> prerequisiteSkills) {
		validateSkills(prerequisiteSkills);
		this.prerequisiteSkills = prerequisiteSkills;
	}

	private void validateSkills(List<Skill> skills) {
		Objects.requireNonNull(skills);
		validateSkillDuplicate(skills);
	}

	private void validateSkillDuplicate(List<Skill> skills) {
		Set<Skill> distinctSkills = new HashSet<Skill>(skills);
		if (distinctSkills.size() != skills.size()) {
			throw new IllegalArgumentException("중복된 스킬이 있습니다.");
		}
	}

	public boolean canLearn(Skill skill, int index) {
		if (contains(skill)) {
			Skill nextLearnSkill = prerequisiteSkills.get(index);
			return nextLearnSkill.equals(skill);
		}
		return true;
	}

	public boolean contains(Skill skill) {
		return prerequisiteSkills.contains(skill);
	}
}

class SkillBook {
	private final PrerequisiteSkill prerequisiteSkill;
	private final List<Skill> skills;
	private int learnedPrerequisiteSkill = 0;

	public SkillBook(PrerequisiteSkill prerequisiteSkill, List<Skill> skills) {
		this.prerequisiteSkill = Objects.requireNonNull(prerequisiteSkill);
		this.skills = Objects.requireNonNull(skills);
	}

	public boolean isAvailableSkillTree() {
		for (Skill skill: skills) {
			if (!canLearn(skill)) {
				return false;
			}
		}
		return true;
	}

	private boolean canLearn(Skill skill) {
		if (!prerequisiteSkill.canLearn(skill, learnedPrerequisiteSkill)) {
			return false;
		}
		if (prerequisiteSkill.contains(skill)) {
			++learnedPrerequisiteSkill;
		}
		return true;
	}
}

class Solution {
	private int canLearnCount = 0;

	public int solution(String skill, String[] skillTrees) {
		List<Skill> prerequisiteSkills = SkillFactory.createSkills(skill);
		PrerequisiteSkill prerequisiteSkill = new PrerequisiteSkill(prerequisiteSkills);
		for (String skillTree: skillTrees) {
			checkLearnAvailability(prerequisiteSkill, skillTree);
		}
		return canLearnCount;
	}

	private void checkLearnAvailability(PrerequisiteSkill prerequisiteSkill, String skillTree) {
		List<Skill> skills = SkillFactory.createSkills(skillTree);
		SkillBook skillBook = new SkillBook(prerequisiteSkill, skills);
		if (skillBook.isAvailableSkillTree()) {
			++canLearnCount;
		}
	}
}
