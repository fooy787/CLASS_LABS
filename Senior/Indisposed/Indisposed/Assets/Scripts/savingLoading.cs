using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using System.Runtime.Serialization.Formatters.Binary;
using System.IO;

public class savingLoading : MonoBehaviour {
    public playerData mData;
    // Use this for initialization
    void Start () {
        
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void Save()
    {
        BinaryFormatter bf = new BinaryFormatter();
        FileStream file = File.Open(Application.persistentDataPath + "playerInfo.dat", FileMode.OpenOrCreate);
        playerData pd = new playerData();
        pd.daysRemaining = GameObject.Find("player").GetComponent<playerMovement>().getDays();
        pd.curJournal = GameObject.Find("player").GetComponent<journal>().getJournal();

        bf.Serialize(file, pd);
        file.Close();
    }

    public void Load()
    {
        if(File.Exists(Application.persistentDataPath + "playerInfo.dat"))
        {
            BinaryFormatter bf = new BinaryFormatter();
            FileStream file = File.Open(Application.persistentDataPath + "playerInfo.dat", FileMode.Open);
            mData = (playerData)bf.Deserialize(file);
            file.Close();
            DontDestroyOnLoad(GameObject.Find("dataPassingObject"));
            GameObject.Find("dataPassingObject").GetComponent<dataPasser>().setDays(mData.daysRemaining);
            GameObject.Find("dataPassingObject").GetComponent<dataPasser>().setJournal(mData.curJournal);
            GameObject.Find("GameController").GetComponent<mainMenuButtons>().goToHouse();
        }
    }
}
[Serializable]
public class playerData
{
    public int daysRemaining;
    public List<personInfo> curJournal;
}