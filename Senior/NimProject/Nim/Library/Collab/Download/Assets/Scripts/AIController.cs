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
        Percent = 100;
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
        if(Random.Range(0.0f,100.0f) > Percent)
        {
            makeRightMove();
        }
        else
        {
            Debug.Log("RANDOBANDO");
            makeWrongMove();
        }
        gameState.GetComponent<GameStateManager>().NextTurn();
        // Code to execute after the delay
    }

    void makeWrongMove()
    {
        GameObject[] obj = GameObject.FindGameObjectsWithTag("Piece");
        List<GameObject> objs = new List<GameObject>();
        for (int i = 0; i < obj.Length; i++)
        {
            objs.Add(obj[i]);
        }
        List<int> cols = new List<int>();
        for (int i = 0; i < objs.Count; i++)
        {
            if (!cols.Contains(objs[i].GetComponent<NimPiece>().colIdx))
            {
                cols.Add(objs[i].GetComponent<NimPiece>().colIdx);
            }
        }
        int chosen_col = cols[Random.Range(0, cols.Count)];
        List<int> rows = new List<int>();
        for (int i = 0; i < objs.Count; i++)
        {
            if (objs[i].GetComponent<NimPiece>().colIdx == chosen_col)
            {
                rows.Add(objs[i].GetComponent<NimPiece>().rowIdx);
            }
        }
        int number_to_pick = Random.Range(1,rows.Count);
        Debug.Log(number_to_pick);
        Debug.Log(chosen_col);
        for (int i = 0; i < number_to_pick; i++)
        {      
            int chosen_row = rows[Random.Range(0, rows.Count)];
            Debug.Log("CHOSEROW");
            Debug.Log(chosen_row);
            for (int j = 0; j < objs.Count; j++)
            {
                if (objs[j].GetComponent<NimPiece>().colIdx == chosen_col && objs[j].GetComponent<NimPiece>().rowIdx == chosen_row)
                {
                    Destroy(objs[j]);
                    objs.RemoveAt(j);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                }
            }
        }
        

    }

    void makeRightMove()
    {

    }
}
