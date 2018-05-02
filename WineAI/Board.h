
#ifndef  _BOARD
#define  _BOARD
#include <string.h>

typedef unsigned long long U64;
const int win = 7;              // ����
const int flex4 = 6;            // ����
const int block4 = 5;           // ����
const int flex3 = 4;            // ����
const int block3 = 3;           // ����
const int flex2 = 2;            // ���
const int block2 = 1;           // �߶�
const int Ntype = 8;            // ���͸���
const int MaxSize = 20;         // �������ߴ�
const int MaxMoves = 40;        // ÿ������֧��
const int hashSize = 1 << 22;   // ��ͨ�û���ߴ�
const int pvsSize = 1 << 20;    // pvs�û���ߴ�
const int MaxDepth = 20;        // ����������
const int MinDepth = 2;			// ��С�������

// hash�����
const int hash_exact = 0;
const int hash_alpha = 1;
const int hash_beta = 2;
const int unknown = -20000;

// ���״̬
enum Pieces { White=0, Black=1, Empty=2, Outside=3 };
// ��������
const int dx[4] = { 1, 0, 1, 1 };
const int dy[4] = { 0, 1, 1, -1 };
// ����
struct Pos
{
	int x;
	int y;
};

// �����۵ĵ�
struct Point
{
	Pos p;
	int val;
};

// ���̵���ṹ
struct Cell
{
	int piece;			//������ɫ
	int IsCand;			//��������������
	int pattern[2][4];	//�ڰ�˫���ĸ��������������
};

// ��ϣ��ṹ
struct Hashe
{
	U64 key;
	int depth;
	int hashf;
	int val;
};
struct Pv
{
	U64 key;
	Pos best;
};

// �߷�·��
struct Line
{
	int n;
	Pos moves[MaxDepth];
};

struct MoveList
{
	int phase, n, index;
	bool first;
	Pos hashMove;
	Pos moves[64];
};

class Board
{
public:
	int step = 0;                                 // ����������
	int size = 15;                                // ���̵�ǰ�ߴ�
	int b_start, b_end;                           // ����������Сֵ�����ֵ
	U64 zobristKey = 0;                           // ��ʾ��ǰ�����zobristKey
	U64 zobrist[2][MaxSize + 4][MaxSize + 4];     // zobrist��ֵ��
	Hashe hashTable[hashSize];                    // ��ϣ��
	Pv pvsTable[pvsSize];                         // pvs�û���
	int typeTable[10][6][6][3];                   // �����жϸ�����
	int patternTable[65536][2];                   // ���ͱ�
	int pval[8][8][8][8];                         // �߷����۱�
	Cell cell[MaxSize + 8][MaxSize + 8];          // ����
	Pos remMove[MaxSize * MaxSize];               // �߷��б�
	Point cand[256];                              // ��ʱ�洢�߷�
	bool IsLose[MaxSize + 4][MaxSize + 4];        // ��¼���ڵ�ıذܵ�
	int who = Black;                              // ���ӷ�
	int opp = White;                              // ��һ��
	Point rootMove[64];                           // ���ڵ��߷�
	int rootCount;                                // ���ڵ��߷�����
	int ply = 0;                                  // ��ǰ�����Ĳ���

	Board();
	~Board();
	void InitZobrist();
	//��ʼ�����ͱ�
	void InitChessType();
	void SetSize(int _size);
	void MakeMove(Pos next);
	void DelMove();
	void Undo();
	void ReStart();
	//��������
	void UpdateType(int x, int y);
	U64 Rand64();
	//��ȡ��λ�õ�key����
	//x,y:����λ�õ�����
	//i:�ĸ�����
	int GetKey(int x, int y, int i);
	//��ȡ�߷��ļ�ֵ
	//a,b,c,d���ĸ����������
	int GetPval(int a, int b, int c, int d);
	//��ȡ����
	//key:��16λ,ÿ��λ�洢��λ�õ�״̬���ڡ��ס��ա�������
	//role:��ʾҪ�ж���һ�������ͣ��ڻ��
	int LineType(int role, int key);
	int ShortLine(int *line);
	//�ж��ǻ�����������
	int CheckFlex3(int *line);
	//�ж��ǻ��Ļ�������
	int CheckFlex4(int *line);
	//�������͸�����
	int GenerateAssist(int len, int len2, int count, int block);

	/* ��������Ա���� */

	// ���ص�step�������ɫ�����ֺ��壬���ְ���
	int color(int step)
	{
		return step & 1;
	}
	// �������x,y�Ƿ�Խ��
	bool CheckXy(int x, int y)
	{
		return cell[x][y].piece != Outside;
	}
	// ������һ�����Cellָ��
	Cell * LastMove()
	{
		return &cell[remMove[step - 1].x][remMove[step - 1].y];
	}
	// role:����=1 ����=0 type:ͳ�����θ��������� c:���̸�λ�õ�Cellָ��
	void TypeCount(Cell *c, int role, int *type)
	{
		++type[c->pattern[role][0]];
		++type[c->pattern[role][1]];
		++type[c->pattern[role][2]];
		++type[c->pattern[role][3]];
	}
	// role:����=1 ����=0 type:Ҫ�жϵ����� c:���̸�λ�õ�Cellָ��
	// ����cell�Ƿ��������type
	bool IsType(Cell *c, int role, int type)
	{
		return c->pattern[role][0] == type|| c->pattern[role][1] == type|| c->pattern[role][2] == type|| c->pattern[role][3] == type;
	}
	// �����һ�����Ƿ������
	bool CheckWin()
	{
		Cell *c = LastMove();

		return c->pattern[opp][0] == win|| c->pattern[opp][1] == win|| c->pattern[opp][2] == win|| c->pattern[opp][3] == win;
	}

};
#endif
