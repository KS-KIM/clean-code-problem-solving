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

class SkillBook {
	private final Map<Skill, Integer> prerequisiteSkills;

	public SkillBook(Map<Skill, Integer> prerequisiteSkills) {
		this.prerequisiteSkills = Objects.requireNonNull(prerequisiteSkills);
	}

	public boolean isAvailableSkillTree(List<Skill> skillTree) {
		int nextLearnPrerequisiteSkillIndex = 0;
		for (Skill skill: skillTree) {
			if (!canLearn(skill, nextLearnPrerequisiteSkillIndex)) {
				return false;
			}
			if (isPrerequisiteSkill(skill)) {
				++nextLearnPrerequisiteSkillIndex;
			}
		}
		return true;
	}

	public boolean canLearn(Skill skill, int nextLearnPrerequisiteSkillIndex) {
		if (isPrerequisiteSkill(skill)) {
			Integer nextLearnSkillIndex = prerequisiteSkills.get(skill);
			return nextLearnSkillIndex.equals(nextLearnPrerequisiteSkillIndex);
		}
		return true;
	}

	public boolean isPrerequisiteSkill(Skill skill) {
		return prerequisiteSkills.containsKey(skill);
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

class Solution {
	public int solution(String skill, String[] skillTrees) {
		Map<Skill, Integer> prerequisiteSkills = SkillFactory.createPrerequisiteSkills(skill);
		SkillBook skillBook = new SkillBook(prerequisiteSkills);
		return getCanLearnSkillTreeCount(skillBook, skillTrees);
	}

	private int getCanLearnSkillTreeCount(SkillBook skillBook, String[] skillTrees) {
		int canLearnCount = 0;
		for (String skillTree: skillTrees) {
			if (canLearnSkillTree(skillBook, skillTree)) {
				++canLearnCount;
			}
		}
		return canLearnCount;
	}

	private boolean canLearnSkillTree(SkillBook skillBook, String skillTree) {
		List<Skill> skills = SkillFactory.createSkills(skillTree);
		return skillBook.isAvailableSkillTree(skills);
	}
}
