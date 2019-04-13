using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class textScript : MonoBehaviour {
    public Text text;
    inputScript IS;
    GameObject button;
    float curNetWorkConnectionAmount;
    // Use this for initialization
    void Start () {
         button = GameObject.Find("getOnWheelButton");
        IS = button.GetComponent<inputScript>();
        curNetWorkConnectionAmount = IS.returnNetwork();
    }
	
	// Update is called once per frame
	void Update () {
        curNetWorkConnectionAmount = IS.returnNetwork();
        Debug.Log(curNetWorkConnectionAmount);
        text.text = "Network: " + ((int)curNetWorkConnectionAmount).ToString();
    }
}
