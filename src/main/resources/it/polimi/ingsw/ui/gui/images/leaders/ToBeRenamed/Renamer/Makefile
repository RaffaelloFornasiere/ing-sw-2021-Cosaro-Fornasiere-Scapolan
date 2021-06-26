COMPILE_OPTIONS = -Wall -Wextra -std=c++17 -O3

main.exe: main.o
	g++ -o main.exe main.o 

main.o: main.cpp 
	g++ -c $(COMPILE_OPTIONS) main.cpp 
	
clean:
	rm -f *.o *.exe *.gch
