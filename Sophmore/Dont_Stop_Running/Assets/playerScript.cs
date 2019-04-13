using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class playerScript : MonoBehaviour
{
    bool inUse;
    inputScript IS;
    GameObject button;
    // Use this for initialization
    void Start ()
    {
        button = GameObject.Find("getOnWheelButton");
        IS = button.GetComponent<inputScript>();
        inUse = IS.returnWheelState();
    }
	
	// Update is called once per frame
	void Update ()
    {
        inUse = IS.returnWheelState();
        if (inUse == false)
        {
            this.transform.position = new Vector2(-3,2);
            inUse = true;
        }
        else
        {
            Vector3 tempVec = button.transform.position;
            tempVec.z = -0.5f;
            this.transform.position = tempVec;
            
        }
	}
}
