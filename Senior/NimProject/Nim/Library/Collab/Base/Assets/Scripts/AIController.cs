using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class AIController : MonoBehaviour {
    public GameObject gameState;
    public GameObject boardRef;
    private float Percent;
    // Use this for initialization
    void Start () {
        //Percent = GameObject.Find("Slider").GetComponent<Slider>().value;
	}
	
	// Update is called once per frame
	void Update () {
        
    }
    public void AITurn()
    {
        StartCoroutine(ExecuteAfterTime(3));
    }
    IEnumerator ExecuteAfterTime(float time)
    {
        yield return new WaitForSeconds(time);
        Debug.Log("BACK TO YOU BOI");
        GameObject[] objs = GameObject.FindGameObjectsWithTag("Piece");
        Debug.Log(objs.Length);
        if(Random.Range(0.0f,100.0f) > Percent)
        {
            makeRightMove();
        }
        else
        {
            makeWrongMove();
        }
        gameState.GetComponent<GameStateManager>().NextTurn();
        // Code to execute after the delay
    }

    void makeWrongMove()
    {

    }

    void makeRightMove()
    {

    }
}
