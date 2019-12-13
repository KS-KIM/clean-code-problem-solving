import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
	public static Map<Skill, Integer> createPrerequisiteSkills (String input) {
		Map<Skill, Integer> skills = new HashMap<>();
		for (int index = 0; index < input.length(); ++index) {
			skills.put(new Skill(input.charAt(index)), index);
		}
		return skills;
	}

	public static List<Skill> createSkills (String input) {
		List<Skill> skills = new ArrayList<>();
		for (char skill: input.toCharArray()) {
			skills.add(new Skill(skill));
		}
		return skills;
	}
}

class PrerequisiteSkill {
	private final Map<Skill, Integer> prerequisiteSkills;

	public PrerequisiteSkill(Map<Skill, Integer> prerequisiteSkills) {
		this.prerequisiteSkills = prerequisiteSkills;
	}

	public boolean canLearn(Skill skill, int index) {
		if (contains(skill)) {
			Integer nextLearnSkillIndex = prerequisiteSkills.get(skill);
			return nextLearnSkillIndex.equals(index);
		}
		return true;
	}

	public boolean contains(Skill skill) {
		return prerequisiteSkills.containsKey(skill);
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
		Map<Skill, Integer> prerequisiteSkills = SkillFactory.createPrerequisiteSkills(skill);
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
