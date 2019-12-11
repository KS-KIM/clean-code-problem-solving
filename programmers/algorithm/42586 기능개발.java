import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * See https://programmers.co.kr/learn/courses/30/lessons/42586
 * @author KSKIM
 * @since 2019-12-12
 */
class Work {
	private static final int COMPLETE = 100;
	
	private int progress;
	private final int speed;
	
	public Work(int progress, int speed) {
		this.progress = progress;
		this.speed = speed;
	}
	
	public void work() {
		if (!isFinished()) {
			progress += speed;
		}
	}
	
	public boolean isFinished() {
		return progress >= COMPLETE;
	}
}

class ServiceDevelopment {
	private static final int HAS_NO_WORK = 0;

	private final List<Work> works;
	
	public ServiceDevelopment(List<Work> works) {
		this.works = Objects.requireNonNull(works);
	}
	
	public void work() {
		for (Work work: works) {
			work.work();
		}
	}
	
	public int distribute() {
		int distributeCount = 0;
		Iterator<Work> iter = works.iterator();
		while (iter.hasNext()) {
			Work work = iter.next();
			if (!work.isFinished()) {
				break;
			}
			iter.remove();
			++distributeCount;
		}
		return distributeCount;
	}
	
	public boolean hasWork() {
		return works.size() > HAS_NO_WORK;
	}
}

class Solution {
	private ServiceDevelopment serviceDevelopment;
	
	public int[] solution(int[] progresses, int[] speeds) {
		init(progresses, speeds);
        List<Integer> answer = new ArrayList<>();
    	while (serviceDevelopment.hasWork()) {
    		serviceDevelopment.work();
    		int distributeCount = serviceDevelopment.distribute();
    		if (distributeCount > 0) {
    			answer.add(distributeCount);
    		}
    	}
        return convertToArray(answer); 
    }
	
	private void init(int[] progresses, int[] speeds) {
    	List<Work> works = convertToWorks(progresses, speeds);
    	serviceDevelopment = new ServiceDevelopment(works);
	}
	
    private List<Work> convertToWorks(int[] progresses, int[] speeds) {
    	List<Work> works = new LinkedList<>();
    	for (int index = 0; index < progresses.length; ++index) {
    		works.add(new Work(progresses[index], speeds[index]));
    	}
    	return works;
    }
    
    private int[] convertToArray(List<Integer> numbers) {
    	int[] result = new int[numbers.size()];
    	for (int index = 0; index < numbers.size(); ++index) {
    		result[index] = numbers.get(index);
    	}
    	return result;
    }
}
