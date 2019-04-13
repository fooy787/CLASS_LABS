using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Devdog.InventoryPro;

public class herbs : MonoBehaviour
{
    /*
     * This is the storage for the herb info. New functions like
    * a compair and such should be added at a later time.
     */

    bool canCollect = false;

    public InventoryItemBase myItem;
    public struct herbInfo
    {
        string cure;
        string cause;
        public herbInfo(string passedCure, string passedCause)
        {
            cure = passedCure;
            cause = passedCause;
        }
        public string getCure()
        {
            return cure;
        }
        public string getCause()
        {
            return cause;
        }
    }


    //Random rnd = new Random();
    List<herbInfo> bush = new List<herbInfo>();
    List<string> illnessList = new List<string>();
    int chosen;
    int refindChance;
    void Start()
    {
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
        illnessList.Add("None");
        illnessList.Add("Paralysis");
        illnessList.Add("Diabetes");
        illnessList.Add("Fever");
        illnessList.Add("Malaria");
        illnessList.Add("Sore throat");
        illnessList.Add("Congestion");
        illnessList.Add("Fever");
        illnessList.Add("Malaise");
        illnessList.Add("None");

        myItem = ItemManager.database.items[2];
        
    }

    public void searchBush()
    {
        //Debug.Log("starts searchBush");
        refindChance = UnityEngine.Random.Range(0, 4);
        if (bush.Count >= 0)// || refindChance != 0) //change sign after 6t5 for len bush
        {
            string tempCure = illnessList[UnityEngine.Random.Range(0, 21)];
            Instantiate(myItem);
            myItem.GetComponent<herbInfoScript>().addCure(tempCure);
            string tempSymptom = illnessList[UnityEngine.Random.Range(0, 20)]; ///rnd.Next(0, 20)
            myItem.GetComponent<herbInfoScript>().addCause(tempSymptom);
            herbInfo temp = new herbInfo(tempCure, tempSymptom);
            bush.Add(temp);
            //Debug.Log("finished bush search");
            List<string> tmpCureList = new List<string>();
            List<string> tmpCauseList = new List<string>();
            tmpCauseList.Add(tempSymptom);
            tmpCureList.Add(tempCure);
            GameObject.Find("player").GetComponent<journal>().AddHerb(tmpCureList, tmpCauseList);
        }
        InventoryManager.AddItem(myItem);
    }


    void OnTriggerEnter2D(Collider2D Collider)
    {
        canCollect = true;
    }

    void OnTriggerExit2D(Collider2D Collider)
    {
        canCollect = false;
    }

    void Update()
    {
        if (Input.GetKeyDown("space") && canCollect == true)
        {
            Debug.Log("Got a plant!");
            searchBush();
            for (int i = 0; i < bush.Count; i++)
            {
                Debug.Log(bush[i].getCure());
            }

        }
    }
}