import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * See https://programmers.co.kr/learn/courses/30/lessons/42626
 * 
 * @author KSKIM
 * @since 2019-12-12
 */
class ScovilleShaker {
	private static final int SHAKE_REQUIRE_NUMBER = 2;
	private static final int SECOND_SCOVILLE_SHAKE_RECIPE_MUL_NUM = 2;

	private final PriorityQueue<Integer> scovilleShakeJobQueue;
	private final int targetScoville;
	private int shakeCount = 0;

	public ScovilleShaker(List<Integer> scoville, int targetScoville) {
		this.scovilleShakeJobQueue = new PriorityQueue<>(scoville);
		this.targetScoville = targetScoville;
	}

	public void shake() {
		if (!canShake()) {
			return;
		}
		int firstSmallScoville = scovilleShakeJobQueue.poll();
		int secondSmallScoville = scovilleShakeJobQueue.poll();
		int shakedFoodScoville = shake(firstSmallScoville, secondSmallScoville);
		scovilleShakeJobQueue.add(shakedFoodScoville);
		++shakeCount;
	}

	public boolean canShake() {
		return scovilleShakeJobQueue.size() >= SHAKE_REQUIRE_NUMBER && !isShakeFinished();
	}

	public boolean isShakeFinished() {
		if (scovilleShakeJobQueue.size() == 0) {
			return false;
		}
		return scovilleShakeJobQueue.peek() >= targetScoville;
	}

	private int shake(int firstSmallScoville, int secondSmallScoville) {
		return firstSmallScoville + secondSmallScoville * SECOND_SCOVILLE_SHAKE_RECIPE_MUL_NUM;
	}

	public int getShakeCount() {
		return this.shakeCount;
	}
}

class Solution {
	private static final int FAILED_TO_MATCH_TARGET = -1;

	public int solution(int[] scoville, int K) {
		List<Integer> scovilles = arrayToList(scoville);
		ScovilleShaker scovilleShaker = new ScovilleShaker(scovilles, K);
		while (scovilleShaker.canShake()) {
			scovilleShaker.shake();
		}

		if (scovilleShaker.isShakeFinished()) {
			return scovilleShaker.getShakeCount();
		}
		return FAILED_TO_MATCH_TARGET;
	}

	private List<Integer> arrayToList(int[] array) {
		return Arrays.stream(array).boxed().collect(Collectors.toList());
	}
}
