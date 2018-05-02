#include "com_andy_gomoku_ai_WineAI.h"
#include "AI.h"
#include<iostream>

/*
* Class:     com_andy_gomoku_ai_WineAI
* Method:    newPoint
* Signature: ()J
*/
JNIEXPORT jlong JNICALL Java_com_andy_gomoku_ai_WineAI_newPoint
(JNIEnv *, jclass, jint size, jint depth, jint turn, jint match){
	AI* wine = new AI;
	if (size <= 0){
		wine->SetSize(15);
	}
	else{
		wine->SetSize(size);
	}
	/*if (depth > 0){
		wine->searchDepth = depth;
	}*/
	if (turn > 0){
		wine->timeout_turn = turn;
	}
	if (match > 0){
		wine->timeout_match = match;
	}
	return (jlong)wine;
}

/*
* Class:     com_andy_gomoku_ai_WineAI
* Method:    deletePoint
* Signature: (J)V
*/
JNIEXPORT void JNICALL Java_com_andy_gomoku_ai_WineAI_deletePoint
(JNIEnv *, jclass, jlong point){
	AI* wine = (AI*)point;
	delete wine;
}

/*
* Class:     com_andy_gomoku_ai_WineAI
* Method:    addChess
* Signature: (JII)Z
*/
JNIEXPORT jboolean JNICALL Java_com_andy_gomoku_ai_WineAI_addChess
(JNIEnv *, jclass, jlong point, jint x, jint y){
	AI* wine = (AI*)point;
	Pos pos = {x,y};
	wine->PutChess(pos);
	return wine->CheckWin();
}

/*
* Class:     com_andy_gomoku_ai_WineAI
* Method:    getBestMove
* Signature: (J)J
*/
JNIEXPORT jlong JNICALL Java_com_andy_gomoku_ai_WineAI_getBestMove
(JNIEnv *, jclass, jlong point){
	AI* wine = (AI*)point;
	auto pos = wine->GetBestMove();
	return pos.x * 10000 + pos.y;
}

/*
* Class:     com_andy_gomoku_ai_WineAI
* Method:    takeBack
* Signature: (J)J
*/
JNIEXPORT void JNICALL Java_com_andy_gomoku_ai_WineAI_takeBack
(JNIEnv *, jclass, jlong point){
	AI* wine = (AI*)point;
	wine->DelMove();
}

int main(){
	std::cout << "请输入下棋坐标(如h8):" << std::endl;
	auto fd = Java_com_andy_gomoku_ai_WineAI_newPoint(0, 0,0,0,0,0);
	auto dd=Java_com_andy_gomoku_ai_WineAI_addChess(0, 0, fd,7,7);
	auto fdd=Java_com_andy_gomoku_ai_WineAI_getBestMove(0,0,fd);
	std::cout << "请输入下棋坐标(如h8):" << fdd << std::endl;
	Java_com_andy_gomoku_ai_WineAI_deletePoint(0, 0, fd);
}
