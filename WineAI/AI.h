
#ifndef  _AI
#define   _AI
#include "Board.h"
#include <ctime>
class AI :public Board
{
private:
	

public:
	int total = 0;					// ����������
	int hashCount = 0;				// hash�����д���
	int searchDepth = 0;			// ʵ���������
	int time_left = 10000000;		//һ�����ʣ��˼��ʱ��
	int timeout_turn = 5000;		//ÿ�����˼��ʱ��
	int timeout_match = 10000000;	//һ�������˼��ʱ��
	int ThinkTime = 0;				//��ǰ�ⲽ���˼��ʱ��
	Point bestPoint;				//AI˼������ѵ�
	Line bestLine;					//AI˼�������·��
	clock_t start;					//AI��ʼ˼����ʱ���
	bool stopThink = false;			//AI�Ƿ�ֹͣ˼��

	//��������
	Pos MainSearch();
	//��ȡ����߷�
	Pos GetBestMove();
	void sort(Point * a, int n);
	void PutChess(Pos next);
	void RecordHash(int depth, int val, int hashf);
	void RecordPVS(Pos best);
	Pos GetNextMove(MoveList &moveList);
	int ProbeHash(int depth, int alpha, int beta);
	int GetTime();
	int StopTime();
	//�����߷�
	int EvaluateMove(Cell * c);
	Point RootSearch(int depth, int alpha, int beta, Line *pline);
	int AlphaBeta(int depth, int alpha, int beta, Line *pline);
	//�ü��߷��б��ɹ����زü�����������޷��ü�����0
	int CutMoveList(Pos * move, Point * cand, int Csize);
	//���ɿ����߷�
	int GenerateMove(Pos * move);
	//���۵�ǰ����
	int evaluate();

};
#endif
