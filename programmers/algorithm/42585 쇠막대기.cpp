#include <vector>
#include <string>
#include <stack>

/**
 * See https://programmers.co.kr/learn/courses/30/lessons/42585
 * @author KSKIM
 * @since 2019-12-12
 */

using namespace std;

class PipeCutter {
private:
	string pipe;
	stack<bool> pipeStack;
	int pipeLength;

	int moveCutterAndWork(int index) {
		if (isRaser(index)) { // "()"
			return cut(index);
		}

		if (isLeftParent(index)) { // "("
			pipeStack.push(true);
			return 0;
		}

		if (isRightParent(index)) { // "))"
			pipeStack.pop();
			return 1; // @TODO Refactor [private: const int END_OF_PIPE;]
		}

		throw new exception("파악할 수 없는 문자열입니다.");
	}

	bool isRaser(int index) {
		if (index == 0) {
			return false;
		}
		return isLeftParent(index - 1) && isRightParent(index);
	}

	bool isLeftParent(int index) {
		return pipe[index] == '(';
	}

	bool isRightParent(int index) {
		return pipe[index] == ')';
	}

	int cut(int index) {
		pipeStack.pop();
		return pipeStack.size();
	}

public:
	PipeCutter(string pipe) {
		this->pipe = pipe;
		this->pipeLength = pipe.size();
	}

	int cutAll() {
		int cuttingPipeCount = 0;
		for (int index = 0; index < pipeLength; ++index) {
			cuttingPipeCount += moveCutterAndWork(index);
		}
		return cuttingPipeCount;
	}
};

int solution(string arrangement) {
	PipeCutter pipeCutter(arrangement);
	int totalCuttingCount = pipeCutter.cutAll();
	return totalCuttingCount;
}
