using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.Events;
using Devdog.InventoryPro;


public class journal : MonoBehaviour {
    List<string> illnessList = new List<string>();
    List<personInfo> personList = new List<personInfo>();
    private bool isActive = false;
    public GameObject menu;
    List<herbsForJournal> journalHerbs = new List<herbsForJournal>();

    // Use this for initialization
    void Start () {
        illnessList.Add("Cancer");
        illnessList.Add("Plague");
        illnessList.Add("Diarrhea");
        illnessList.Add("Stroke");
        illnessList.Add("Influenza");
        illnessList.Add("Malaria");
        illnessList.Add("Small Pox");
        illnessList.Add("Ebola");
        illnessList.Add("Cough");
        illnessList.Add("Runny Nose");
        illnessList.Add("Insomnia");
        illnessList.Add("Paralysis");
        illnessList.Add("Diabetes");
        illnessList.Add("Fever");
        illnessList.Add("Malaria");
        illnessList.Add("Sore throat");
        illnessList.Add("Congestion");
        illnessList.Add("Fever");
        illnessList.Add("Malaise");

        //menu = GameObject.Find("person1Text");
        this.addNewPersonToJournal("Bently", 5);
    }
	
	// Update is called once per frame
	void Update () {
		if(Input.GetKeyDown("j"))
        {
            isActive = !isActive;
            menu.SetActive(isActive);
        }
    }

    void addNewPersonToJournal(string name, int numDiseases)
    {
        //Generate a person and get illnesses and add to list
        personInfo tmpPerson = new personInfo();
        for(int i = 0; i < numDiseases; i++)
        {
            int randomNumber = Random.Range(0, 18);
            if (!tmpPerson.getIllnessList().Contains(illnessList[randomNumber]))
            {
                tmpPerson.addIllness(illnessList[randomNumber]);
            }
            else
            {
                randomNumber = Random.Range(0, 18);
                tmpPerson.addIllness(illnessList[randomNumber]);
            }
        }

        tmpPerson.setName(name);
        personList.Add(tmpPerson);

        //Add it onto the UI
        string person = name;
        person += " - ";
        for(int i = 0; i < tmpPerson.getIllnessList().Count; i++)
        {
            person += tmpPerson.getIllnessList()[i];
            person += ", ";
        }
        person = person.Remove(person.Length - 2);
        
        GameObject.Find("person1Text").GetComponent<Text>().text = person;
    }

    public void AddHerb(List<string> cures, List<string> causes)
    {
        herbsForJournal temp = new herbsForJournal();
        temp.AddCures(cures);
        temp.AddCauses(causes);
        journalHerbs.Add(temp);
    }

    public List<personInfo> getJournal()
    {
        return personList;
    }

    public void setJournal(List<personInfo> tmp)
    {
        personList = tmp;
    }

    public void onClick()
    {
        bool useHerb = false;
        var itemsWithID = InventoryManager.FindAll(2, false);
        int curCount = 0;
        //Debug.Log(itemsWithID.ToArray().Length);
        foreach(var herb in journalHerbs)
        {
            var curItemCures = herb.GetCures();
            var curItemCauses = herb.GetCauses();
            curCount++;

            if (useHerb == false)
            { 
                foreach (var illness in personList[0].getIllnessList())
                {
                    Debug.Log(curItemCures.Count);
                    foreach (var cure in curItemCures)
                    {
                        Debug.Log(cure);
                        if (illness.ToString() == cure)
                        {
                            Debug.Log("Cured");
                            useHerb = true;
                            break;
                        }
                    }
                }
            }
            else
            {
                foreach(var cure in curItemCures)
                {
                    personList[0].removeIllness(cure);
                }
                /*foreach(var cause in curItemCauses)
                {
                    if (!personList[0].getIllnessList().Contains(cause))
                    {
                        personList[0].addIllness(cause);
                    }
                }*/
                //InventoryManager.RemoveItem(item.ID, 1, false);
                //journalHerbs.Remove(herb);

                string tmp = personList[0].personName;
                tmp += "- ";
                for(int i = 0; i < personList[0].getIllnessList().Count;i++)
                {
                    tmp += personList[0].getIllnessList()[i];
                    tmp += ", ";
                }
                tmp = tmp.Remove(tmp.Length - 2);

                GameObject.Find("person1Text").GetComponent<Text>().text = tmp;
                break;
            }
        }
        journalHerbs.RemoveAt(curCount - 1);
        curCount = 0;
    }
}

public class herbsForJournal
{
    List<string> cures = new List<string>();
    List<string> causes = new List<string>();

    public void AddCures(List<string> CureList) { cures = CureList; }
    public void AddCauses(List<string> CauseList) { causes = CauseList; }
    public List<string> GetCures() { return cures; }
    public List<string> GetCauses() { return causes; }
}

public class personInfo
{
    List<string> personIllness = new List<string>();
    public string personName;
    
    public void addIllness(string illnessToAdd)
    {
        personIllness.Add(illnessToAdd);
    }

    public void removeIllness(string illnessToRemove)
    {
        if(personIllness.Contains(illnessToRemove))
        {
            personIllness.Remove(illnessToRemove);
        }
    }

    public void setName(string name) { personName = name; }

    public List<string> getIllnessList() { return personIllness; }
}