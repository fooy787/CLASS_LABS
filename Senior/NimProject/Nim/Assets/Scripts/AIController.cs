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
        Percent = GameObject.Find("Slider").GetComponent<Slider>().value;
        GameObject.Find("mCanvas").SetActive(false);
        //Percent = Random.Range(0.0f, 100.0f);
        //Percent = 100;
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
        if(Random.Range(0.0f,100.0f) < Percent)
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
        GameObject[] obj = GameObject.FindGameObjectsWithTag("Piece");
        List<GameObject> objs = new List<GameObject>();
        int row0Count = 0;
        int row1Count = 0;
        int row2Count = 0;
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
                if (objs[i].GetComponent<NimPiece>().colIdx == 2) row0Count++;
                else if (objs[i].GetComponent<NimPiece>().colIdx == 1) row1Count++;
                else if (objs[i].GetComponent<NimPiece>().colIdx == 0) row2Count++;
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
        int curState = (row0Count ^ row1Count ^ row2Count);
        switch(objs.Count)
        {
            case 12:
                Destroy(objs[2]);
                objs.RemoveAt(2);
                boardRef.GetComponent<NimBoard>().NumPieces--;
                Destroy(objs[1]);
                objs.RemoveAt(1);
                boardRef.GetComponent<NimBoard>().NumPieces--;
                break;
            case 11:
                if(row0Count == 0)
                {
                    Destroy(objs[1]);
                    objs.RemoveAt(1);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                    Destroy(objs[0]);
                    objs.RemoveAt(0);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                }
                else if(row0Count == 1)
                {
                    Destroy(objs[2]);
                    objs.RemoveAt(2);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                    Destroy(objs[1]);
                    objs.RemoveAt(1);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                }
                else if(row0Count == 2)
                {
                    Destroy(objs[3]);
                    objs.RemoveAt(3);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                    Destroy(objs[2]);
                    objs.RemoveAt(2);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                }
                else if (row0Count == 3)
                {
                    Destroy(objs[4]);
                    objs.RemoveAt(4);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                    Destroy(objs[3]);
                    objs.RemoveAt(3);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                }
                break;
            case 10:
                Destroy(objs[objs.Count - 1]);
                objs.RemoveAt(objs.Count - 1);
                boardRef.GetComponent<NimBoard>().NumPieces--;
                Destroy(objs[objs.Count - 1]);
                objs.RemoveAt(objs.Count - 1);
                boardRef.GetComponent<NimBoard>().NumPieces--;
                Destroy(objs[objs.Count - 1]);
                objs.RemoveAt(objs.Count - 1);
                boardRef.GetComponent<NimBoard>().NumPieces--;
                Destroy(objs[objs.Count - 1]);
                objs.RemoveAt(objs.Count - 1);
                boardRef.GetComponent<NimBoard>().NumPieces--;
                break;
            case 9:
                Destroy(objs[objs.Count - 1]);
                objs.RemoveAt(objs.Count - 1);
                boardRef.GetComponent<NimBoard>().NumPieces--;
                break;
            case 8:
                Destroy(objs[objs.Count - 1]);
                objs.RemoveAt(objs.Count - 1);
                boardRef.GetComponent<NimBoard>().NumPieces--;
                Destroy(objs[objs.Count - 1]);
                objs.RemoveAt(objs.Count - 1);
                boardRef.GetComponent<NimBoard>().NumPieces--;
                //end
                break;
            case 7:
                if(row1Count == 4)
                {
                    if(row0Count == 3)
                    {
                        Destroy(objs[objs.Count - 1]);
                        objs.RemoveAt(objs.Count - 1);
                        boardRef.GetComponent<NimBoard>().NumPieces--;
                    }
                    else
                    {
                        Destroy(objs[0]);
                        objs.RemoveAt(0);
                        boardRef.GetComponent<NimBoard>().NumPieces--;
                    }
                }
                else if(row0Count == 1)
                {
                    Destroy(objs[0]);
                    objs.RemoveAt(0);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                }
                else
                {
                    Destroy(objs[objs.Count - 1]);
                    objs.RemoveAt(objs.Count - 1);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                }
                
                //my
                break;
            case 6:
                Destroy(objs[0]);
                objs.RemoveAt(0);
                boardRef.GetComponent<NimBoard>().NumPieces--;
                //suffering
                break;
            case 5:
                if (row2Count == 4 | row1Count == 4)
                {
                    Destroy(objs[objs.Count - 1]);
                    objs.RemoveAt(objs.Count - 1);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                    Destroy(objs[objs.Count - 1]);
                    objs.RemoveAt(objs.Count - 1);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                    Destroy(objs[objs.Count - 1]);
                    objs.RemoveAt(objs.Count - 1);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                    Destroy(objs[objs.Count - 1]);
                    objs.RemoveAt(objs.Count - 1);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                }
                else if (row0Count != 0)
                {
                    Destroy(objs[objs.Count - 1]);
                    objs.RemoveAt(objs.Count - 1);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                }
                else
                {
                    Destroy(objs[0]);
                    objs.RemoveAt(0);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                    
                }
                break;
            case 4:
                if(row2Count == 4 | row1Count == 4)
                {
                    Destroy(objs[objs.Count - 1]);
                    objs.RemoveAt(objs.Count - 1);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                    Destroy(objs[objs.Count - 1]);
                    objs.RemoveAt(objs.Count - 1);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                    Destroy(objs[objs.Count - 1]);
                    objs.RemoveAt(objs.Count - 1);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                }
                else
                {
                    Destroy(objs[objs.Count - 1]);
                    objs.RemoveAt(objs.Count - 1);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                }
                break;
            case 3:
                if(row0Count ==  2)
                {
                    Destroy(objs[2]);
                    objs.RemoveAt(2);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                }
                else if(row1Count == 2)
                {
                    Destroy(objs[2]);
                    objs.RemoveAt(2);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                }
                else if(row2Count == 2)
                {
                    Destroy(objs[0]);
                    objs.RemoveAt(0);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                }
                else
                {
                    Destroy(objs[0]);
                    objs.RemoveAt(0);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                    Destroy(objs[0]);
                    objs.RemoveAt(0);
                    boardRef.GetComponent<NimBoard>().NumPieces--;
                }
                break;
            case 2:
                Destroy(objs[0]);
                objs.RemoveAt(0);
                boardRef.GetComponent<NimBoard>().NumPieces--;
                break;
            case 1:
                Destroy(objs[0]);
                objs.RemoveAt(0);
                boardRef.GetComponent<NimBoard>().NumPieces--;
                break;
        }
    }
}
