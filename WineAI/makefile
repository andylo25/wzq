CFLAG = -std=c++11 -O2 -w -D__int64="long long"
CXX = g++

.PHONY : clean

WineAI.so : ai.o board.o wine.o
	$(CXX) $^ -shared -fPIC -o $@
	@echo WineAI.so is ready!
ai.o : AI.cpp AI.h Board.h
	$(CXX) AI.cpp $(CFLAG) -c -o $@
board.o : Board.cpp Board.h
	$(CXX) Board.cpp $(CFLAG) -c -o $@
wine.o : Wine.cpp AI.h Board.h jni_md.h jni.h com_andy_gomoku_ai_WineAI.h 
	$(CXX) Wine.cpp $(CFLAG) -c -o $@
clean :
	-rm -rf *.o WineAI.so

