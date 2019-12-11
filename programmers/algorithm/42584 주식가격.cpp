#include <stack>
#include <vector>

/**
 * See https://programmers.co.kr/learn/courses/30/lessons/42584
 * @author KSKIM
 * @since 2019-12-12
 */

using namespace std;

class PriceNonDropTimeCalculator {
private:
	vector<int> prices;
	stack<int> topPriceIndexStack;
	vector<int> nonDropTimes;
	int priceLength;

	int getTopPrice() {
		int topIndex = topPriceIndexStack.top();
		return getPrice(topIndex);
	}

	int getPrice(int index) {
		return prices[index];
	}

	bool isDropPrice(int index) {
		return getTopPrice() > getPrice(index);
	}

	void calculateNonDropTimes(int index) {
		while (!topPriceIndexStack.empty() && isDropPrice(index)) {
			int topIndex = topPriceIndexStack.top();
			nonDropTimes[topIndex] = index - topIndex;
			topPriceIndexStack.pop();
		}
	}

	void calculateLastNonDropTimes() {
		while (!topPriceIndexStack.empty()) {
			int topIndex = topPriceIndexStack.top();
			nonDropTimes[topIndex] = (priceLength - 1) - topIndex;
			topPriceIndexStack.pop();
		}
	}


public:
	PriceNonDropTimeCalculator(vector<int> prices) {
		this->prices = prices;
		nonDropTimes = vector<int>(prices.size(), 0);
		priceLength = prices.size();
	}

	vector<int> getNonDropTimes() {
		for (int index = 0; index < priceLength; ++index) {
			calculateNonDropTimes(index);
			topPriceIndexStack.push(index);
		}
		calculateLastNonDropTimes();
		return nonDropTimes;
	}
};

vector<int> solution(vector<int> prices) {
	PriceNonDropTimeCalculator priceNonDropTimeCalculator(prices);
	return priceNonDropTimeCalculator.getNonDropTimes();
}
