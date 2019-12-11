#include <string>
#include <vector>
#include <stack>

/**
 * See https://programmers.co.kr/learn/courses/30/lessons/42588
 * @author KSKIM
 * @since 2019-12-12
 */

using namespace std;

class SignalSender {
private:
	vector<int> heights;
	stack<int> towerIndexStack;
	vector<int> receivers;
	int totalHeightCount;

	void send(int index) {
		receivers[index] = findReceiveTower(index);
	}

	int findReceiveTower(int index) {
		while (!towerIndexStack.empty() && !canReceiveSignal(index)) {
			towerIndexStack.pop();
		}

		if (!towerIndexStack.empty()) {
			return towerIndexStack.top() + 1;
		}
		return 0;
	}

	bool canReceiveSignal(int index) {
		return getTowerHeightOfIndexStackTop() > getTowerHeight(index);
	}

	int getTowerHeightOfIndexStackTop() {
		return getTowerHeight(towerIndexStack.top());
	}

	int getTowerHeight(int index) {
		return heights[index];
	}

public:
	SignalSender(const vector<int> &heights) {
		this->heights = heights;
		totalHeightCount = heights.size();
		receivers = vector<int>(totalHeightCount, 0);
	}

	void sendSignals() {
		for (int index = 0; index < totalHeightCount; ++index) {
			send(index);
			towerIndexStack.push(index);
		}
	}

	vector<int> getReceivers() {
		return receivers;
	}
};

vector<int> solution(vector<int> heights) {
	SignalSender signalSender(heights);
	signalSender.sendSignals();
	vector<int> receivers = signalSender.getReceivers();
	return receivers;
}
