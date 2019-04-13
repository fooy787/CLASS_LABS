using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Lab4
{
    class neuron
    {
        List<double> inputs;
        List<double> weights;

        bool activated;

        public neuron(List<double> inputInts)
        {
            inputs = inputInts;
            weights = new List<double>(inputs.Count + 1);
            weights[weights.Count-1] = 1;

            Random r =  new Random();

            activated = false;
            for(int i = 0; i < weights.Count; i++)
            {
                weights[i] = r.NextDouble();
            }
        }

        public double Activation()
        {
            double t = 0;
            for(var q = 0; q < inputs.Count; q++)
            {
                t += inputs[q] * weights[q];
            }
            return( 1 / (1 + Math.Exp(-t)));
        }
    }

    

    class Program
    {
        public double neuron_derivative(neuron input)
        {

            return 0;
        }
        static void Main(string[] args)
        {
            int numInputs = 3;
            int numNodes = 3;
            int numOutputs = 3;

            List<neuron> inputLayer = new List<neuron>(numInputs);
            List<double> inputLayerOutputs = new List<double>();
            List<neuron> hiddenLayer = new List<neuron>(numNodes);
            List<double> hiddenLayerOutputs = new List<double>();
            List<neuron> outputLayer = new List<neuron>(numOutputs);
            List<double> outputLayerOutputs = new List<double>();

            foreach(var v in inputLayer)
            {
                inputLayerOutputs.Add(v.Activation());
            }
            for(int i = 0; i < numNodes; i++)
            {
                hiddenLayer[i] = new neuron(inputLayerOutputs);
            }
            foreach(var v in hiddenLayer)
            {
                hiddenLayerOutputs.Add(v.Activation());
            }
            for (int i = 0; i < numOutputs; i++)
            {
                hiddenLayer[i] = new neuron(inputLayerOutputs);
            }
            foreach (var v in outputLayer)
            {
                outputLayerOutputs.Add(v.Activation());
            }
        }
    }
}
