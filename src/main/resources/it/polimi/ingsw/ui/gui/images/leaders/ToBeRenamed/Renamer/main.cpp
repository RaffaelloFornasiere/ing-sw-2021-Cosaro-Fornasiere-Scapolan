#include <iostream>
#include <filesystem>
#include <string>
#include <algorithm>
#include <cctype>
namespace fs = std::filesystem;


int main(/*int argc, char const *argv[]*/)
{
	std::string path = "../";

	std::string oldDir;
	int i = 0;
	for (const auto & entry : fs::recursive_directory_iterator(path))
	{


		if(!entry.is_directory() && entry.path().string().find("Renamer") == std::string::npos)
		{
			std::string aux = entry.path().string();
			std::string dir = aux;
			
			dir.erase(dir.find_last_of('/')+1, dir.length());
			int n = std::stoi(aux.substr(aux.find("Group") + std::string("Group").length(), aux.find(".png")));

			std::string newName = dir + "LeaderCard" + std::to_string(n) + ".png";
			std::cout << newName << std::endl;
			fs::rename(entry.path().string(), newName);
		}
	}
	return 0;
}