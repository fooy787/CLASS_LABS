using System.Collections;
using System.Collections.Generic;
using UnityEngine.UI;
using UnityEngine;

public class GameStateManager : MonoBehaviour {
    public string mCurTurn;
    public Text turnText;
    public Button nextButton;
    public bool canPress;
    public GameObject AI;
    // Use this for initialization
   
    void Start () {
        turnText.text = "Turn:" + mCurTurn;
        canPress = true;
	}
	
	// Update is called once per frame
	void Update () {

    }
    public void NextTurn()
    {
        
        if (mCurTurn == "Player")
        {
            nextButton.gameObject.SetActive(false);
            canPress = false;
            mCurTurn = "AI";
            AI.GetComponent<AIController>().AITurn();
        }
        else
        {
            nextButton.gameObject.SetActive(true);
            canPress = true;
            mCurTurn = "Player";
        }
        turnText.text = "Turn: " + mCurTurn;
        

    }
}
