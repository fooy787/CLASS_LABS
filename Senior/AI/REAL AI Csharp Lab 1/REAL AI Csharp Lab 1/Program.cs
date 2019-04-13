using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace REAL_AI_Csharp_Lab_1
{
    class number
    {
        Random random = new Random();
        public number()
        {
            for (int j = 0; j < 5; j++)
            {
                numList.Add(random.Next(0, 9));
            }
        }
        public List<int> numList;
        public int mNumCorrect;
    }

    class Program
    {
        static void Main(string[] args)
        {
            List<number> crossover(number num1, number num2, List<int> correctNum)
            {
                number child1 = new number();
                number child2 = new number();

                List<number> mList = new List<number>();

                child1.numList[0] = num1.numList[0];
                child1.numList[1] = num1.numList[1];
                child1.numList[2] = num1.numList[2];
                child1.numList[3] = num2.numList[3];
                child1.numList[4] = num2.numList[4];

                child2.numList[0] = num2.numList[0];
                child2.numList[1] = num2.numList[1];
                child2.numList[2] = num2.numList[2];
                child2.numList[3] = num1.numList[3];
                child2.numList[4] = num1.numList[4];

                setNumCorrect(child1, correctNum);
                setNumCorrect(child2, correctNum);

                mList.Add(child1);
                mList.Add(child2);
                return mList;
            }

            List<number> combinationSelection(List<number> pop)
            {
                List<number> returnList = new List<number>();
                Random rn = new Random();
                foreach (number i in pop)
                {
                    int tmp = rn.Next(1,100);
                    if(tmp >= 75)
                    {
                        returnList.Add(i);
                    }
                    
                }
                return (returnList);
            }

            void setNumCorrect(number num1, List<int> numToCheckAgainst)
            {
                int counter = 0;
                for (int i = 0; i < 5; i++)
                {

                    if (num1.numList[i] == numToCheckAgainst[i]) counter++;
                }
                num1.mNumCorrect = counter;
            }

            List<int> mNumber = new List<int>();
            Random random = new Random();
            bool done = false;
            for(int i = 0; i <5; i++)
            {
                mNumber.Add(random.Next(0, 9));
            }
            

            List<number> population = new List<number>();
            
            for(int i = 0; i < 10; i++)
            {
                number temp = new number();
                population.Add(temp);
                setNumCorrect(temp, mNumber);
            }
            while (!done){
                foreach (number i in population)
                {
                    if (i.mNumCorrect == 5)
                    {
                        done = true;
                        Console.Write("Done!");
                    }
                }

                List<number> bestTwo = combinationSelection(population);
                List<number> newChildren = crossover(bestTwo[0], bestTwo[1], mNumber);
            }
        }
    }
}
