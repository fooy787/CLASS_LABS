using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Globalization;

namespace OccupancyDetectionSet
{
    class Program
    {
        static void Main(string[] args)
        {
            int testpoint = 0; //Change this to select which testpoint to use
            int selector = 5; //Change this to change the column
            double meanUnoccupied = 0;
            double meanOccupied = 0;
            List<string> dates = new List<string>();
            List<double> temperatures = new List<double>();
            List<double> humidities = new List<double>();
            List<double> lights = new List<double>();
            List<double> c02s = new List<double>();
            List<double> humidityRatios = new List<double>();
            List<double> occupancies = new List<double>();
            List<double> newOccupancies = new List<double>();
            int occupancyCounter = 0;
            var lines = File.ReadLines("datatraining.txt");
            int curCount = 0;
            foreach(var line in lines)
            {
                if(curCount!= 0)
                {
                    List<string> temp = line.Split(',').ToList<string>();
                    dates.Add(temp[1]);
                    temperatures.Add(double.Parse(temp[2], CultureInfo.InvariantCulture.NumberFormat));
                    humidities.Add(double.Parse(temp[3], CultureInfo.InvariantCulture.NumberFormat));
                    lights.Add(double.Parse(temp[4], CultureInfo.InvariantCulture.NumberFormat));
                    c02s.Add(double.Parse(temp[5], CultureInfo.InvariantCulture.NumberFormat));
                    humidityRatios.Add(double.Parse(temp[6], CultureInfo.InvariantCulture.NumberFormat));
                    occupancies.Add(double.Parse(temp[7], CultureInfo.InvariantCulture.NumberFormat));
                }
                curCount++;
            }
            
            switch (selector)
            {
                case 1:
                    meanOccupied = 0;
                    meanUnoccupied = 0;
                    occupancyCounter = 0;
                    for (int i = 0; i < temperatures.Count; i++)
                    {
                        if(occupancies[i] == 1)
                        {
                            occupancyCounter++;
                            meanOccupied += temperatures[i];
                        }
                        else
                        {
                            meanUnoccupied += temperatures[i];
                        }
                    }
                    meanOccupied /= occupancyCounter;
                    meanUnoccupied /= (temperatures.Count - occupancyCounter);

                    break;
                case 2:
                    meanOccupied = 0;
                    meanUnoccupied = 0;
                    occupancyCounter = 0;
                    for (int i = 0; i < humidities.Count; i++)
                    {
                        if (occupancies[i] == 1)
                        {
                            occupancyCounter++;
                            meanOccupied += temperatures[i];
                        }
                        else
                        {
                            meanUnoccupied += temperatures[i];
                        }
                    }
                    meanOccupied /= occupancyCounter;
                    meanUnoccupied /= (humidities.Count - occupancyCounter);
                    break;
                case 3:
                    meanOccupied = 0;
                    meanUnoccupied = 0;
                    occupancyCounter = 0;
                    for (int i = 0; i < lights.Count; i++)
                    {
                        if (occupancies[i] == 1)
                        {
                            occupancyCounter++;
                            meanOccupied += temperatures[i];
                        }
                        else
                        {
                            meanUnoccupied += temperatures[i];
                        }
                    }
                    meanOccupied /= occupancyCounter;
                    meanUnoccupied /= (lights.Count - occupancyCounter);
                    break;
                case 4:
                    meanOccupied = 0;
                    meanUnoccupied = 0;
                    occupancyCounter = 0;
                    for (int i = 0; i < c02s.Count; i++)
                    {
                        if (occupancies[i] == 1)
                        {
                            occupancyCounter++;
                            meanOccupied += temperatures[i];
                        }
                        else
                        {
                            meanUnoccupied += temperatures[i];
                        }
                    }
                    meanOccupied /= occupancyCounter;
                    meanUnoccupied /= (c02s.Count - occupancyCounter);
                    break;
                case 5:
                    meanOccupied = 0;
                    meanUnoccupied = 0;
                    occupancyCounter = 0;
                    for (int i = 0; i < humidityRatios.Count; i++)
                    {
                        if (occupancies[i] == 1)
                        {
                            occupancyCounter++;
                            meanOccupied += temperatures[i];
                        }
                        else
                        {
                            meanUnoccupied += temperatures[i];
                        }
                    }
                    meanOccupied /= occupancyCounter;
                    meanUnoccupied /= (humidityRatios.Count - occupancyCounter);
                    break;
            }

            for(; testpoint < lights.Count; testpoint++)
            {

            }
            Console.WriteLine(meanOccupied);
            Console.WriteLine(meanUnoccupied);
            Console.ReadKey();
        }
    }
}
