using System.Collections;
using System.Collections.Generic;
using System;
using UnityEngine;

public class herbInfoScript : MonoBehaviour {
    private List<string> cures = new List<string>();
    private List<string> causes = new List<string>();
	// Use this for initialization
	public void addCure(string cureToAdd)
    {
        cures.Add(cureToAdd);
        Debug.Log("Added a cure");
        
    }
    public void addCause(string causeToAdd)
    {
        causes.Add(causeToAdd);
    }

    public List<string> returnCureList() { return (cures); }
    public List<string> returnCauseList() { return (causes); }
}
