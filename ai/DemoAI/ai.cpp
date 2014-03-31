#include <iostream>
#include <cstdlib>
#include <string>
#include <vector>
#include <fstream>

using namespace std;

int playerCount, aiPlayer;
vector<vector<int> > graph;
vector<pair<int, vector<int> > > continents;
vector<pair<int, int> > regions;

bool isPreparation;

bool alreadyAttacked;
int lastAttackSource, lastAttackDrain;

bool stayInLoop;

int errorCount;

ofstream log;

void readGameInfo()
{
    cin >> playerCount >> aiPlayer;
    log.close();
    string logname = "logAIX.txt";
    logname[5] = (char)(aiPlayer+'0');
    log.open(&logname[0]);
    log << "I'm N° " << aiPlayer << endl;
}

void readGraph()
{
    graph.clear();
    int n, m;
    cin >> n >> m;
    graph.resize(n);
    regions.resize(n);
    for(int i = 0; i < m; i++)
    {
        int a, b;
        cin >> a >> b;
        graph[a].push_back(b);
        graph[b].push_back(a);
    }
}

void readContinents()
{
    continents.clear();
    int n;
    cin >> n;
    continents.resize(n);
    for(int i = 0; i < n; i++)
    {
        int m;
        cin >> continents[i].first >> m;
        continents[i].second.resize(m);
        for(int j = 0; j < m; j++)
            cin >> continents[i].second[j];
    }
}

void readGameState()
{
    for(int i = 0; i < regions.size(); i++)
    {
        cin >> regions[i].first >> regions[i].second;
    }
}

void roundStart()
{
    alreadyAttacked = false;
}

void placeUnit()
{
    int n;
    cin >> n;

    if(isPreparation)
    {
        for(int i = rand() % regions.size(); i < regions.size(); i++)
        {
            if(regions[i].first == -1)
            {
                cout << "#50" << endl << i << " " << 1 << endl;
                return;
            }
        }
        // zufaellig kein freies gefunden
        for(int i = 0; i < regions.size(); i++)
        {
            if(regions[i].first == -1)
            {
                cout << "#50" << endl << i << " " << 1 << endl;
                return;
            }
        }
        // alle Felder wurden bereits besetzt
        for(int i = rand() % regions.size(); i < regions.size(); i++)
        {
            if(regions[i].first == aiPlayer)
            {
                cout << "#50" << endl << i << " " << 1 << endl;
                return;
            }
        }
        // zufaellig kein geeignetes gefunden
        for(int i = 0; i < regions.size(); i++)
        {
            if(regions[i].first == aiPlayer)
            {
                cout << "#50" << endl << i << " " << 1 << endl;
                return;
            }
        }
    }
    else
    {
        while(n > 0)
        {
            bool noPlace = true;
            for(int i = rand() % regions.size(); i < regions.size() && n > 0; i++)
            {
                if(regions[i].first == aiPlayer)
                {
                    cout << "#50" << endl << i << " " << 1 << endl;
                    n--;
                    noPlace = false;
                    break;
                }
            }
            if(noPlace)
                for(int i = 0; i < regions.size() && n > 0; i++)
                {
                    if(regions[i].first == aiPlayer)
                    {
                        cout << "#50" << endl << i << " " << 1 << endl;
                        n--;
                        break;
                    }
                }
        }
    }
}

void attack()
{
    if(alreadyAttacked)
    {
        cout << "#54" << endl;
        return;
    }
    for(int i = rand() % regions.size(); i < regions.size(); i++)
    {
        if(regions[i].first == aiPlayer && regions[i].second > 4)
        {
            for(int j = 0; j < graph[i].size(); j++)
            {
                if(regions[graph[i][j]].first != aiPlayer)
                {
                    lastAttackSource = i;
                    lastAttackDrain = graph[i][j];
                    cout << "#51" << endl << lastAttackSource << " " << lastAttackDrain << endl;
                    alreadyAttacked = true;
                    return;
                }
            }
        }
    }
    for(int i = 0; i < regions.size(); i++)
    {
        if(regions[i].first == aiPlayer && regions[i].second > 4)
        {
            for(int j = 0; j < graph[i].size(); j++)
            {
                if(regions[graph[i][j]].first != aiPlayer)
                {
                    lastAttackSource = i;
                    lastAttackDrain = graph[i][j];
                    cout << "#51" << endl << lastAttackSource << " " << lastAttackDrain << endl;
                    alreadyAttacked = true;
                    return;
                }
            }
        }
    }
    cout << "#54" << endl;
}

void attackSuccess()
{
    int a, b;
    cin >> a >> b;
    regions[lastAttackSource].second = a;
    regions[lastAttackDrain].second = b;
}

void attackFailed()
{
    int a, b;
    cin >> a >> b;
    regions[lastAttackSource].second = a;
    regions[lastAttackDrain].second = b;
    alreadyAttacked = false;
}

void backRegion()
{
    int a, b;
    cin >> a >> b;
    cout << "#52" << endl << (regions[a].second - 1) / 2 << endl << "#43" << endl;
}

void moveUnits()
{
    cout << "#54" << endl;
}

void processInput(string s)
{
    log << "in: " << s << endl;
    if(s == "#11")
    {
        attackSuccess();
    }
    if(s == "#12")
    {
        attackFailed();
    }
    if(s == "#13")
    {
        isPreparation = false;
    }
    if(s == "#14")
    {
        roundStart();
    }
    if(s == "#30")
    {
        readGameInfo();
    }
    if(s == "#31")
    {
        readGraph();
    }
    if(s == "#32")
    {
        readContinents();
    }
    if(s == "#33")
    {
        readGameState();
    }
    if(s == "#60")
    {
        placeUnit();
    }
    if(s == "#61")
    {
        attack();
    }
    if(s == "#62")
    {
        backRegion();
    }
    if(s == "#63")
    {
        moveUnits();
    }
    if(s == "#64")
    {
        stayInLoop = false;
    }

    if(s == "#20" || s == "#21" || s == "#22" || s == "#23" || s == "#24")
    {
        if(errorCount > 100)
        {
            cout << "#44" << endl;
            errorCount = 0;
        }
        errorCount++;
        log << "error" << endl;
    }
}

int main()
{
    string input;
    cin >> input;
    if(input != "GDay")
        return 0;
    cout << "#70" << endl;
    cout << "WazUp" << endl;

    isPreparation = true;

    log.open("logAI.txt");

    stayInLoop = true;
    while(stayInLoop && cin >> input)
        processInput(input);

    log.close();
    return 0;
}
