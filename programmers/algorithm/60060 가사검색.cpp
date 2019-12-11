#include <iostream>
#include <string>
#include <vector>
#include <algorithm>

/**
 * See https://programmers.co.kr/learn/courses/30/lessons/60060
 * @author KSKIM
 * @since 2019-12-12
 */

using namespace std;

const int MAX_TRIE_CHILDREN = 26;
const int MAX_WORD_SIZE = 10001;

int alphabetToIndex(const char alphabet) {
	return alphabet - 'a';
}

bool isWildCard(const char character) {
	return character == '?';
}

class Trie {
private:
	vector<Trie *> children;
	int matchCount;
	bool isTerminal;

	void insert(const string &, const int);
	int find(const string &, const int);

public:
	Trie();
	void insert(const string &);
	int find(const string &);
};

class LyricsSearcher {
private:
	vector<Trie> trie;
	vector<Trie> reverseTrie;

public:
	LyricsSearcher();
	void insert(string);
	int find(string);
};

Trie::Trie() {
	children = vector<Trie *>(MAX_TRIE_CHILDREN);
	matchCount = 0;
	isTerminal = false;
}

void Trie::insert(const string &word, const int index) {
	++matchCount;

	if (index == word.size()) {
		isTerminal = true;
		return;
	}

	int alphabet = word[index];
	int childIndex = alphabetToIndex(alphabet);
	if (children[childIndex] == NULL) {
		children[childIndex] = new Trie();
	}

	return children[childIndex]->insert(word, index + 1);
}

void Trie::insert(const string &word) {
	return insert(word, 0);
}

int Trie::find(const string &query, const int index) {
	if (index == query.size()) {
		if (isTerminal) {
			return 1;
		}
		else {
			return 0;
		}
	}

	char character = query[index];
	if (isWildCard(character)) {
		return matchCount;
	}

	int childIndex = alphabetToIndex(character);
	if (children[childIndex] == NULL) {
		return 0;
	}

	return children[childIndex]->find(query, index + 1);
}

int Trie::find(const string &target) {
	return find(target, 0);
}

LyricsSearcher::LyricsSearcher() {
	trie = vector<Trie>(MAX_WORD_SIZE);
	reverseTrie = vector<Trie>(MAX_WORD_SIZE);
}

void LyricsSearcher::insert(string word) {
	trie[word.size()].insert(word);
	reverse(word.begin(), word.end());
	reverseTrie[word.size()].insert(word);
}

int LyricsSearcher::find(string query) {
	if (query[0] == '?') {
		reverse(query.begin(), query.end());
		return reverseTrie[query.size()].find(query);
	}
	else {
		return trie[query.size()].find(query);
	}
}

vector<int> solution(vector<string> words, vector<string> queries) {
	LyricsSearcher lyricsSearcher;

	for (auto word : words) {
		lyricsSearcher.insert(word);
	}

	vector<int> answers;
	for (auto query : queries) {
		int matchWords = lyricsSearcher.find(query);
		answers.push_back(matchWords);
	}

	return answers;
}
