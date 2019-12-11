#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <cmath>
#include <cfloat>

/**
 * See https://www.acmicpc.net/problem/17822
 * @author KSKIM
 * @since 2019-12-12
 */

#define CW 0
#define CCW 1
#define MAX_DIR 4
#define ERASE 0

using namespace std;

int dr[MAX_DIR] = { 1, -1, 0, 0 };
int dc[MAX_DIR] = { 0, 0, -1, 1 };

struct Sector {
	int diskIndex;
	int sectorIndex;
};

struct Operation {
	int trackIndex;
	int offset;
	int direction;
	int totalRotates;
};

typedef vector<int> vi;
typedef vector<vi> vvi;

typedef vector<bool> vb;
typedef vector<vb> vvb;

typedef vector<Operation> vo;

int trackSize;
int sectorSize;
vvi disk;
vvb isVisited;

int totalOperations;
vo operations;

void sync();
void init();
int solution();
void rotateTracks(const Operation operation);
void rotateTrack(const int trackIndex, int rotateCount);
bool deleteAdjacentSectors();
int deleteAdjacentSector(const int diskIndex, const int sectorIndex);
void reallocateDisk();
int sumSectors();

int main() {
	sync();
	init();
	cout << solution();
	return 0;
}

void sync() {
	ios_base::sync_with_stdio(false);
	cin.tie(NULL);
}

void init() {
	cin >> trackSize >> sectorSize >> totalOperations;
	disk = vvi(trackSize, vi(sectorSize));

	for (int trackIndex = 0; trackIndex < trackSize; ++trackIndex) {
		for (int sectorIndex = 0; sectorIndex < sectorSize; ++sectorIndex) {
			cin >> disk[trackIndex][sectorIndex];
		}
	}

	for (int operationIndex = 0; operationIndex < totalOperations; ++operationIndex) {
		int trackIndex, direction, totalRotates;
		cin >> trackIndex >> direction >> totalRotates;
		operations.push_back({ trackIndex - 1, trackIndex, direction, totalRotates });
	}
}

int solution() {
	for (Operation operation : operations) {
		rotateTracks(operation);
		bool isDeleted = deleteAdjacentSectors();
		if (!isDeleted) {
			reallocateDisk();
		}
	}
	return sumSectors();
}

void rotateTracks(const Operation operation) {
	int direction = operation.direction;
	int totalRotates = operation.totalRotates % sectorSize;
	if (direction == CCW) {
		totalRotates = sectorSize - totalRotates;
	}

	int startTrackIndex = operation.trackIndex;
	int offset = operation.offset;
	for (int trackIndex = startTrackIndex; trackIndex < trackSize; trackIndex += offset) {
		rotateTrack(trackIndex, totalRotates);
	}
}

void rotateTrack(const int trackIndex, int rotateCount) {
	vi tmp(sectorSize);
	for (int sectorIndex = 0; sectorIndex < sectorSize; ++sectorIndex) {
		tmp[(sectorIndex + rotateCount) % sectorSize] = disk[trackIndex][sectorIndex];
	}

	for (int sectorIndex = 0; sectorIndex < sectorSize; ++sectorIndex) {
		disk[trackIndex][sectorIndex] = tmp[sectorIndex];
	}
}

bool deleteAdjacentSectors() {
	isVisited = vvb(trackSize, vb(sectorSize, false));

	int totalDeleteCount = 0;
	for (int diskIndex = 0; diskIndex < trackSize; ++diskIndex) {
		for (int sectorIndex = 0; sectorIndex < sectorSize; ++sectorIndex) {
			if (isVisited[diskIndex][sectorIndex]) {
				continue;
			}

			if (ERASE == disk[diskIndex][sectorIndex]) {
				continue;
			}

			totalDeleteCount += deleteAdjacentSector(diskIndex, sectorIndex);
		}
	}

	if (totalDeleteCount == 0) {
		return false;
	}
	else {
		return true;
	}
}

int deleteAdjacentSector(const int diskIndex, const int sectorIndex) {
	queue<Sector> visitQueue;
	visitQueue.push({ diskIndex, sectorIndex });
	isVisited[diskIndex][sectorIndex] = true;

	int deleteCount = 0;
	while (!visitQueue.empty()) {
		Sector here = visitQueue.front();
		visitQueue.pop();

		for (int dir = 0; dir < MAX_DIR; ++dir) {
			int thereDiskIndex = here.diskIndex + dr[dir];
			int thereSectorIndex = here.sectorIndex + dc[dir];

			if (thereDiskIndex < 0 || thereDiskIndex >= trackSize) {
				continue;
			}

			if (-1 == thereSectorIndex) {
				thereSectorIndex = sectorSize - 1;
			}

			if (sectorSize == thereSectorIndex) {
				thereSectorIndex = 0;
			}

			if (isVisited[thereDiskIndex][thereSectorIndex]) {
				continue;
			}

			int targetNumber = disk[diskIndex][sectorIndex];
			if (disk[thereDiskIndex][thereSectorIndex] != targetNumber) {
				continue;
			}

			visitQueue.push({ thereDiskIndex, thereSectorIndex });
			isVisited[thereDiskIndex][thereSectorIndex] = true;
			disk[thereDiskIndex][thereSectorIndex] = ERASE;
			++deleteCount;
		}
	}

	if (deleteCount > 0) {
		disk[diskIndex][sectorIndex] = ERASE;
		++deleteCount;
	}
	return deleteCount;
}

void reallocateDisk() {
	int totalSum = 0;
	int notErasedSectorCount = 0;
	for (int diskIndex = 0; diskIndex < trackSize; ++diskIndex) {
		for (int sectorIndex = 0; sectorIndex < sectorSize; ++sectorIndex) {
			if (ERASE == disk[diskIndex][sectorIndex]) {
				continue;
			}

			totalSum += disk[diskIndex][sectorIndex];
			++notErasedSectorCount;
		}
	}

	if (notErasedSectorCount == 0) {
		return;
	}

	double average = totalSum / (double)notErasedSectorCount;
	for (int diskIndex = 0; diskIndex < trackSize; ++diskIndex) {
		for (int sectorIndex = 0; sectorIndex < sectorSize; ++sectorIndex) {
			if (ERASE == disk[diskIndex][sectorIndex]) {
				continue;
			}

			double diff = fabsf(disk[diskIndex][sectorIndex] - average);
			if (diff < DBL_EPSILON) {
				continue;
			}

			if (disk[diskIndex][sectorIndex] - average > 0) {
				--disk[diskIndex][sectorIndex];
			}
			else {
				++disk[diskIndex][sectorIndex];
			}
		}
	}
}

int sumSectors() {
	int totalSum = 0;
	for (int diskIndex = 0; diskIndex < trackSize; ++diskIndex) {
		for (int sectorIndex = 0; sectorIndex < sectorSize; ++sectorIndex) {
			totalSum += disk[diskIndex][sectorIndex];
		}
	}
	return totalSum;
}
