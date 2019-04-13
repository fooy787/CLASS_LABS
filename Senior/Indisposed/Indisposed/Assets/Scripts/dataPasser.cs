using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class dataPasser : MonoBehaviour {
    int days;
    List<personInfo> mJournal;

    // Use this for initialization
    void Start ()
    {
        days = 20;
	}
	
	// Update is called once per frame
	void Update () {
		
	}
    public void setDays(int nDays)
    {
        days = nDays;
    }

    public int getDays()
    {
        return days;
    }

    public void setJournal(List<personInfo> tmp)
    {
        mJournal = tmp;
    }

    public List<personInfo> getJournal()
    {
        return mJournal;
    }
}