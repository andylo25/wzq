CFLAG = -std=c++11 -fPIC
CXX = g++

.PHONY : clean

libWineAI.so : ai.o board.o wine.o
	$(CXX) $^ -shared -fPIC -o $@
	@echo WineAI.so is ready!
ai.o : AI.cpp AI.h Board.h
	$(CXX) AI.cpp $(CFLAG) -c -o $@
board.o : Board.cpp Board.h
	$(CXX) Board.cpp $(CFLAG) -c -o $@
wine.o : Wine.cpp AI.h Board.h com_andy_gomoku_ai_WineAI.h 
	$(CXX) Wine.cpp $(CFLAG) -I/usr/lib/jvm/java-1.8.0-openjdk-amd64/include/ -I/usr/lib/jvm/java-1.8.0-openjdk-amd64/include/linux/ -c -o $@
clean :
	-rm -rf *.o WineAI.so

